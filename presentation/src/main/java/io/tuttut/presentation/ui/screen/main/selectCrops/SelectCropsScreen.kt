package io.tuttut.presentation.ui.screen.main.selectCrops

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.CropsInfoScreenPart
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutTopBar


@Composable
fun SelectCropsRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onItemClick: () -> Unit,
    onButton: () -> Unit,
    viewModel: SelectCropsViewModel = hiltViewModel()
) {
    val monthlyCrops by viewModel.cropsInfoRepo.monthlyCropsList.collectAsStateWithLifecycle()
    val totalCrops by viewModel.cropsInfoRepo.cropsInfoList.collectAsStateWithLifecycle()

    SelectCropsScreen(
        modifier = modifier,
        monthlyCrops = monthlyCrops,
        totalCrops = totalCrops,
        onBack = onBack,
        onItemClick = { onItemClick() },
        onButton = onButton
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun SelectCropsScreen(
    modifier: Modifier,
    monthlyCrops: List<CropsInfo>,
    totalCrops: List<CropsInfo>,
    onBack: () -> Unit,
    onItemClick: (CropsInfo) -> Unit,
    onButton: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = stringResource(id = R.string.select_crops),
            needBack = true,
            onBack = onBack
        )
        CropsInfoScreenPart(
            modifier = modifier.weight(1f),
            monthlyCrops = monthlyCrops,
            totalCrops = totalCrops,
            onItemClick = onItemClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = stringResource(id = R.string.select_myself),
                isLoading = false,
                onClick = onButton
            )
        }
    }
}
