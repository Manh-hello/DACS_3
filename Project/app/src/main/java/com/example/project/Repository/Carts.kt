package com.example.project.Repository

import com.example.project.Model.Entity.Cart
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Carts {
    private val db = FirebaseDatabase.getInstance().getReference("Carts")

    suspend fun save(
        cusId: String,
        proId: String,
        name: String,
        img: String,
        size: String,
        color: String,
        quantity: String,
        price: String
    ) {
        val cartId = db.push().key ?: ""
        val cart = Cart(cartId, cusId, proId, name, img, size, color, quantity.toInt(), price)
        db.child(cartId).setValue(cart)
    }

    suspend fun select(cusId: String): List<Cart> {
        val list = db.orderByChild("cusId").equalTo(cusId).get().await()
        return list.children.mapNotNull { it.getValue(Cart::class.java) }
    }

    suspend fun addQuantity(cartId: String) {
        val snapshot = db.child(cartId).get().await()
        val cart = snapshot.getValue(Cart::class.java)
        cart?.let {
            val newQuantity = it.quantity + 1
            db.child(cartId).child("quantity").setValue(newQuantity).await()
        }
    }

    suspend fun subQuantity(cartId: String) {
        val snapshot = db.child(cartId).get().await()
        val cart = snapshot.getValue(Cart::class.java)
        cart?.let {
            if (it.quantity > 1) {
                val newQuantity = it.quantity - 1
                db.child(cartId).child("quantity").setValue(newQuantity).await()
            }
        }
    }

    suspend fun delete(cartId: String) {
        db.child(cartId).removeValue().await()
    }

    suspend fun clear(cusId: String){
        val snapshot = db.orderByChild("cusId").equalTo(cusId).get().await()
        for (item in snapshot.children) {
            item.ref.removeValue().await()
        }
    }
}