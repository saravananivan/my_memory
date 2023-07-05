package com.example.bullseye.utils

import android.graphics.Bitmap

object BitmapScaler {

    //Scale to maintain aspect ratio given a desired width
    fun scaleToFitWidth(b : Bitmap, width: Int): Bitmap{
            val factor = width / b.width.toFloat()
        return Bitmap.createScaledBitmap(b, width, (b.height * factor).toInt(), true)
    }

    //Scale to maintain aspect ratio given a desired height
    fun scaleToFitHeight(b : Bitmap, height: Int): Bitmap{
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, height, (b.width * factor).toInt(), true)
    }

}
