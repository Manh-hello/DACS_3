package com.example.projectmanage.Model.Entity

class Evaluate(
    val id:String,
    val cusId: String,
    val rating:Int,
    val desc:String,
    val img: String
) {
    constructor():this("","",0,"","")
}