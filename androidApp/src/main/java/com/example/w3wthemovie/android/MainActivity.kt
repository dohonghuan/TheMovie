package com.example.w3wthemovie.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.w3wthemovie.android.ui.AppNavHost
import com.example.w3wthemovie.android.ui.OfflineMessageView
import com.example.w3wthemovie.android.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    private val appContainer by lazy {
        (application as W3WTheMovieApplication).appContainer
    }

    private val networkMonitor by lazy {
        NetworkMonitor(
            context = applicationContext,
            ioDispatcher = Dispatchers.IO,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberW3WTheMovieAppState(
                networkMonitor = networkMonitor,
            )
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
            W3WTheMovieTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost(
                        navController = rememberNavController(),
                        appContainer = appContainer,
                    )

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isOffline,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .safeDrawingPadding()
                            .padding(16.dp),
                    ) {
                        OfflineMessageView()
                    }
                }
            }
        }
    }
}
