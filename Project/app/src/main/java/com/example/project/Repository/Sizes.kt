package com.example.project.Repository

import com.example.project.Model.Entity.Size
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Sizes {
    private val db = FirebaseDatabase.getInstance().getReference("Sizes")

    suspend fun select(id: String): List<String>{
        val snap = db.orderByChild("productId").equalTo(id).get().await()
        return snap.children.mapNotNull { it.getValue(Size::class.java)?.size }
    }
}