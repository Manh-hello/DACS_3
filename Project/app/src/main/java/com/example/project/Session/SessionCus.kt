package com.example.project.Session

import android.content.Context

class SessionCus(context: Context) {
    private val prefs = context.getSharedPreferences("id_customers", Context.MODE_PRIVATE)

    fun getCustomerId(): String? = prefs.getString("id_customer", null)

    fun clearCustomerId() {
        prefs.edit().remove("id_customer").apply()
    }
}
