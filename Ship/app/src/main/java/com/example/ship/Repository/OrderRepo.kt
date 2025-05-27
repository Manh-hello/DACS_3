package com.example.ship.Repository

import com.example.projectmanage.Model.Entity.Order
import com.example.ship.Model.Customer
import com.example.ship.Model.OrderDetail
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class OrderRepo {
    private val dbOrder = FirebaseDatabase.getInstance().getReference("Orders")
    private val dbCus = FirebaseDatabase.getInstance().getReference("Customers")
    private val dbOrderDt = FirebaseDatabase.getInstance().getReference("OrderDetails")

    suspend fun selectAllOrderDetail(): List<OrderDetail>{
        val snap = dbOrderDt.get().await()
        return snap.children.mapNotNull { it.getValue(OrderDetail::class.java) }
    }

    suspend fun loadNameCus(orderId: String): Customer?{
        val snap = dbOrder.child(orderId).get().await()
        val cusId = snap.getValue(Order::class.java)?.cusId
        val cus = dbCus.child(cusId.toString()).get().await()
        return cus.getValue(Customer::class.java)
    }

    suspend fun update(id: String, spId: String){
        val updates = mapOf<String, Any>(
            "status" to "Đang giao",
            "shiper" to spId
        )
        dbOrderDt.child(id).updateChildren(updates).await()
    }

    suspend fun updateSuccess(id: String){
        val updates = mapOf<String,Any>(
            "status" to "Đã giao"
        )
        dbOrderDt.child(id).updateChildren(updates).await()
    }
}