package com.example.project.Repository

import com.example.project.Model.Entity.Shiper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ShiperRepo {
    private val db = FirebaseDatabase.getInstance().getReference("Shipers")

    suspend fun getName(id: String): String {
        val snap = db.child(id).get().await()
        return snap.getValue(Shiper::class.java)?.name ?: ""
    }
}