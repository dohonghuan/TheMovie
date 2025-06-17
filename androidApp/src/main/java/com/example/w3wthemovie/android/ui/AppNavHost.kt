package com.example.w3wthemovie.android.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.w3wthemovie.AppContainer
import com.example.w3wthemovie.android.ui.detail.DetailScreen
import com.example.w3wthemovie.android.ui.detail.DetailViewModel
import com.example.w3wthemovie.android.ui.list.ListScreen
import com.example.w3wthemovie.android.ui.list.ListViewModel
import kotlinx.serialization.Serializable

@Serializable
private object MainRoute

@Serializable
private object ListRoute

@Serializable
data class DetailRoute(val id: Long)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appContainer: AppContainer,
) {
    SharedTransitionLayout {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = MainRoute,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            },
        ) {
            navigation<MainRoute>(startDestination = ListRoute) {
                composable<ListRoute> {
                    ListScreen(
                        viewModel = viewModel(
                            factory = ListViewModel.provideFactory(
                                theMovieRepository = appContainer.theMovieRepository,
                            ),
                        ),
                        onClickDetail = { id ->
                            navController.navigate(DetailRoute(id))
                        },
                    )
                }

                composable<DetailRoute> {
                    DetailScreen(
                        viewModel = viewModel(
                            factory = DetailViewModel.provideFactory(
                                theMovieRepository = appContainer.theMovieRepository,
                            )
                        ),
                        onBack = {
                            navController.navigateUp()
                        },
                    )
                }
            }
        }
    }
}