package io.tuttut.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Stable
class TutTutAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
)

@Composable
fun rememberTutTutAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) : TutTutAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        TutTutAppState(
            navController = navController,
            coroutineScope = coroutineScope
        )
    }
}
