package com.example.projectmanage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanage.Model.Entity.Chat
import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.Repository.ChatRepo
import com.example.projectmanage.Repository.Customers
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _listCus = MutableLiveData<List<Customer>>()
    val listCus : LiveData<List<Customer>> = _listCus

    private val _cusInfo = MutableLiveData<Customer>()
    val cusInfo : LiveData<Customer> = _cusInfo

    private val _messages = MutableLiveData<List<Chat>>()
    val messages: LiveData<List<Chat>> = _messages

    private val cusRepo = Customers()
    private val chatRepo = ChatRepo()

    fun loadList(storeId:String){
        viewModelScope.launch {
            try {
                val roomIds = chatRepo.selectAllRooms()
                val cusIds = mutableSetOf<String>()
                for (roomId in roomIds) {
                    val parts = roomId.split(" - ")
                    if (parts.size == 2) {
                        val cusId = parts[0]
                        val storeRoomId = parts[1]
                        if (storeRoomId == storeId) {
                            cusIds.add(cusId)
                        }
                    }
                }
                val allCustomers = cusRepo.selectAll()
                _listCus.value = allCustomers.filter { it.id in cusIds }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun info(id: String){
        viewModelScope.launch {
            try {
                _cusInfo.value = cusRepo.info(id)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun addMessage(send: String, receiver: String, message: String, timestamp: Long){
        viewModelScope.launch {
            try {
                chatRepo.save(send,receiver,message,timestamp)
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun loadMessages(roomId: String) {
        viewModelScope.launch {
            try {
                _messages.value = chatRepo.selectMessages(roomId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}