package com.example.projectmanage.Model

import android.os.Parcelable
import com.example.projectmanage.Model.Entity.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductFull(
    val product: Product,
    val sizes: MutableList<String> = mutableListOf<String>(),
    val colors: MutableList<String> = mutableListOf<String>(),
    val imgs: MutableList<String> = mutableListOf<String>()
) : Parcelable {

}