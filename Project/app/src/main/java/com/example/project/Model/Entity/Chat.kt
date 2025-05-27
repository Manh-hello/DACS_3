package com.example.project.Model.Entity

data class Chat (
    val send: String = "",
    val receiver: String = "",
    val message: String = "",
    val timestamp: Long = 0
)