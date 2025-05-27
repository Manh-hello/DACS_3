package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Size
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Sizes {
    private val db = FirebaseDatabase.getInstance().getReference("Sizes")

    suspend fun save(sizes : List<String>, id:String){
        sizes.forEach { size->
            val key = db.push().key ?: ""
            db.child(key).setValue(Size(id,size)).await()
        }
    }
    suspend fun select(id: String): List<String>{
        val snap = db.orderByChild("productId").equalTo(id).get().await()
        return snap.children.mapNotNull { it.getValue(Size::class.java)?.size }
    }

    suspend fun update(proId: String, sizes: List<String>) {
        val snap = db.orderByChild("productId").equalTo(proId).get().await()
        for (child in snap.children) {
            child.ref.removeValue().await()
        }
        sizes.forEach { size ->
            val key = db.push().key ?: ""
            db.child(key).setValue(Size(proId, size)).await()
        }
    }

    suspend fun delete(proId: String) {
        val snap = db.orderByChild("productId").equalTo(proId).get().await()
        for (child in snap.children) {
            child.ref.removeValue().await()
        }
    }
}