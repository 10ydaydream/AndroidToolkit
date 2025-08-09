package com.daydreaminger.android.app.libs.graphic

import android.graphics.Bitmap

class GraphicLib {

    external fun testOpenCv(srcBitmap: Bitmap): Bitmap

    companion object {
        // Used to load the 'graphic' library on application startup.
        init {
            System.loadLibrary("graphic")
        }
    }
}