package com.example.eduverify.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryHelper {
    // Upload image to Cloudinary
    fun uploadImage(
        uri: Uri,
        context: Context,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        MediaManager.get().upload(uri)
            .option("folder", "eduverify") // Optional: specify a folder
            .option("resource_type", "image")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    if (url != null) {
                        onSuccess(url)
                    } else {
                        onFailure("Error: Secure URL not found.")
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    onFailure(error.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }
}
