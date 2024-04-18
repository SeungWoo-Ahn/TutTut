package io.tuttut.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.navigation.NavBackStackEntry

fun enterAnimation(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)
     = {
         slideIntoContainer(
             towards = AnimatedContentTransitionScope.SlideDirection.Left,
             animationSpec = spring(
                 dampingRatio = 0.9F,
                 stiffness = Spring.StiffnessLow
             )
        )
     }

fun popEnterAnimation(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)
    = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = spring(
                dampingRatio = 0.9F,
                stiffness = Spring.StiffnessLow
            )
        )
    }