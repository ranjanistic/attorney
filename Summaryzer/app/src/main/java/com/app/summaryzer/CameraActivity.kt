package com.app.summaryzer

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.util.concurrent.Executors



class CameraActivity : AppCompatActivity() {
    // This is an arbitrary number we are using to keep track of the permission
// request. Where an app has multiple context for requesting permission,
// this can help differentiate the different contexts.
    private  val REQUEST_CODE_PERMISSIONS = 10
var width = 0
    var height = 0
    private lateinit var caminst: TextView
    private lateinit var campermit: TextView
    // This is an array of all the permission specified in the manifest.
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    @TargetApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themSetter(getThemeStatus())
        setContentView(R.layout.activity_camera)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val attrib = window.attributes
        attrib.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        caminst = findViewById(R.id.camInstruction)
        campermit = findViewById(R.id.camPermitMsg)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x
        height = size.y
        viewFinder = findViewById(R.id.view_finder)
        if(checkCameraHardware(context = applicationContext)) {
            // Request camera permissions
            if (allPermissionsGranted()) {
                campermit.visibility = View.INVISIBLE
                caminst.visibility = View.VISIBLE
                viewFinder.post { startCamera() }
            } else {
                campermit.visibility = View.VISIBLE
                ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }

            // Every time the provided texture view changes, recompute layout
            viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                updateTransform()
            }
        } else {
            Toast.makeText(this,
                    "No camera available.",
                    Toast.LENGTH_SHORT).show()
        }
    }
    // Add this after onCreate

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var viewFinder: TextureView

    private fun startCamera() {
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(width, height))
        }.build()


        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // Add this before CameraX.bindToLifecycle

        // Create configuration object for the image capture use case
        val imageCaptureConfig = ImageCaptureConfig.Builder()
                .apply {
                    // We don't set a resolution for image capture; instead, we
                    // select a capture mode which will infer the appropriate
                    // resolution based on aspect ration and requested mode
                    setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                }.build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)
        findViewById<ImageButton>(R.id.capture_button).setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                        this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } else {
                val file = File(externalMediaDirs.first(),
                        "${System.currentTimeMillis()}.jpg")

                imageCapture.takePicture(file, executor,
                        object : ImageCapture.OnImageSavedListener {
                            override fun onError(
                                    imageCaptureError: ImageCapture.ImageCaptureError,
                                    message: String,
                                    exc: Throwable?
                            ) {
                                val msg = "Photo capture failed: $message"
                                Log.e("CameraXApp", msg, exc)
                                viewFinder.post {
                                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onImageSaved(file: File) {
                                val msg = "Photo capture succeeded: ${file.absolutePath}"
                                Log.d("CameraXApp", msg)
                                viewFinder.post {
                                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                                    val processintent = Intent(this@CameraActivity, TextProcessor::class.java)
                                    startActivity(processintent)
                                }
                            }
                        })
            }
        }

            // Bind use cases to lifecycle
            // If Android Studio complains about "this" being not a LifecycleOwner
            // try rebuilding the project or updating the appcompat dependency to
            // version 1.1.0 or higher.
            CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)

    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                campermit.visibility = View.INVISIBLE
                caminst.visibility = View.VISIBLE
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show()
                campermit.visibility = View.VISIBLE
                caminst.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun themSetter(tcode: Int) {
        when (tcode) {
            101 -> setTheme(R.style.AppTheme)
            102 -> setTheme(R.style.LightTheme)
            103 -> setTheme(R.style.joyTheme)
        }
    }

    private fun getThemeStatus(): Int {
        val mSharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        return mSharedPreferences.getInt("themeCode", 0)
    }
}
