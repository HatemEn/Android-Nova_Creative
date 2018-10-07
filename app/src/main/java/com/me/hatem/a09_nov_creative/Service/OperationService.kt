package com.me.hatem.a09_nov_creative.Service

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object OperationService {
    /*fun stringToDrawable(icon: String) :Drawable {
        return
    }*/

    fun drawableToBitmap(drawable: Drawable) : Bitmap? {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        return null
    }
}