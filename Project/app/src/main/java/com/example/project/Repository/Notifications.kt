package com.example.project.Repository

import com.example.project.Model.Entity.Notification
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Notifications {
    private val db = FirebaseDatabase.getInstance().getReference("Notifications")

    suspend fun save(cusId: String, type: String, title: String, mes: String, time: String): Boolean {
        return try {
            val id = db.push().key ?: return false
            val notification = Notification(id, cusId, type, title, mes, time)
            db.child(id).setValue(notification).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun selectAll(cusId: String): List<Notification>{
        val list = db.orderByChild("cusId").equalTo(cusId).get().await()
        return list.children.mapNotNull { it.getValue(Notification::class.java) }
    }
}