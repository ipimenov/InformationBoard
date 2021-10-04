package ru.ipimenov.informationboard.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager {

    private const val MAX_IMAGE_SIZE = 1000
    private const val IMAGE_WIDTH = 0
    private const val IMAGE_HEIGHT = 1

    fun getImageSize(uriImageString: String): List<Int> {

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uriImageString, options)

        return if (getImageRotation(uriImageString) == 90)
            listOf(options.outHeight, options.outWidth)
        else
            listOf(options.outWidth, options.outHeight)
    }

    private fun getImageRotation(uriImageString: String): Int {
        val rotation: Int
        val imageFile = File(uriImageString)
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        rotation =
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270)
                90
            else
                0
        return rotation
    }

    fun chooseScaleType(imageView: ImageView, bitmap: Bitmap) {

        if (bitmap.width > bitmap.height) {
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

    }

    suspend fun resizeImages(uriImageStrings: List<String>?): List<Bitmap> =
        withContext(Dispatchers.IO) {
            val imageSizesList = ArrayList<List<Int>>()
            val imageBitmapList = ArrayList<Bitmap>()
            for (index in uriImageStrings?.indices!!) {
                val size = getImageSize(uriImageStrings[index])
                val imageRatio = size[IMAGE_WIDTH] / size[IMAGE_HEIGHT].toFloat()
                Log.d("MyLog", "Width: ${size[IMAGE_WIDTH]}, Height: ${size[IMAGE_HEIGHT]}, Ratio: $imageRatio")
                if (imageRatio > 1) {
                    if (size[IMAGE_WIDTH] > MAX_IMAGE_SIZE) {
                        imageSizesList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
                    } else {
                        imageSizesList.add(listOf(size[IMAGE_WIDTH], size[IMAGE_HEIGHT]))
                    }
                } else {
                    if (size[IMAGE_HEIGHT] > MAX_IMAGE_SIZE) {
                        imageSizesList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                    } else {
                        imageSizesList.add(listOf(size[IMAGE_WIDTH], size[IMAGE_HEIGHT]))
                    }
                }
            }

            for (index in uriImageStrings.indices) {
                kotlin.runCatching {
                    imageBitmapList.add(
                        Picasso.get().load(File(uriImageStrings[index]))
                            .resize(imageSizesList[index][IMAGE_WIDTH], imageSizesList[index][IMAGE_HEIGHT])
                            .get()
                    )
                }
            }

            return@withContext imageBitmapList

        }
}