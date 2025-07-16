package com.example.primo_app_demo.di

import android.app.Application
import androidx.room.Room
import com.example.primo_app_demo.data.local.AppDatabase
import com.example.primo_app_demo.data.local.ArticleDao
import com.example.primo_app_demo.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://medium.com/")
            .build()

    @Provides
    @Singleton
    fun provideMediumApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "medium_articles.db"
        ).build()

    @Provides
    fun provideArticleDao(db: AppDatabase): ArticleDao =
        db.articleDao()
}
