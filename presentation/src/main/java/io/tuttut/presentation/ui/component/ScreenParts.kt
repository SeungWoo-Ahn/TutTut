package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Recipe
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding

@Composable
fun CropsInfoScreenPart(
    modifier: Modifier = Modifier,
    monthlyCrops: List<CropsInfo>,
    totalCrops: List<CropsInfo>,
    onItemClick: (CropsInfo) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.padding(screenHorizontalPadding),
        columns = GridCells.Fixed(3)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Text(
                    text = stringResource(id = R.string.monthly_recommended_crops),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        if (monthlyCrops.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MonthlyCropsEmpty()
            }
        } else {
            items(
                items = monthlyCrops,
                key = { "${it.key}-monthly" },
                itemContent = { CropsSelectItem(cropsInfo = it, onItemClick = { onItemClick(it) }) }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(id = R.string.total_crops),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        items(
            items = totalCrops,
            key = { it.key },
            itemContent = { CropsSelectItem(cropsInfo = it, onItemClick = { onItemClick(it) }) }
        )
    }
}

@Composable
fun CropsSelectItem(modifier: Modifier = Modifier, cropsInfo: CropsInfo, onItemClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .height(150.dp)
            .padding(top = 10.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onItemClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TutTutImage(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            url = cropsInfo.imageUrl
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = cropsInfo.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CropsInfoItem(
    modifier: Modifier = Modifier,
    iconId: Int,
    nameId: Int,
    content: String
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = "crops-info-icon"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id = nameId),
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = content.split("@").joinToString("\n"),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Right,
            lineHeight = 30.sp,
        )
    }
}

@Composable
fun TutTutLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TutTutLoading(size = 50, color = MaterialTheme.colorScheme.primary)
    }
}

fun LazyGridScope.loading(height: Int) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        TutTutLoadingScreen(Modifier.height(height.dp))
    }
}

fun LazyListScope.loading(height: Int) {
    item {
        TutTutLoadingScreen(Modifier.height(height.dp))
    }
}

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    isLeftItem: Boolean,
    onItemClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isLeftItem) screenHorizontalPadding else 8.dp,
                end = if (!isLeftItem) screenHorizontalPadding else 8.dp,
                bottom = 24.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onItemClick
                )
        ) {
            TutTutImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(MaterialTheme.shapes.medium),
                url = recipe.imgUrl
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = recipe.title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}