package io.tuttut.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getUriFromPath(path: String): Uri {
        return Uri.fromFile(File(path))
    }

    fun getOptimizedFile(uri: Uri, maxWidth: Int, maxHeight: Int): File? {
        try {
            val tempFile = createTempImageFile()
            val fos = FileOutputStream(tempFile)
            decodeBitmapFromUri(uri, maxWidth, maxHeight)?.apply {
                compress(Bitmap.CompressFormat.JPEG, 100, fos)
                recycle()
            } ?: return null
            fos.flush()
            fos.close()
            return tempFile
        } catch (e: Exception) {
            Log.e(javaClass.name, "ImageUtil - ${e.message}")
        }
        return null
    }

    private fun createTempImageFile(): File {
        val fileName = "${UUID.randomUUID()}.jpg"
        return File(context.cacheDir, fileName)
    }

    private fun decodeBitmapFromUri(uri: Uri, maxWidth: Int, maxHeight: Int): Bitmap? {
        var input = context.contentResolver.openInputStream(uri) ?: return null
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(input, null, options)
        input.close()

        input = context.contentResolver.openInputStream(uri) ?: return null
        options.run {
            inSampleSize = calculateInSampleSize(maxWidth, maxHeight)
            inJustDecodeBounds = false
        }
        val bitmap = BitmapFactory.decodeStream(input, null, options) ?: return null
        input.close()

        val rotatedBitmap = rotateImageIfNeeded(bitmap, uri)
        return resizeBitmapIfNeeded(rotatedBitmap, maxWidth, maxHeight)
    }

    private fun BitmapFactory.Options.calculateInSampleSize(maxWidth: Int, maxHeight: Int): Int {
        val (width, height) = this.run { outWidth to outHeight }
        var inSampleSize = 1
        if (width > maxWidth || height > maxHeight) {
            val halfWidth = width / 2
            val halfHeight = height / 2
            while (halfWidth / inSampleSize >= maxWidth && halfHeight / inSampleSize >= maxHeight) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun resizeBitmapIfNeeded(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val (width, height) = bitmap.run { width to height }
        return if (width > maxWidth || height > maxHeight) {
            var resizedWidth = width
            var resizedHeight = height
            if (width == height) {
                resizedWidth = maxWidth
                resizedHeight = maxHeight
            }
            if (width > height && width > maxWidth) {
                resizedWidth = maxWidth
                resizedHeight = (maxWidth.toDouble() / width * height).toInt()
            }
            if (height > width && height > maxHeight) {
                resizedHeight = maxHeight
                resizedWidth = (maxHeight.toDouble() / height * width).toInt()
            }
            Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, true)
        }
        else bitmap
    }

    private fun rotateImageIfNeeded(bitmap: Bitmap, uri: Uri): Bitmap {
        val filePath = getRealPath(uri) ?: return bitmap
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270F)
            else -> return bitmap
        }
    }

    private fun getRealPath(uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                realPath = cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return realPath
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}