package com.example.eduverify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.eduverify.databinding.ActivityUploadBinding
import com.google.android.material.button.MaterialButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var fileUri: Uri? = null
    private lateinit var photoUri: Uri

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        if (granted) {
            launchGalleryIntent()
        } else {
            Toast.makeText(this, "Permissions required to continue", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data?.data != null) {
            fileUri = result.data!!.data
            binding.tvFileName.text = getFileName(fileUri)
        } else {
            Toast.makeText(this, "File selection cancelled or failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            fileUri = photoUri
            binding.tvFileName.text = "Scanned Document"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseFile.setOnClickListener {
            if (arePermissionsGranted()) {
                launchGalleryIntent()
            } else {
                requestGalleryPermissions()
            }
        }
        val checkButton = findViewById<MaterialButton>(R.id.check)
        val toggleSwitch = findViewById<SwitchCompat>(R.id.switchToggle)
        val lottieCheck = findViewById<LottieAnimationView>(R.id.lottieViewCheck)

        checkButton.setOnClickListener {
            lottieCheck.visibility = View.VISIBLE

            if (toggleSwitch.isChecked) {
                lottieCheck.setAnimation(R.raw.success)
            } else {
                lottieCheck.setAnimation(R.raw.unsuccess)
            }

            lottieCheck.playAnimation()
        }

        binding.btnScanDocument.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
            }
        }

        binding.btnUpload.setOnClickListener {
            fileUri?.let {
                uploadFileToCloudinary(it)
            } ?: Toast.makeText(this, "Please select or scan a file first", Toast.LENGTH_SHORT).show()
        }

        binding.btnBackToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestGalleryPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissions)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir!!)
    }

    private fun launchCamera() {
        val imageFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "$packageName.provider", imageFile)

        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri?): String {
        var result = "Unknown file"
        uri?.let {
            val cursor = contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && c.moveToFirst()) {
                    result = c.getString(nameIndex)
                }
            }
        }
        return result
    }

    private fun uploadFileToCloudinary(uri: Uri) {
        Toast.makeText(this, "Starting upload...", Toast.LENGTH_SHORT).show()
        Log.d("UploadActivity", "Uploading URI: $uri")

        MediaManager.get().upload(uri)
            //.unsigned("your_unsigned_preset") // Uncomment if using unsigned upload preset
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Toast.makeText(this@UploadActivity, "Uploading started", Toast.LENGTH_SHORT).show()
                    Log.d("UploadActivity", "Upload started")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    val progress = (100 * bytes / totalBytes).toInt()
                    runOnUiThread {
                        binding.tvFileName.text = "Uploading... $progress%"
                    }
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val uploadedImageUrl = resultData["secure_url"].toString()
                    Toast.makeText(this@UploadActivity, "Upload successful", Toast.LENGTH_SHORT).show()
                    Log.d("UploadActivity", "Upload success: $uploadedImageUrl")

                    Glide.with(this@UploadActivity)
                        .load(uploadedImageUrl)
                        .into(binding.ivUploadedImage)

                    binding.tvFileName.text = "Uploaded to Cloudinary"
                    fileUri = null
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@UploadActivity, "Upload failed: ${error?.description}", Toast.LENGTH_LONG).show()
                    Log.e("UploadActivity", "Upload error: ${error?.description}")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@UploadActivity, "Upload rescheduled: ${error?.description}", Toast.LENGTH_LONG).show()
                    Log.w("UploadActivity", "Upload rescheduled: ${error?.description}")
                }
            })
            .dispatch()
    }
}
