package com.example.project.Repository

import com.example.project.Model.Entity.Admin
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class StoreRepo {
    private val db = FirebaseDatabase.getInstance().getReference("Stores")

    suspend fun getStore(id: String): Admin? {
        val store = db.child(id).get().await()
        return store.getValue(Admin::class.java)
    }

    suspend fun selectAll(): List<Admin>{
        val snap = db.get().await()
        return snap.children.mapNotNull { it.getValue(Admin::class.java) }
    }

}