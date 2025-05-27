package com.example.project.Model.Entity

class Admin(
    val id:String,
    val name:String,
    val email:String,
    val pass:String,
    val address:String,
    val sdt:String,
    val img:String,
    val created:String
) {
    constructor():this("","","","","","","","")
}