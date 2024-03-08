package io.tuttut.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.tuttut.presentation.theme.TutTutTheme
import io.tuttut.presentation.ui.TutTutApp
import io.tuttut.presentation.ui.rememberTutTutAppState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberTutTutAppState()
            TutTutTheme {
                TutTutApp(appState)
            }
        }
    }
}