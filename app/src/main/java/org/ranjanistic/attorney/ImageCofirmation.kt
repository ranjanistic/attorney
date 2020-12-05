package org.ranjanistic.attorney

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.app.summaryzer.R

class ImageCofirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themSetter(themeStatus)
        setContentView(R.layout.activity_image_cofirmation)
        val imagepath = imagePath
        val imageView = findViewById<ImageView>(R.id.captured_img_preview)
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagepath))
        val redobtn = findViewById<ImageButton>(R.id.retakeButt)
        redobtn.setOnClickListener { view: View? -> finish() }
        val cancelbtn = findViewById<ImageButton>(R.id.cancelCaptureButt)
        cancelbtn.setOnClickListener { view: View? ->
            val i = Intent(this@ImageCofirmation, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities
            startActivity(i)
        }
        val donebtn = findViewById<ImageButton>(R.id.doneCaptureButt)
        donebtn.setOnClickListener { view: View? ->
            val i = Intent(this@ImageCofirmation, TextOpenActivity::class.java)
            startActivity(i)
        }
    }

    private fun themSetter(tcode: Int) {
        when (tcode) {
            101 -> this.setTheme(R.style.AppTheme)
            102 -> this.setTheme(R.style.LightTheme)
            103 -> this.setTheme(R.style.joyTheme)
        }
    }

    private val themeStatus: Int
        private get() {
            val mSharedPreferences = getSharedPreferences("theme", MODE_PRIVATE)
            return mSharedPreferences.getInt("themeCode", 0)
        }
    private val imagePath: String?
        private get() {
            val mSharedPreferences = getSharedPreferences("ImageCaptured", MODE_PRIVATE)
            return mSharedPreferences.getString("imagepath", "")
        }
}