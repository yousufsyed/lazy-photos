package com.yousuf.photos.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yousuf.photos.ui.screen.PhotoDetailsScreen
import com.yousuf.photos.ui.screen.PhotoListScreen
import com.yousuf.photos.viewmodel.PhotosViewModel

@Composable
fun PhotosNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    photosViewModel: PhotosViewModel,
    startDestination: String = Destination.List.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Destination.List.route) {
            PhotoListScreen(navController, photosViewModel)
        }
        composable(route = "{Destination.Details.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            PhotoDetailsScreen(id)
        }
    }
}

enum class Screen {
    List,
    Details
}

sealed class Destination(val route: String) {
    object List : Destination(Screen.List.name)
    object Details : Destination(Screen.Details.name)
}
