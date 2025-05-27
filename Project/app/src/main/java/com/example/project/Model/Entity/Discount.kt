package com.example.projectmanage.Model.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Discount(
    val id:String,
    val storeId:String,
    var code:String,
    var reduce:String,
    val count:Int
) : Parcelable{
    constructor():this("","","","",0)
}