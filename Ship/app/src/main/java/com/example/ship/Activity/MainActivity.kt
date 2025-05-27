package com.example.ship.Activity

import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ship.Fragment.HomeFragment
import com.example.ship.Fragment.ProfileFragment
import com.example.ship.Fragment.StatisticalFragment
import com.example.ship.R
import com.example.ship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var shiperId: String
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shiperId = intent.getStringExtra("id_shiper").orEmpty()
        if (shiperId.isNotEmpty()) {
            val sharedPref = getSharedPreferences("id_shiper", MODE_PRIVATE)
            sharedPref.edit().putString("id_shiper", shiperId).apply()
        }
        setFragmentInActivity()
        setFragment(HomeFragment())
        selectLayout(binding.layout1)

        binding.btnMenu.setOnClickListener{
            toolBar()
        }

    }
    private fun toolBar(){
        if (binding.menuLayout.visibility == View.INVISIBLE){
            binding.dimView.visibility = View.VISIBLE
            binding.menuLayout.post{
                binding.menuLayout.translationX = -binding.menuLayout.width.toFloat()
                binding.menuLayout.visibility = View.VISIBLE
                binding.menuLayout.animate().translationX(0f).setDuration(200).start()
            }
        }else{
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
    }
    private fun setFragmentInActivity(){
        binding.layout1.setOnClickListener {
            selectLayout(binding.layout1)
            setFragment((HomeFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
        binding.layout2.setOnClickListener {
            selectLayout(binding.layout2)
            setFragment((StatisticalFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
        binding.layout3.setOnClickListener {
            selectLayout(binding.layout3)
            setFragment((ProfileFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
    }
    private fun  selectLayout(layout:LinearLayout){
        binding.layout1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.layout2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.layout3.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
    }
    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}