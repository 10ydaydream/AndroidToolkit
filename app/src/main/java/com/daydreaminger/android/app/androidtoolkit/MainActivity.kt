package com.daydreaminger.android.app.androidtoolkit

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.daydreaminger.android.app.androidtoolkit.databinding.ActivityMainBinding
import com.daydreaminger.android.app.libs.graphic.GraphicLib
import com.daydreaminger.android.toolkit.ui.compat.binding.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        testGraphic()
    }

    private fun testGraphic() {
        binding.btnTestOpencv.setOnClickListener {
            val srcBitmap = createBitmap(300, 300)
            srcBitmap.eraseColor(Color.RED)
            val dstBitmap = GraphicLib().testOpenCv(srcBitmap)
            Log.i(TAG, "testGraphic: >>> dstBitmap = $dstBitmap")
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}