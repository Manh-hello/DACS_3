package com.example.projectmanage.Model.Entity

class Admin(
    val id:String,
    var name:String,
    var email:String,
    var pass:String,
    var address:String,
    var sdt:String,
    var img:String,
    val created:String
) {
    constructor():this("","","","","","","","")
}