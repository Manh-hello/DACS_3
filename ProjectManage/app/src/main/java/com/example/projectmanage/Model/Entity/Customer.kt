package com.example.projectmanage.Model.Entity

class Customer (
    val id:String,
    val srcimg:String,
    val name:String,
    val email:String,
    val sdt:String,
    val address:String,
    val password:String,
    val birth:String
){
    constructor() : this("","", "", "", "", "", "","")
}