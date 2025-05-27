package com.example.ship.Activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ship.R
import com.example.ship.ViewModel.AccountViewModel
import com.example.ship.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val myViewModel: AccountViewModel by viewModels()
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val phone = binding.edtSDT.text.toString()
            val password = binding.edtPassword.text.toString()
            val address = binding.edtAddress.text.toString()
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty()){
                Toast.makeText(this,"Bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }else{
                myViewModel.register(name,email,phone,password,address)
            }
        }
        myViewModel.success.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                binding.edtName.setText("")
                binding.edtEmail.setText("")
                binding.edtSDT.setText("")
                binding.edtPassword.setText("")
                binding.edtAddress.setText("")
                binding.tvError.visibility = View.INVISIBLE
                var build = AlertDialog.Builder(this, R.style.dialog)
                var view = layoutInflater.inflate(R.layout.form_dialog_register, null)
                build.setView(view)
                var btnClose = view.findViewById<ImageButton>(R.id.btnClose)
                var btnAccess = view.findViewById<Button>(R.id.btnAccess)
                btnClose.setOnClickListener { dialog.dismiss() }
                btnAccess.setOnClickListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                dialog = build.create()
                dialog.show()
            }
        }

        myViewModel.error.observe(this) { message ->
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.setText(message)
        }
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