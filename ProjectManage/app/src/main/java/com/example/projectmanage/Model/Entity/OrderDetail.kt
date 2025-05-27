package com.example.projectmanage.Model.Entity

class OrderDetail(
    val id:String,
    val orderId:String,
    val productId:String,
    val src: String,
    val name: String,
    val price: String,
    val quantity:Int,
    val color:String,
    val size: String,
    val status:String,
    val date: String,
    val shiper: String
) {
    constructor():this("","","","","","",0,"","","","","")
}