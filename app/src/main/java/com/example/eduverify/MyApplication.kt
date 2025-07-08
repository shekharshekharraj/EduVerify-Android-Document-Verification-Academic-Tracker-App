package com.example.eduverify

import android.app.Application
import com.cloudinary.android.MediaManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Cloudinary SDK with your credentials
        val config = mutableMapOf<String, String>()
        config["cloud_name"] = getString(R.string.cloudinary_cloud_name)
        config["api_key"] = getString(R.string.cloudinary_api_key)
        config["api_secret"] = getString(R.string.cloudinary_api_secret)

        MediaManager.init(this, config)
    }
}
