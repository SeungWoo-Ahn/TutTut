package io.tuttut.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.tuttut.data.model.dto.StorageImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TutTutImage(modifier: Modifier, url: String) {
    GlideImage(
        modifier = modifier,
        model = url,
        contentScale = ContentScale.Crop,
        contentDescription = "web-image"
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryPagerImage(
    modifier: Modifier = Modifier,
    imgUrlList: List<StorageImage>
) {
    val pageCount = imgUrlList.size
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pageCount })
    val indicatorState = rememberLazyListState()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            key = { it }
        ) { page ->
            TutTutImage(
                modifier = Modifier.fillMaxSize(),
                url = imgUrlList[page].url
            )
        }
        LazyRow(
            modifier = Modifier
                .height(50.dp)
                .align(Alignment.BottomCenter),
            state = indicatorState,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(pageCount) {
                val color by animateColorAsState(
                    targetValue = if (it == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    label = "indicator-color"
                )
                val size by animateDpAsState(
                    targetValue = if (it == pagerState.currentPage) 10.dp else 6.dp,
                    label = "indicator-size"
                )
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(color, CircleShape)
                )
            }
        }
    }
}