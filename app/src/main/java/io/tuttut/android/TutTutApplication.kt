package io.tuttut.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TutTutApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}