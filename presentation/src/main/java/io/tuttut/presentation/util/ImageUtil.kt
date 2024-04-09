package io.tuttut.presentation.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getResizedUri(uri: Uri): Uri {
        val path = getRealPath(uri)
        val bitmap = BitmapFactory.decodeFile(path)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

        val tempFile = File.createTempFile("resized_image", ".jpg", context.cacheDir)
        val fileOutputStream = FileOutputStream(tempFile)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.close()
        return Uri.fromFile(tempFile)
    }

    private fun getRealPath(uri: Uri): String? {
        var path: String?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            path = getRealPathAPI19(uri)
            if (path == null) {
                val documentId = DocumentsContract.getDocumentId(uri)
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val file = File("${context.cacheDir.absolutePath}/$documentId")
                if (inputStream != null) {
                    writeFile(inputStream, file)
                    path = file.absolutePath
                }
            }
        } else {
            path = getPath21(uri)
        }
        return path
    }

    private fun getPath21(uri: Uri): String? {
        var fullPath: String? = null
        val column = MediaStore.Images.Media.DATA
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToNext()
            var documentId = cursor.getString(0)
            if (documentId == null) {
                for (i in 0 until cursor.columnCount) {
                    if (column == cursor.getColumnName(i)) {
                        fullPath = cursor.getString(i)
                        break
                    }
                }
            } else {
                documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
                cursor.close()
                fullPath = processDocumentId(documentId)
            }
        }
        return fullPath
    }

    private fun processDocumentId(documentId: String?): String? {
        val projection = arrayOf("_data")
        var fullPath: String? = null
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Media._ID + " = ? ",
                arrayOf(documentId),
                null
            )
            if (cursor != null) {
                cursor.moveToNext()
                fullPath = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return fullPath
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getRealPathAPI19(uri : Uri): String? {
        if(DocumentsContract.isDocumentUri(context, uri)) {
            val documentId = DocumentsContract.getDocumentId(uri)
            when(uri.authority)
            {
                TYPE_EXTERNAL ->{
                    documentId.split(':').apply{
                        if(size > 1) return "${Environment.getExternalStorageDirectory().path}/${this[1]}"
                    }
                }
                TYPE_DOWNLOAD ->{
                    try {
                        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                        val file = File("${context.cacheDir.absolutePath}/$documentId")
                        if (inputStream != null) {
                            writeFile(inputStream,file)
                            return file.absolutePath
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
                TYPE_MEDIA ->{
                    return getDataColumn(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "_id = ? ",
                        arrayOf(documentId.split(':')[1])
                    )
                }
            }
        }
        if("content".equals(uri.scheme, ignoreCase = true)) {
            if(uri.authority == TYPE_GOOGLEPHOTO)
                return uri.lastPathSegment
            return getDataColumn(uri,null,null)
        }
        if("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun writeFile(input: InputStream, file: File) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (input.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun getDataColumn(uri : Uri, selection : String?, selectionArgs : Array<String>?) : String? {
        val projection = arrayOf("_data")
        context.contentResolver.query(uri,projection,selection,selectionArgs,null).use { cursor->
            if(cursor?.moveToFirst() == true) {
                return cursor.getString(cursor.getColumnIndexOrThrow("_data"))
            }
        }
        return null
    }

    companion object {
        const val TYPE_EXTERNAL = "com.android.externalstorage.documents"
        const val TYPE_DOWNLOAD = "com.android.providers.downloads.documents"
        const val TYPE_MEDIA = "com.android.providers.media.documents"
        const val TYPE_GOOGLEPHOTO = "com.google.android.apps.photos.content"
    }
}