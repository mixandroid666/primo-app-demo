package com.example.primo_app_demo.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.primo_app_demo.domain.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onArticleClicked: (Article) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isRefreshing = state is HomeUIState.Loading

    Column(modifier = Modifier.padding(bottom = 40.dp)) {
        TopAppBar(title = { Text(text = "Primo Feed") })
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.loadArticles() }
        ) {
            when (val currentState = state) {
                is HomeUIState.Loading -> {
                    ShimmeringLayout()
                }

                is HomeUIState.Empty -> {
                    Text(
                        text = "No articles available.",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is HomeUIState.Success -> {
                    LazyColumnContent(
                        articles = currentState.articles,
                        onArticleClicked = onArticleClicked
                    )
                }

                is HomeUIState.Error -> {
                    Text("Error: ${currentState.message}")
                }
            }
        }
    }
}
