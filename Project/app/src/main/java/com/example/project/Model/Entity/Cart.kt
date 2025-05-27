package com.example.project.Model.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Cart(
    val id:String,
    val cusId:String,
    val proId: String,
    val name: String,
    val img: String,
    val size: String,
    val color: String,
    val quantity:Int,
    val price: String
): Parcelable {
    constructor():this("","","","","","","",0,"")
}