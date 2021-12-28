package com.example.library.data

data class Book(
    val bookId: Int = 0,
    val title: String = "",
    val author: String = "",
    val edition: String = "",
    val docLink: String = "",
    val imageLink: String = "",
    val pages: String = "",
    val chapters: String = "",
    val tags: String = "Tag1",
    val description: String = ""
)