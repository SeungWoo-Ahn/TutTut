package io.tuttut.android

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dagger.hilt.android.HiltAndroidApp
import io.tuttut.presentation.BuildConfig

@HiltAndroidApp
class TutTutApplication : Application() {
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(provideAppCheckProviderFactory())
        super.onCreate()
    }

    private fun provideAppCheckProviderFactory(): AppCheckProviderFactory {
        return if (BuildConfig.DEBUG) DebugAppCheckProviderFactory.getInstance()
        else PlayIntegrityAppCheckProviderFactory.getInstance()
    }
}