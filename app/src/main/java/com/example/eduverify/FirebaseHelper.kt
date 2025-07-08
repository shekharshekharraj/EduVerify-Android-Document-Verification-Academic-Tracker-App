package com.example.eduverify

import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

object FirebaseHelper {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun currentUser(): FirebaseUser? = auth.currentUser

    fun isLoggedIn(): Boolean = currentUser() != null

    fun logout() {
        auth.signOut()
    }

    fun uploadDocument(
        fileUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val filename = "eduverify/${UUID.randomUUID()}"

        MediaManager.get().upload(fileUri)
            .option("public_id", filename)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("CloudinaryUpload", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    // Optional: show upload progress
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    Log.d("CloudinaryUpload", "Upload success: $resultData")
                    onSuccess()
                }

                override fun onError(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                    Log.e("CloudinaryUpload", "Upload failed: ${error?.description}")
                    onFailure(Exception(error?.description))
                }

                override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                    Log.w("CloudinaryUpload", "Upload rescheduled: ${error?.description}")
                }
            })
            .dispatch()
    }
}
