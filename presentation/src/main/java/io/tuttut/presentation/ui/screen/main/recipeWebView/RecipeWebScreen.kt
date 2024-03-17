package io.tuttut.presentation.ui.screen.main.recipeWebView

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.constant.CRAWLING_BASE_URL
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.TutTutTopBar

@Composable
fun RecipeWebRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: RecipeWebViewModel = hiltViewModel()
) {
    val crops by viewModel.crops.collectAsStateWithLifecycle()
    val link by viewModel.link.collectAsStateWithLifecycle()
    val webView = rememberWebView(url = "${CRAWLING_BASE_URL}${link}")

    RecipeWebScreen(
        modifier = modifier,
        cropsName = crops.name,
        webView = webView,
        onBack = onBack
    )
    BackHandler {
        if (webView.canGoBack()) webView.goBack()
        else onBack()
    }
}

@Composable
internal fun RecipeWebScreen(
    modifier: Modifier,
    cropsName: String,
    webView: WebView,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = "$cropsName ${stringResource(id = R.string.crops_recipe)}",
            needBack = true,
            onBack = onBack
        )
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { webView }
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