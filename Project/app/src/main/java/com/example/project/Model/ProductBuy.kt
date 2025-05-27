package com.example.project.Model

class ProductBuy(
    var img: String,
    var name:String,
    val size: String,
    val color: String,
    var price:String,
    val quantity: Int,
){
    constructor(): this("","","","","",0)
}