package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.CropsInfoItem
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.screen.main.selectCrops.cropsInfoList

@Composable
fun CropsInfoDetailRoute(modifier: Modifier = Modifier, onBack: () -> Unit, onItemClick: () -> Unit, onButton: () -> Unit) {
    CropsInfoDetailScreen(
        modifier = modifier,
        onBack = onBack,
        onItemClick = onItemClick,
        onButton = onButton
    )
    BackHandler(onBack = onBack)
}

@Composable
fun CropsInfoDetailScreen(
    modifier: Modifier,
    onBack: () -> Unit,
    onItemClick: () -> Unit,
    onButton: () -> Unit
) {
    val cropsInfo = cropsInfoList[0]
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = cropsInfo.name,
            needBack = true,
            onBack = onBack
        )
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .weight(1f)
                .padding(screenHorizontalPadding),
            columns = StaggeredGridCells.Fixed(2)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        text = stringResource(id = R.string.crops_info),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    CropsInfoItem(
                        iconId = R.drawable.ic_trophy,
                        nameId = R.string.difficulty,
                        content = cropsInfo.difficulty.displayName
                    )
                    CropsInfoItem(
                        iconId = R.drawable.ic_shovel,
                        nameId = R.string.planting,
                        content = cropsInfo.plantingSeasons.joinToString("/") { it.toString() }
                    )
                    CropsInfoItem(
                        iconId = R.drawable.ic_gap,
                        nameId = R.string.planting_interval,
                        content = cropsInfo.plantingInterval
                    )
                    CropsInfoItem(
                        iconId = R.drawable.ic_water,
                        nameId = R.string.watering,
                        content = cropsInfo.wateringIntervalStr
                    )
                    CropsInfoItem(
                        iconId = R.drawable.ic_harvest,
                        nameId = R.string.harvesting,
                        content = cropsInfo.harvestSeasons.joinToString("/") { it.toString() }
                    )
                    Spacer(modifier = Modifier.height(54.dp))
                    Text(
                        text = "${cropsInfo.name} ${stringResource(id = R.string.crops_recipe)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = "${cropsInfo.name} ${stringResource(id = R.string.add)}",
                isLoading = false,
                onClick = onButton
            )
        }
    }
}