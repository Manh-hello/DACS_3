package com.example.ship.Model


class Shiper(
    val id: String,
    val src: String,
    val name: String,
    val email: String,
    val pass: String,
    val sdt: String,
    val address: String,
    val date: String
) {
constructor() : this("","","","","","","","")
}