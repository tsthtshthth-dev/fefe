package com.pix.app.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.pix.app.R
import com.pix.app.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var isRecording = false
    
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera)
        
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        
        setupClickListeners()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    
    private fun setupClickListeners() {
        binding.btnCapture.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }
        
        binding.btnPhoto.setOnClickListener {
            takePhoto()
        }
        
        binding.btnSwitchCamera.setOnClickListener {
            switchCamera()
        }
        
        binding.btnClose.setOnClickListener {
            finish()
        }
        
        binding.btnFlash.setOnClickListener {
            toggleFlash()
        }
        
        binding.btnTimer.setOnClickListener {
            // TODO: Implement timer functionality
            Toast.makeText(this, "Timer feature coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnEffects.setOnClickListener {
            // TODO: Implement effects/filters
            Toast.makeText(this, "Effects feature coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnSpeed.setOnClickListener {
            // TODO: Implement speed control
            Toast.makeText(this, "Speed control coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnBeauty.setOnClickListener {
            // TODO: Implement beauty filters
            Toast.makeText(this, "Beauty filters coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }
            
            imageCapture = ImageCapture.Builder().build()
            
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)
            
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
            
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Failed to start camera", Toast.LENGTH_SHORT).show()
            }
            
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
                put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Pix")
            }
        }
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Photo capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity, "Photo captured successfully", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    private fun startRecording() {
        val videoCapture = this.videoCapture ?: return
        
        binding.btnCapture.isEnabled = false
        
        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }
        
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
                put(android.provider.MediaStore.Video.Media.RELATIVE_PATH, "Movies/Pix")
            }
        }
        
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (ContextCompat.checkSelfPermission(this@CameraActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        binding.btnCapture.apply {
                            text = "Stop"
                            isEnabled = true
                        }
                        isRecording = true
                        updateRecordingUI()
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            Toast.makeText(this@CameraActivity, "Video capture succeeded", Toast.LENGTH_SHORT).show()
                            // Navigate to video upload screen
                            val intent = Intent(this@CameraActivity, com.pix.app.ui.upload.VideoUploadActivity::class.java)
                            intent.putExtra("video_uri", recordEvent.outputResults.outputUri)
                            startActivity(intent)
                            finish()
                        } else {
                            recording?.close()
                            recording = null
                            Toast.makeText(this@CameraActivity, "Video capture failed: ${recordEvent.error}", Toast.LENGTH_SHORT).show()
                        }
                        binding.btnCapture.apply {
                            text = "Record"
                            isEnabled = true
                        }
                        isRecording = false
                        updateRecordingUI()
                    }
                }
            }
    }
    
    private fun stopRecording() {
        recording?.stop()
        recording = null
    }
    
    private fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        startCamera()
    }
    
    private fun toggleFlash() {
        // TODO: Implement flash toggle
        Toast.makeText(this, "Flash toggle coming soon!", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateRecordingUI() {
        if (isRecording) {
            binding.recordingIndicator.visibility = android.view.View.VISIBLE
            binding.btnCapture.setBackgroundResource(R.drawable.record_button_stop)
        } else {
            binding.recordingIndicator.visibility = android.view.View.GONE
            binding.btnCapture.setBackgroundResource(R.drawable.record_button)
        }
    }
    
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}