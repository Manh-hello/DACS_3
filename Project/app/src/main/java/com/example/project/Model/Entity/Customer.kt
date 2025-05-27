package com.example.project.Model.Entity

class Customer (
    val id:String,
    var srcimg:String,
    var name:String,
    var email:String,
    var sdt:String,
    var address:String,
    var password:String,
    var birth:String
){
    constructor() : this("","", "", "", "", "", "","")
}