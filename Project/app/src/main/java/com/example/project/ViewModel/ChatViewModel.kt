package com.example.project.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.Model.Entity.Admin
import com.example.project.Model.Entity.Chat
import com.example.project.Model.Entity.Customer
import com.example.project.Model.Person
import com.example.project.R
import com.example.project.Repository.ChatRepo
import com.example.project.Repository.StoreRepo
import kotlinx.coroutines.launch

class ChatViewModel():ViewModel() {
    private val _storeInfo = MutableLiveData<Admin>()
    val storeInfo : LiveData<Admin> = _storeInfo

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _listStore = MutableLiveData<List<Admin>>()
    val listStore:LiveData<List<Admin>> = _listStore

    private val _messages = MutableLiveData<List<Chat>>()
    val messages: LiveData<List<Chat>> = _messages

    private val storeRepo = StoreRepo()
    private val chatRepo = ChatRepo()

    fun loadListStore(cusId:String){
        viewModelScope.launch {
            try {
                val roomIds = chatRepo.selectAllRooms()
                val storeIds = mutableSetOf<String>()

                for (roomId in roomIds) {
                    val parts = roomId.split(" - ")
                    if (parts.size == 2) {
                        val roomCusId = parts[0]
                        val roomStoreId = parts[1]
                        if (roomCusId == cusId) {
                            storeIds.add(roomStoreId)
                        }
                    }
                }

                val allStores = storeRepo.selectAll()
                _listStore.value = allStores.filter { it.id in storeIds }
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }
    fun getStore(id: String){
        viewModelScope.launch {
            try {
                _storeInfo.value = storeRepo.getStore(id)
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