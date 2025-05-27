package com.example.projectmanage.Activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projectmanage.Fragment.ChatFragment
import com.example.projectmanage.Fragment.DiscountFragment
import com.example.projectmanage.Fragment.HomeFragment
import com.example.projectmanage.Fragment.ProductFragment
import com.example.projectmanage.Fragment.SettingFragment
import com.example.projectmanage.R
import com.example.projectmanage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeId = intent.getStringExtra("id_store")
        val sharedPref = getSharedPreferences("id_store", MODE_PRIVATE)
        sharedPref.edit().putString("id_store", storeId).apply()

        setFragment(HomeFragment())
        selectLayout(binding.layout1)
        binding.layout1.setBackgroundColor(ContextCompat.getColor(this, R.color.darkGrey))
        setFragmentInActivity()

        binding.btnMenu.setOnClickListener{
            toolBar()
        }
        binding.dimView.setOnClickListener {
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat())
                .setDuration(200).withEndAction {
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
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
            setFragment((ChatFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
        binding.layout3.setOnClickListener {
            selectLayout(binding.layout3)
            setFragment((ProductFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
        binding.layout4.setOnClickListener {
            selectLayout(binding.layout4)
            setFragment((DiscountFragment()))
            binding.dimView.visibility = View.GONE
            binding.menuLayout.animate().translationX(-binding.menuLayout.width.toFloat()).setDuration(200).withEndAction{
                binding.menuLayout.visibility = View.INVISIBLE
            }.start()
        }
        binding.layout5.setOnClickListener {
            selectLayout(binding.layout4)
            setFragment((SettingFragment()))
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
        binding.layout4.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.layout5.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkGrey))
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