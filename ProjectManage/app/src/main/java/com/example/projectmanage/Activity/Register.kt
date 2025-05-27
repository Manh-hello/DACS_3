package com.example.projectmanage.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.RegisterViewModel
import com.example.projectmanage.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bluemore)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val phone = binding.edtSDT.text.toString()
            val password = binding.edtPassword.text.toString()
            registerViewModel.register(name, email, phone, password)
        }
        registerViewModel.registerSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                binding.edtName.setText("")
                binding.edtEmail.setText("")
                binding.edtSDT.setText("")
                binding.edtPassword.setText("")
                var build = AlertDialog.Builder(this, R.style.dialog)
                var view = layoutInflater.inflate(R.layout.form_dialog_register, null)
                build.setView(view)
                var btnClose = view.findViewById<ImageButton>(R.id.btnClose)
                var btnAccess = view.findViewById<Button>(R.id.btnAccess)
                btnClose.setOnClickListener { dialog.dismiss() }
                btnAccess.setOnClickListener {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
                dialog = build.create()
                dialog.show()
            }
        }

        registerViewModel.errorMessage.observe(this) { message ->
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
