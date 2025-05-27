package com.example.projectmanage.Model.Entity

class Order(
    val id:String,
    val cusId: String,
    val reduce:String,
    val pay:String,
    val total:String
) {
    constructor():this("","","","","")
}