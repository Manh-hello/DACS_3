package com.example.ship.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ship.R
import com.example.ship.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.lOrange)
        setContentView(binding.root)

        var handler = Handler(Looper.getMainLooper())
        var progress = 0
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(progress<=100){
                    binding.progressbar.progress = progress
                    progress += 2
                    handler.postDelayed(this,30)
                }else{
                    val i = Intent(this@IntroActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        },0)


    }
}