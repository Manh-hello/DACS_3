package com.example.projectmanage.Activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projectmanage.Adapter.LoadMessageAdapter
import com.example.projectmanage.Model.Entity.Chat
import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.R
import com.example.projectmanage.ViewModel.ChatViewModel
import com.example.projectmanage.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var cusId: String
    private lateinit var storeId: String
    private lateinit var cusInfo: Customer
    private var listMessage = mutableListOf<Chat>()
    private lateinit var adapter: LoadMessageAdapter
    private var x = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightOrange)
        loadInfo()
        obser()
        setUpRV()
        setUpClick()

    }

    private fun obser() {
        chatViewModel.cusInfo.observe(this, Observer {
            cusInfo = it
            if (it.srcimg.isNotEmpty()) {
                Glide.with(binding.avtPerson).load(it.srcimg).into(binding.avtPerson)
            }
            binding.namePerson.setText(it.name)
            x++
            if (x == 2) {
                binding.container.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        })
        chatViewModel.messages.observe(this, Observer {
            listMessage.clear()
            listMessage.addAll(it)
            adapter.notifyDataSetChanged()
            x++
            if (x == 2) {
                binding.container.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        })
    }

    private fun setUpClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnSend.setOnClickListener {
            if (binding.edtMes.text.isNotEmpty()) {
                val timestamp = System.currentTimeMillis()
                chatViewModel.addMessage(storeId, cusId, binding.edtMes.text.toString(), timestamp)
                chatViewModel.loadMessages("$cusId - $storeId")
                binding.edtMes.setText("")
            }
        }
    }

    private fun loadInfo() {
        storeId = getSharedPreferences("id_store", MODE_PRIVATE)
            .getString("id_store", null).orEmpty()
        cusId = intent.getStringExtra("cusId").orEmpty()

        chatViewModel.info(cusId)
        chatViewModel.loadMessages("$cusId - $storeId")
    }

    private fun setUpRV() {
        adapter = LoadMessageAdapter(storeId, listMessage)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
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