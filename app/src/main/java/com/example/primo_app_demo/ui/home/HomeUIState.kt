package com.example.primo_app_demo.ui.home

import com.example.primo_app_demo.domain.model.Article

sealed interface HomeUIState {
    object Loading : HomeUIState
    object Empty : HomeUIState
    data class Success(val articles: List<Article>) : HomeUIState
    data class Error(val message: String) : HomeUIState
}
