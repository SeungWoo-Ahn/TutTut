package io.tuttut.presentation.util

import android.content.Context
import android.content.Intent
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class LinkUtil @Inject constructor() {
    fun openBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}