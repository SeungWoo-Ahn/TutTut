package io.tuttut.presentation.ui.screen.main.recipeWebView

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun RecipeWebRoute(
    modifier: Modifier = Modifier,
    name: String,
    link: String,
    onBack: () -> Unit,

) {
    val webView = rememberWebView("https://www.10000recipe.com$link")

    DisposableEffect(webView) {
        onDispose {
            webView.destroy()
        }
    }

    RecipeWebScreen(
        modifier = modifier,
        name = name,
        webView = webView,
        onBack = onBack
    )
    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            onBack()
        }
    }
}

@Composable
internal fun RecipeWebScreen(
    modifier: Modifier,
    name: String,
    webView: WebView,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = "$name ${stringResource(id = R.string.crops_recipe)}",
            needBack = true,
            onBack = onBack
        )
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { webView },
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun rememberWebView(url: String): WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }
    return webView
}