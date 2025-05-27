package com.example.project.Model.Entity

class CartItem(
    val id:String,
    val cartId:String,
    val productId:String,
    var color:String,
    var quantity:Int,
) {
    constructor():this("","","","",0)
}