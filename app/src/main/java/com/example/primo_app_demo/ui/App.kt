package com.example.primo_app_demo.ui

import android.net.Uri
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.primo_app_demo.domain.model.Article
import com.example.primo_app_demo.ui.detail.DetailScreen
import com.example.primo_app_demo.ui.home.HomeScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.lifecycle.compose.currentStateAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { backStackEntry ->
            HomeScreen(
                onArticleClicked = { article ->
                    val json = Json.encodeToString(article)
                    navController.navigate("detail/${Uri.encode(json)}")
                }
            )
        }

        composable(
            "detail/{articleJson}",
            arguments = listOf(
                navArgument("articleJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("articleJson")
            val article = json?.let { Json.decodeFromString<Article>(it) }
            if (article != null) {
                DetailScreen(article, onBackClick = { navController.popBackStack() })
            }
        }
    }
}