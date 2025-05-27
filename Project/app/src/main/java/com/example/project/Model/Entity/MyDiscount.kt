package com.example.project.Model.Entity

class MyDiscount(
    val id: String,
    val cusId:String,
    val discountId:String,
    val code:String,
    val reduce: String,
    var status: Boolean
) {
    constructor():this("","","","","",true)
}