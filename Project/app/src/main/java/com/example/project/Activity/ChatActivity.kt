package com.example.project.Activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project.Adapter.LoadMessageAdapter
import com.example.project.Model.Entity.Chat
import com.example.project.R
import com.example.project.ViewModel.ChatViewModel
import com.example.project.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var cusId: String
    private lateinit var storeId: String
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var adapter: LoadMessageAdapter
    private var x = 0
    private var listMessage = mutableListOf<Chat>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        loadInfo()
        setUpRV()
        setUpClick()
        obser()
    }

    private fun obser() {
        chatViewModel.storeInfo.observe(this, Observer {
            binding.apply {
                if(it.img.isNotEmpty()){
                    Glide.with(avtPerson).load(it.img).into(avtPerson)
                }
                namePerson.setText(it.name)
            }
            x++
            if (x == 2) {
                binding.container.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        })
        chatViewModel.messages.observe(this) { messages ->
            listMessage.clear()
            listMessage.addAll(messages)
            adapter.notifyDataSetChanged()
            binding.rv.scrollToPosition(messages.size - 1)
            x++
            if (x == 2) {
                binding.container.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnSend.setOnClickListener {
            val messageText = binding.edtMes.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val timestamp = System.currentTimeMillis()
                chatViewModel.addMessage(cusId, storeId, messageText, timestamp)
                chatViewModel.loadMessages("$cusId - $storeId")
                binding.edtMes.setText("")
            }
        }
    }

    private fun loadInfo() {
        val sharedPref = getSharedPreferences("id_customers", AppCompatActivity.MODE_PRIVATE)
        cusId = sharedPref.getString("id_customer", null).toString()
        storeId = intent.getStringExtra("storeId").orEmpty()


        if (storeId.isNotEmpty()) {
            chatViewModel.getStore(storeId)
            chatViewModel.loadMessages("$cusId - $storeId")
        }
    }

    private fun setUpRV() {
        adapter = LoadMessageAdapter(cusId, listMessage)
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