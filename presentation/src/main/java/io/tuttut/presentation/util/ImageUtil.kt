package io.tuttut.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun compressToFile(uri: Uri): File {
        return suspendCoroutine { continuation ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()


                val rotatedBitmap: Bitmap?
                    = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        rotateBitmap(bitmap, uri)
                    } else null
                val resizedBitmap = resizeBitmap(rotatedBitmap ?: bitmap)

                val extension: String
                val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    extension = "webp"
                    Bitmap.CompressFormat.WEBP_LOSSLESS
                } else {
                    extension = "png"
                    Bitmap.CompressFormat.PNG
                }
                val outputFile = File(context.cacheDir, "image_${System.currentTimeMillis()}.${extension}")
                val outputStream = FileOutputStream(outputFile)
                resizedBitmap.compress(compressFormat, 90, outputStream)
                outputStream.close()

                continuation.resume(outputFile)

            } catch (e: IOException) {
                e.printStackTrace()
                continuation.resumeWithException(e)
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val ratio = width.toFloat() / height.toFloat()

        var targetWidth = MAX_IMAGE_SIZE
        var targetHeight = (targetWidth / ratio).toInt()

        if (width > height) {
            targetHeight = MAX_IMAGE_SIZE
            targetWidth = (targetHeight * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun rotateBitmap(bitmap: Bitmap, uri: Uri): Bitmap {
        val ei = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        return when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


    companion object {
        private const val MAX_IMAGE_SIZE = 300
    }
}