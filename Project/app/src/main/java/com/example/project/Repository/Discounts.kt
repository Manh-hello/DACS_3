package com.example.project.Repository

import com.example.projectmanage.Model.Entity.Discount
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Discounts {
    private val db = FirebaseDatabase.getInstance().getReference("Discounts")

    suspend fun selectAll(): List<Discount>{
        val snap = db.get().await()
        return snap.children.mapNotNull { it.getValue(Discount::class.java) }
    }


    suspend fun update(id: String){
        val dc = db.child(id).get().await().getValue(Discount::class.java)?.count ?: 0
        var newDis = dc - 1
        val updates = mapOf<String, Any>(
            "count" to newDis
        )
        db.child(id).updateChildren(updates).await()
    }
}