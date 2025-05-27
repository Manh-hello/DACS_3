package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Color
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Colors {
    private val db = FirebaseDatabase.getInstance().getReference("Colors")

    suspend fun save(colors:List<String>,id:String){
        colors.forEach { color->
            val key = db.push().key?:""
            db.child(key).setValue(Color(id,color)).await()
        }
    }
    suspend fun select(id: String): List<String>{
        val snap = db.orderByChild("productId").equalTo(id).get().await()
        return snap.children.mapNotNull { it.getValue(Color::class.java)?.color }
    }

    suspend fun update(proId: String, colors: List<String>) {
        val snap = db.orderByChild("productId").equalTo(proId).get().await()
        for (child in snap.children) {
            child.ref.removeValue().await()
        }
        colors.forEach { color ->
            val key = db.push().key ?: ""
            db.child(key).setValue(Color(proId, color)).await()
        }
    }

    suspend fun delete(proId: String) {
        val snap = db.orderByChild("productId").equalTo(proId).get().await()
        for (child in snap.children) {
            child.ref.removeValue().await()
        }
    }
}