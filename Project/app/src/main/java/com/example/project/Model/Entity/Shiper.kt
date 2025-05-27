package com.example.project.Model.Entity


class Shiper(
    val id: String,
    val name: String,
    val email: String,
    val pass: String,
    val sdt: String,
    val address: String,
    val date: String
) {
constructor() : this("","","","","","","")
}