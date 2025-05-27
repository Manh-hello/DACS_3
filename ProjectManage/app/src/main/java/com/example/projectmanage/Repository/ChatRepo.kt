package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Chat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ChatRepo {
    private val db = FirebaseDatabase.getInstance().getReference("Chats")

    suspend fun save(storeId: String, cusId: String,message: String, timestamp: Long){
        val x = Chat(storeId,cusId,message,timestamp)
        db.child("${cusId} - ${storeId}").push().setValue(x).await()
    }

    suspend fun selectAllRooms(): List<String> {
        val snapshot = db.get().await()
        val roomList = mutableListOf<String>()
        for (roomSnapshot in snapshot.children) {
            roomSnapshot.key?.let { roomList.add(it) }
        }
        return roomList
    }

    suspend fun selectMessages(roomId: String): List<Chat> {
        val snapshot = db.child(roomId).get().await()
        val messageList = mutableListOf<Chat>()
        for (messageSnapshot in snapshot.children) {
            val message = messageSnapshot.getValue(Chat::class.java)
            message?.let { messageList.add(it) }
        }
        return messageList
    }

}