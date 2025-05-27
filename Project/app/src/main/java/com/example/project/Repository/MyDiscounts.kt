package com.example.project.Repository

import com.example.project.Model.Entity.MyDiscount
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MyDiscounts {
    private val db = FirebaseDatabase.getInstance().getReference("MyDiscounts")

    suspend fun select(cusId: String) :List<MyDiscount>{
        val snap = db.orderByChild("cusId").equalTo(cusId).get().await()
        return snap.children.mapNotNull { it.getValue(MyDiscount::class.java) }
    }

    suspend fun save(
        cusId: String,
        discountId: String,
        code: String,
        reduce: String
    ) {
        val id = db.push().key ?: return
        val myDiscount = MyDiscount(id, cusId, discountId, code, reduce, true)
        db.child(id).setValue(myDiscount).await()
    }

    suspend fun update(id: String) {
        val updates = mapOf<String, Any>(
            "status" to false
        )
        db.child(id).updateChildren(updates).await()
    }
}
