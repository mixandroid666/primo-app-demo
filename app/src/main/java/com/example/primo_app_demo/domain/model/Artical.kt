package com.example.primo_app_demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val title: String,
    val link: String,
    val pubDate: String,
    val description: String,
    val image: String? = ""
)