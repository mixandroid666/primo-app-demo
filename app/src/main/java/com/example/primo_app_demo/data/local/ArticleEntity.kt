package com.example.primo_app_demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.primo_app_demo.domain.model.Article


@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val link: String,
    val title: String,
    val pubDate: String,
    val description: String,
    val image: String
)

fun ArticleEntity.toDomain(): Article {
    return Article(
        title = this.title,
        link = this.link,
        pubDate = this.pubDate,
        description = this.description,
        image = this.image
    )
}