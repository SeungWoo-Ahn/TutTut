package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import io.tuttut.data.network.model.CropsInfoDto
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.screen.login.LoginUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TutTutBottomSheet(
    showSheet: Boolean,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    sheetState: SheetState,
    windowInsets: WindowInsets = WindowInsets(
        top = 0.dp,
        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    ),
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = containerColor,
            sheetState = sheetState,
            contentWindowInsets = { windowInsets },
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
    monthlyCrops: List<CropsInfoDto>,
    totalCrops: List<CropsInfoDto>,
    onItemClick: (CropsInfoDto) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = showSheet,
        sheetState = sheetState,
        windowInsets = WindowInsets(top = 100.dp),
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
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
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
        Column(
            modifier = Modifier.padding(screenHorizontalPadding)
        ) {
            listOf(
                "유출/사칭/사기",
                "낚시/놀람/도배",
                "음란물",
                "상업적 광고 및 판매",
                "욕설/비하"
            ).forEach { reason ->
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = reason,
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            onSelectReportReason(reason)
                        }
                    }
                )
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
    uiState: LoginUiState,
    togglePolicyChecked: (LoginUiState.PolicySheetState.Idle) -> Unit,
    togglePersonalChecked: (LoginUiState.PolicySheetState.Idle) -> Unit,
    showPolicy: () -> Unit,
    showPersonal: () -> Unit,
    onAgreement: (JoinRequest) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    TutTutBottomSheet(
        showSheet = uiState is LoginUiState.PolicySheetState,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
        onDismissRequest = onDismissRequest
    ) {
        val state = uiState as LoginUiState.PolicySheetState
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
                checked = state.policyChecked,
                onCheckedChange = {
                    if (state is LoginUiState.PolicySheetState.Idle)
                        togglePolicyChecked(state)
                },
                showPolicy = showPolicy
            )
            Spacer(modifier = Modifier.height(10.dp))
            PolicyButton(
                name = stringResource(id = R.string.service_policy_agreement),
                checked = state.personalChecked,
                onCheckedChange = {
                    if (state is LoginUiState.PolicySheetState.Idle)
                        togglePersonalChecked(state)
                },
                showPolicy = showPersonal
            )
            Spacer(modifier = Modifier.height(30.dp))
            TutTutButton(
                text = stringResource(id = R.string.continue_with_agree),
                isLoading = state == LoginUiState.PolicySheetState.Loading,
                onClick = {
                    if (state is LoginUiState.PolicySheetState.Idle)
                        onAgreement(state.joinRequest)
                }
            )
        }
    }
}