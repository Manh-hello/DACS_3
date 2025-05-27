package com.example.projectmanage.Model

import android.app.Application
import com.cloudinary.android.MediaManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Khởi tạo Cloudinary tại đây
        val config: HashMap<String, String> = hashMapOf(
            "cloud_name" to "dbnww6mst",
            "upload_preset" to "oyba1hle"
        )
        MediaManager.init(this, config)
    }
}