package io.tuttut.presentation.ui.screen.main.selectCrops

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.CropsInfoScreenPart
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.withScreenPadding


@Composable
fun SelectCropsRoute(
    modifier: Modifier = Modifier,
    moveDetail: (CropsKey, String) -> Unit,
    moveAdd: () -> Unit,
    onBack: () -> Unit,
    viewModel: SelectCropsViewModel = hiltViewModel()
) {
    SelectCropsScreen(
        modifier = modifier,
        uiState = viewModel.uiState,
        onItemClick = moveDetail,
        onButton = moveAdd,
        onBack = onBack,
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun SelectCropsScreen(
    modifier: Modifier,
    uiState: SelectCropsUiState,
    onBack: () -> Unit,
    onItemClick: (CropsKey, String) -> Unit,
    onButton: () -> Unit
) {
    when (uiState) {
        SelectCropsUiState.Loading -> TutTutLoadingScreen()
        is SelectCropsUiState.Success -> {
            Column(modifier.fillMaxSize()) {
                TutTutTopBar(
                    title = stringResource(id = R.string.select_crops),
                    needBack = true,
                    onBack = onBack
                )
                CropsInfoScreenPart(
                    modifier = Modifier.weight(1f),
                    monthlyCropsList = uiState.monthlyCropsList,
                    cropsInfoList = uiState.cropsInfoList,
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
    }
}
