package com.example.project.Model.Entity

class Notification (
    val id: String,
    val cusId: String,
    val type: String,
    val title: String,
    val mes: String,
    val time: String
){
    constructor():this("","","","","","")
}