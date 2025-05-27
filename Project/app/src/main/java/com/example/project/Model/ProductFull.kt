package com.example.project.Model

import android.os.Parcelable
import com.example.project.Model.Entity.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductFull(
    val product: Product,
    val sizes: MutableList<String> = mutableListOf<String>(),
    val colors: MutableList<String> = mutableListOf<String>(),
    val imgs: MutableList<String> = mutableListOf<String>()
) : Parcelable {

}
