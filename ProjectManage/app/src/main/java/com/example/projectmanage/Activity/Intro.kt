package com.example.projectmanage.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.projectmanage.R
import com.example.projectmanage.databinding.ActivityIntroBinding

class Intro : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.be)
        setContentView(binding.root)

        var handler = Handler(Looper.getMainLooper())
        var progress = 0
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    binding.progressbar.progress = progress
                    progress += 2
                    handler.postDelayed(this, 30)
                } else {
                    val i = Intent(this@Intro, Login::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }, 0)
    }
}