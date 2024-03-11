package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding

@Composable
fun CropsInfoScreenPart(
    modifier: Modifier = Modifier,
    monthlyCrops: List<CropsInfo>,
    totalCrops: List<CropsInfo>,
    onItemClick: (String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.padding(screenHorizontalPadding),
        columns = StaggeredGridCells.Fixed(3)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Column {
                Text(
                    text = stringResource(id = R.string.monthly_recommended_crops),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        if (monthlyCrops.isEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
                MonthlyCropsEmpty()
            }
        } else {
            items(
                items = monthlyCrops,
                key = { "${it.key}-monthly" },
                itemContent = { CropsInfoItem(cropsInfo = it, onItemClick = onItemClick) }
            )
        }
        item(span = StaggeredGridItemSpan.FullLine) {
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
            itemContent = { CropsInfoItem(cropsInfo = it, onItemClick = onItemClick) }
        )
    }
}

@Composable
fun CropsInfoItem(modifier: Modifier = Modifier, cropsInfo: CropsInfo, onItemClick: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .height(150.dp)
            .padding(top = 10.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onItemClick(cropsInfo.key) }
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