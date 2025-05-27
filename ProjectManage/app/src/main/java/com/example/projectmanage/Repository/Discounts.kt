package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Discount
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlin.math.cos

class Discounts {
    private val db = FirebaseDatabase.getInstance().getReference("Discounts")
    suspend fun save(storeId:String,
                     code:String,
                     reduce:String,
                     count:Int){
        val id = db.push().key!!
        val discount = Discount(id,storeId,code, reduce, count)
        db.child(discount.id).setValue(discount).await()
    }
    suspend fun select(storeId: String): List<Discount>{
        var snap = db.orderByChild("storeId").equalTo(storeId).get().await()
        return snap.children.mapNotNull { it.getValue(Discount::class.java) }
    }
    suspend fun update(id: String, quantity: Int, reduce: String){
        val updates = mapOf<String, Any>(
            "count" to quantity,
            "reduce" to reduce
        )
        db.child(id).updateChildren(updates).await()
    }
    suspend fun delete(id: String){
        db.child(id).removeValue().await()
    }
}