package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Customer
import com.example.projectmanage.Model.Entity.Order
import com.example.projectmanage.Model.Entity.OrderDetail
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Orders {
    private val dbOrder = FirebaseDatabase.getInstance().getReference("Orders")
    private val db = FirebaseDatabase.getInstance().getReference("OrderDetails")
    private val dbCus = FirebaseDatabase.getInstance().getReference("Customers")

    suspend fun selectAll(): List<OrderDetail>{
        val snap = db.get().await()
        return snap.children.mapNotNull { it.getValue(OrderDetail::class.java) }
    }

    suspend fun updateAccess(id: String){
        val updates = mapOf<String, Any>(
            "status" to "Đã xác nhận"
        )
        db.child(id).updateChildren(updates).await()
    }

    suspend fun updateError(id: String){
        val updates = mapOf<String, Any>(
            "status" to "Đã hủy"
        )
        db.child(id).updateChildren(updates).await()
    }

    suspend fun loadNameCus(orderId: String): Customer?{
        val snap = dbOrder.child(orderId).get().await()
        val cusId = snap.getValue(Order::class.java)?.cusId
        val cus = dbCus.child(cusId.toString()).get().await()
        return cus.getValue(Customer::class.java)
    }
}