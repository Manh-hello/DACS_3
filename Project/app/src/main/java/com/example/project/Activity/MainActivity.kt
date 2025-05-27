package com.example.project.Activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project.Fragment.Bell_Fragment
import com.example.project.Fragment.Chat_Fragment
import com.example.project.Fragment.Home_Fragment
import com.example.project.Fragment.Profile_Fragment
import com.example.project.R
import com.example.project.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        setFragment(Home_Fragment())

        val fragmentName = intent.getStringExtra("fragment")
        val userId = intent.getStringExtra("id_customer")
        if (userId != null) {
            val sharedPref = getSharedPreferences("id_customers", MODE_PRIVATE)
            sharedPref.edit().putString("id_customer", userId).apply()
        }

        if (fragmentName == "fragment_chat") {
            setFragment(Chat_Fragment())
            updateSelectedLayout()
            binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnChat.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }else if (fragmentName == "fragment_bell"){
            setFragment(Bell_Fragment())
            updateSelectedLayout()
            binding.tvBell.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnBell.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }else {
            binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnHome.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }

        binding.layout1.setOnClickListener {
            setFragment(Home_Fragment())
            updateSelectedLayout()
            binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnHome.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
        binding.layout2.setOnClickListener {
            setFragment(Chat_Fragment())
            updateSelectedLayout()
            binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnChat.setColorFilter(ContextCompat.getColor(this, R.color.black))
        }
        binding.layout3.setOnClickListener {
            updateSelectedLayout()
            setFragment(Bell_Fragment())
            binding.tvBell.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnBell.setColorFilter(ContextCompat.getColor(this, R.color.black))

        }
        binding.layout4.setOnClickListener {
            updateSelectedLayout()
            setFragment(Profile_Fragment())
            binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.btnProfile.setColorFilter(ContextCompat.getColor(this, R.color.black))

        }
    }

    fun updateSelectedLayout() {
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnHome.setColorFilter(ContextCompat.getColor(this, R.color.white))
        binding.tvChat.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnChat.setColorFilter(ContextCompat.getColor(this, R.color.white))
        binding.tvBell.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnBell.setColorFilter(ContextCompat.getColor(this, R.color.white))
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnProfile.setColorFilter(ContextCompat.getColor(this, R.color.white))
//        selectedLayout.background = ContextCompat.getDrawable(this, R.drawable.bottomnav_item_select)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus?.clearFocus() // Hủy focus EditText
        }
        return super.dispatchTouchEvent(ev)
    }

}