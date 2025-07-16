package com.example.primo_app_demo.data.repository

import android.util.Log
import com.example.primo_app_demo.data.local.ArticleDao
import com.example.primo_app_demo.data.local.ArticleEntity
import com.example.primo_app_demo.data.local.toDomain
import com.example.primo_app_demo.data.remote.ApiService
import com.example.primo_app_demo.domain.model.Article
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ArticleDao
) {
    suspend fun hasLocalData(): Boolean {
        return dao.getArticleCount() > 0
    }

    suspend fun loadArticles(): List<Article> {
        return if (hasLocalData()) {
            val local = dao.getArticlesOnce().map { it.toDomain() }
            val articlesFromApi = fetchArticlesFromApi()
            articlesFromApi.ifEmpty { local }
        } else {
            val articlesFromApi = fetchArticlesFromApi()
            articlesFromApi.ifEmpty {
                dao.getArticlesOnce().map { it.toDomain() }
            }
        }
    }

    suspend fun fetchArticlesFromApi(): List<Article> {
        return try {
            val response = api.getMediumFeed()
            val xml = response.body()?.string() ?: return emptyList()

            val doc = Jsoup.parse(xml, "", Parser.xmlParser())

            Log.d("xxx", "fetchArticlesFromApi: $doc")
            val articles = doc.select("item").mapNotNull { item ->
                val title = item.selectFirst("title")?.text()
                val link = item.selectFirst("link")?.text()
                val pubDate = item.selectFirst("pubDate")?.text() ?: ""
                val description = item.selectFirst("description")?.text() ?: ""

                val contentEncoded = item.selectFirst("content|encoded")?.text()

                val imageUrl = contentEncoded?.let { content ->
                    val contentDoc = Jsoup.parse(content)
                    val imgElement = contentDoc.selectFirst("img")
                    imgElement?.attr("src") ?: ""
                } ?: ""

                if (title == null || link == null) return@mapNotNull null

                Article(
                    title = title,
                    link = link,
                    pubDate = pubDate,
                    description = description,
                    image = imageUrl
                )
            }

            val entities = articles.map {
                ArticleEntity(
                    title = it.title,
                    link = it.link,
                    pubDate = it.pubDate,
                    description = it.description,
                    image = it.image ?: ""
                )
            }

            dao.clearArticles()
            dao.insertArticles(entities)

            articles
        } catch (e: Exception) {
            Log.e("ArticleRepository", "Error fetching articles from API", e)
            emptyList()
        }
    }

}
