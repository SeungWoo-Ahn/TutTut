package io.tuttut.android

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TutTutApplication : Application() {
    override fun onCreate() {
        Firebase.initialize(this)
        Firebase.appCheck.installAppCheckProviderFactory(provideAppCheckProviderFactory())
        super.onCreate()
    }

    private fun provideAppCheckProviderFactory(): AppCheckProviderFactory {
        return if (BuildConfig.DEBUG_MODE) DebugAppCheckProviderFactory.getInstance()
        else PlayIntegrityAppCheckProviderFactory.getInstance()
    }
}