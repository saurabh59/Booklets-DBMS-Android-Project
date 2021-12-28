package com.example.library.data

data class User(
    var name: String? = null,
    var email: String? = null,
    @field:JvmField
    val isUser: Int = 1,
    val books: MutableList<String>? = mutableListOf()
)
