package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TutTutBottomSheet(
    showSheet: Boolean,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    sheetState: SheetState,
    windowInsets: WindowInsets = WindowInsets(top = 100.dp),
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = containerColor,
            sheetState = sheetState,
            windowInsets = windowInsets,
            dragHandle = null,
            properties = properties,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropsTypeBottomSheet(
    showSheet: Boolean,
    scope: CoroutineScope,
    monthlyCrops: List<CropsInfo>,
    totalCrops: List<CropsInfo>,
    onItemClick: (CropsInfo) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenHorizontalPadding)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.select_crops),
                style = MaterialTheme.typography.headlineLarge
            )
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        scope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                onDismissRequest()
                            }
                    },
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = "x-icon",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        CropsInfoScreenPart(
            monthlyCrops = monthlyCrops,
            totalCrops = totalCrops,
            onItemClick = { cropsInfo ->
                scope.launch {
                    onItemClick(cropsInfo)
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismissRequest()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    showSheet: Boolean,
    scope: CoroutineScope,
    onSelectReportReason: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenHorizontalPadding)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.report),
                style = MaterialTheme.typography.headlineLarge
            )
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterEnd)
                    .clickable {
                        scope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                onDismissRequest()
                            }
                    },
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = "x-icon",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        val reasons = listOf(
            "유출/사칭/사기",
            "낚시/놀람/도배",
            "음란물",
            "상업적 광고 및 판매",
            "욕설/비하"
        )
        LazyColumn(
            contentPadding = PaddingValues(screenHorizontalPadding)
        ) {
            items(
                items = reasons,
                key = { it }
            ) {
                TextButton(text = it, onClick = { onSelectReportReason(it) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegativeBottomSheet(
    showSheet: Boolean,
    title: String = stringResource(id = R.string.delete_warning),
    buttonText: String = stringResource(id = R.string.delete),
    scope: CoroutineScope,
    onButton: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(top = 0.dp),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenHorizontalPadding)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            TutTutButton(
                text = buttonText,
                isLoading = false,
                buttonColor = MaterialTheme.colorScheme.error,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onButton()
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TutTutButton(
                text = stringResource(id = R.string.close),
                isLoading = false,
                buttonColor = MaterialTheme.colorScheme.inverseSurface,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismissRequest()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarvestBottomSheet(
    showSheet: Boolean,
    scope: CoroutineScope,
    onHarvest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(top = 0.dp),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenHorizontalPadding)
        ) {
            Text(
                text = stringResource(id = R.string.harvest_announce),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            TutTutButton(
                text = stringResource(id = R.string.harvest),
                isLoading = false,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onHarvest()
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TutTutButton(
                text = stringResource(id = R.string.close),
                isLoading = false,
                buttonColor = MaterialTheme.colorScheme.inverseSurface,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismissRequest()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyBottomSheet(
    showSheet: Boolean,
    isLoading: Boolean,
    policyChecked: Boolean,
    personalChecked: Boolean,
    onPolicyCheckedChange: (Boolean) -> Unit,
    onPersonalCheckedChange: (Boolean) -> Unit,
    showPolicy: () -> Unit,
    showPersonal: () -> Unit,
    onAgreement: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = false)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        windowInsets = WindowInsets(top = 0.dp),
        properties = properties,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenHorizontalPadding)
        ) {
            Text(
                text = stringResource(id = R.string.policy_agreement),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            PolicyButton(
                name = stringResource(id = R.string.service_policy_agreement),
                checked = policyChecked,
                onCheckedChange = { if (!isLoading) onPolicyCheckedChange(it) },
                showPolicy = showPolicy
            )
            Spacer(modifier = Modifier.height(10.dp))
            PolicyButton(
                name = stringResource(id = R.string.service_policy_agreement),
                checked = personalChecked,
                onCheckedChange = { if (!isLoading) onPersonalCheckedChange(it) },
                showPolicy = showPersonal
            )
            Spacer(modifier = Modifier.height(30.dp))
            TutTutButton(
                text = stringResource(id = R.string.continue_with_agree),
                isLoading = isLoading,
                onClick = onAgreement
            )
        }
    }
}