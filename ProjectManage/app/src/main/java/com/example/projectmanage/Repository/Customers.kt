package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Customer
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Customers {
    private val db = FirebaseDatabase.getInstance().getReference("Customers")

    suspend fun selectAll() : List<Customer>{
        val snap = db.get().await()
        return snap.children.mapNotNull { it.getValue(Customer::class.java) }
    }

    suspend fun info(id: String): Customer? {
        val cus = db.child(id).get().await()
        return cus.getValue(Customer::class.java)
    }
}