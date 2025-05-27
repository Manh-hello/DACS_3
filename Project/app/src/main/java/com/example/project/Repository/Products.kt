package com.example.project.Repository

import com.example.project.Model.Entity.Product
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class Products {
    private val db = FirebaseDatabase.getInstance().getReference("Products")


    suspend fun selectAll(): List<Product>?{
        val snap = db.get().await()
        if(!snap.exists()){
            return emptyList()
        }
        val productList = mutableListOf<Product>()
        for (s in snap.children){
            val product = s.getValue(Product::class.java)?:continue
            productList.add(product)
        }
        return productList
    }

    suspend fun updateStar(id: String, star: Double){
        val us = mapOf<String, Any>(
            "star" to star
        )
        db.child(id).updateChildren(us).await()
    }

    suspend fun searchByName(query: String): List<Product>? {
        val snap = db.get().await()
        if (!snap.exists()) {
            return emptyList()
        }

        val productList = mutableListOf<Product>()
        for (s in snap.children) {
            val product = s.getValue(Product::class.java) ?: continue
            if (product.name?.contains(query, ignoreCase = true) == true) {
                productList.add(product)
            }
        }
        return productList
    }

    suspend fun update(id: String, qtt:Int) {
        val product = db.child(id).get().await().getValue(Product::class.java) ?: return

        val quantity  = product.quantity ?: 0
        val count = product.count ?: 0
        val newQty      = (quantity - qtt).coerceAtLeast(0)
        val newCount    = count + qtt

        val updates = mapOf<String, Any>(
            "quantity" to newQty,
            "count"    to newCount
        )
        db.child(id).updateChildren(updates).await()
    }
}