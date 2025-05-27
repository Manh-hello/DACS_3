package com.example.projectmanage.Repository

import com.example.projectmanage.Model.Entity.Product
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Products {
    private val db = FirebaseDatabase.getInstance().getReference("Products")

    suspend fun save(
        storeId: String,
        name: String,
        price: String,
        desc: String,
        quantity: Int,
    ): String {
        val id = db.push().key ?: return "không thể tạo id sản phẩm mới"
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formartDate = format.format(calendar.time)
        val product = Product(id, storeId, name, price, desc, quantity, 0.0, 0, formartDate)
        db.child(product.id).setValue(product).await()
        return id
    }

    suspend fun select(storeId: String): List<Product>? {
        val snap = db.orderByChild("storeId").equalTo(storeId).get().await()
        if (!snap.exists()) {
            return emptyList()
        }
        val productList = mutableListOf<Product>()
        for (s in snap.children) {
            val product = s.getValue(Product::class.java) ?: continue
            productList.add(product)
        }
        return productList
    }

    suspend fun update(
        proId: String,
        name: String,
        price: String,
        desc: String,
        quantity: Int
    ) {
        val updates = mapOf<String, Any>(
            "name" to name,
            "price" to price,
            "quantity" to quantity,
            "desc" to desc
        )
        db.child(proId).updateChildren(updates).await()
    }

    suspend fun delete(proId: String) {
        db.child(proId).removeValue().await()
    }
}
