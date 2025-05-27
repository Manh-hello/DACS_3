package com.example.project.Repository

import com.example.projectmanage.Model.Entity.Order
import com.example.projectmanage.Model.Entity.OrderDetail
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Orders {
    private val dbOrder = FirebaseDatabase.getInstance().getReference("Orders")
    private val dbOrderDetail = FirebaseDatabase.getInstance().getReference("OrderDetails")

    suspend fun save(cusId: String, reduce:String, pay:String, total:String) : String{
        val orderId = dbOrder.push().key?:""

        val order = Order(orderId,cusId,reduce,pay,total)

        dbOrder.child(orderId).setValue(order).await()
        return orderId
    }

    suspend fun saveDetail(orderId: String, productId:String, src: String, name: String, price: String, quantity:Int, color:String, size: String,time: String){
        val orderDetailId = dbOrderDetail.push().key?:""
        val orderDetail = OrderDetail(orderDetailId,orderId,productId,src,name,price,quantity,color,size,"Chờ cửa hàng xác nhận",time,"",false)
        dbOrderDetail.child(orderDetailId).setValue(orderDetail).await()
    }

    suspend fun selectOrder(cusId: String):List<Order>{
        val list = dbOrder.orderByChild("cusId").equalTo(cusId).get().await()
        return list.children.mapNotNull { it.getValue(Order::class.java) }
    }

    suspend fun selectOrderDetail(orderId: String): List<OrderDetail>{
        val list = dbOrderDetail.orderByChild("orderId").equalTo(orderId).get().await()
        return list.children.mapNotNull { it.getValue(OrderDetail::class.java) }
    }

    suspend fun updateAccess(id: String){
        val updates = mapOf<String, Any>(
            "status" to "Đã nhận"
        )
        dbOrderDetail.child(id).updateChildren(updates).await()
    }

    suspend fun updateEvaluate(id: String){
        val us = mapOf<String, Any>(
            "evaluate" to true
        )
        dbOrderDetail.child(id).updateChildren(us).await()
    }

    suspend fun updateError(id: String){
        val updates = mapOf<String, Any>(
            "status" to "Đã hủy"
        )
        dbOrderDetail.child(id).updateChildren(updates).await()
    }

    suspend fun selectOne(id: String): OrderDetail? {
        val i = dbOrderDetail.child(id).get().await()
        return i.getValue(OrderDetail::class.java)
    }
}