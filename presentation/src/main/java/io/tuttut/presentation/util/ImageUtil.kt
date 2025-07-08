package io.tuttut.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.path.createTempFile

@Singleton
class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun compressUriToFile(uri: Uri, reqWidth: Int, reqHeight: Int): Result<File> =
        withContext(Dispatchers.IO) {
            runCatching {
                val downSampledBitmap = getDownSampledBitmap(uri, reqWidth, reqHeight).getOrThrow()
                val correctedBitmap = rotateBitmapIfRequired(downSampledBitmap, uri)
                    .getOrElse { t ->
                        downSampledBitmap.recycle()
                        throw t
                    }
                val (suffix, compressFormat) = getSuffixAndCompressFormatByVersion()
                val tempFile = createTempImageFile(suffix)
                compressBitmapToFile(correctedBitmap, tempFile, compressFormat)
                    .also {
                        downSampledBitmap.recycle()
                        correctedBitmap.recycle()
                    }
                    .getOrElse { t ->
                        tempFile.delete()
                        throw t
                    }
                tempFile
            }
        }

    private fun getDownSampledBitmap(uri: Uri, reqWidth: Int, reqHeight: Int): Result<Bitmap> {
        val options = BitmapFactory
            .Options()
            .apply {
                inJustDecodeBounds = true
                decodeUriToBitmap(uri)
                inSampleSize = calcInSampleSize(reqWidth, reqHeight)
                inJustDecodeBounds = false
            }
        return options.decodeUriToBitmap(uri)
    }

    private fun BitmapFactory.Options.decodeUriToBitmap(uri: Uri): Result<Bitmap> = runCatching {
        context.contentResolver.openInputStream(uri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, this)
                ?: throw RuntimeException("bitmap decoding failed")
        }
    }

    private fun BitmapFactory.Options.calcInSampleSize(reqWidth: Int, reqHeight: Int): Int {
        val (width, height) = outWidth to outHeight
        var inSampleSize = 1
        if (width > reqWidth || height > reqHeight) {
            val halfWidth = width shr 1
            val halfHeight = height shr 1
            while (halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight) {
                inSampleSize = inSampleSize shl 1
            }
        }
        return inSampleSize
    }

    private suspend fun rotateBitmapIfRequired(bitmap: Bitmap, uri: Uri): Result<Bitmap> =
        withContext(Dispatchers.Default) {
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val exif = ExifInterface(inputStream)
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    val degrees = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }
                    if (degrees == 0f) {
                        bitmap
                    } else {
                        val matrix = Matrix().apply { postRotate(degrees) }
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    }
                } ?: bitmap
            }
        }

    private fun getSuffixAndCompressFormatByVersion(): Pair<String, CompressFormat> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ".webp" to CompressFormat.WEBP_LOSSY
        } else {
            ".jpeg" to CompressFormat.JPEG
        }

    private fun createTempImageFile(suffix: String): File =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createTempFile(
                directory = context.cacheDir.toPath(),
                prefix = FILE_NAME_PREFIX,
                suffix = suffix
            ).toFile()
        } else {
            File.createTempFile(FILE_NAME_PREFIX, suffix, context.cacheDir)
        }

    private fun compressBitmapToFile(
        bitmap: Bitmap,
        file: File,
        compressFormat: CompressFormat,
        quality: Int = 100,
    ): Result<Boolean> = runCatching {
        FileOutputStream(file).use { outStream ->
            bitmap.compress(compressFormat, quality, outStream)
        }
    }

    companion object {
        private const val FILE_NAME_PREFIX = "tuttut_upload_"
    }
}