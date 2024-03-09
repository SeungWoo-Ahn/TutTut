package io.tuttut.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.navigation.TutTutNavHost
import io.tuttut.presentation.util.GoogleAuthClient

@Composable
fun TutTutApp(
    appState: TutTutAppState,
    googleAuthClient: GoogleAuthClient,
    startDestination: ScreenGraph
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            TutTutNavHost(
                appState = appState,
                googleAuthClient = googleAuthClient,
                startDestination = startDestination
            )
        }
    }
}