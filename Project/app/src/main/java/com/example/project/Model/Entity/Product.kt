package com.example.project.Model.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product(
    val id:String,
    val storeId:String,
    val name:String,
    val price:String,
    val desc:String,
    val quantity:Int,
    val star: Double,
    val count:Int
) : Parcelable{
    constructor(): this("","","","","",0,0.0,0)
}