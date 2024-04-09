package io.tuttut.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getOptimizeUri(uri: String): String? {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")
            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)
            decodeBitmapFromUri(uri.toUri())?.apply {
                compress(Bitmap.CompressFormat.JPEG, 100, fos)
                recycle()
            } ?: throw NullPointerException()
            fos.flush()
            fos.close()
            return tempFile.absolutePath
        } catch (e: Exception) {
            Log.e(javaClass.name, "ImageUtil - ${e.message}")
        }
        return null
    }

    private fun decodeBitmapFromUri(uri: Uri): Bitmap? {
        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))
        input.mark(input.available())
        var bitmap: Bitmap?
        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)
            input.reset()
            inSampleSize = calculateInSampleSize()
            inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(input, null, this)?.apply {
               rotateBitmap(this, uri)
            }
        }
        input.close()
        return bitmap
    }

    private fun BitmapFactory.Options.calculateInSampleSize(): Int {
        val (height, width) = this.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > MAX_HEIGHT || width > MAX_WIDTH) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun rotateBitmap(bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val exif = ExifInterface(input)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        Log.d(javaClass.name, "rotateBitmap: $orientation")
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        private const val MAX_WIDTH = 400
        private const val MAX_HEIGHT = 400
    }
}