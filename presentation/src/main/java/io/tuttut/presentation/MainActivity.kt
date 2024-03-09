package io.tuttut.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import io.tuttut.presentation.navigation.ScreenGraph
import io.tuttut.presentation.theme.TutTutTheme
import io.tuttut.presentation.ui.TutTutApp
import io.tuttut.presentation.ui.rememberTutTutAppState
import io.tuttut.presentation.util.GoogleAuthClient

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy { GoogleAuthClient(Identity.getSignInClient(applicationContext)) }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val startDestination = ScreenGraph.LoginGraph
        setContent {
            val appState = rememberTutTutAppState()
            TutTutTheme {
                TutTutApp(appState, googleAuthClient, startDestination)
            }
        }
    }
}