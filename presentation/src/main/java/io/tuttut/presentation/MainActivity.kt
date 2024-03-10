package io.tuttut.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.theme.TutTutTheme
import io.tuttut.presentation.ui.TutTutApp
import io.tuttut.presentation.ui.rememberTutTutAppState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val startDestination = ScreenGraph.LoginGraph
        setContent {
            val appState = rememberTutTutAppState()
            TutTutTheme {
                TutTutApp(appState, startDestination)
            }
        }
    }
}