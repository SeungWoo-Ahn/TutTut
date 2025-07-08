package io.tuttut.presentation.ui.screen.main.addCrops

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.presentation.R
import io.tuttut.presentation.mapper.DateFormatStrategy
import io.tuttut.presentation.mapper.format
import io.tuttut.presentation.model.CropsInfoItemUiModel
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.AddCropsCheckBox
import io.tuttut.presentation.ui.component.CropsTypeBottomSheet
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutDatePickerDialog
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.state.DayTextFieldState
import io.tuttut.presentation.ui.state.ITextFieldState
import io.tuttut.presentation.util.withScreenPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddCropsRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveCropsDetail: (String, String) -> Unit,
    onBack: () -> Unit,
    viewModel: AddCropsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val buttonEnabled by remember { derivedStateOf { viewModel.validate() } }

    AddCropsScreen(
        modifier = modifier,
        editMode = viewModel.editMode,
        buttonEnabled = buttonEnabled,
        isLoading = uiState == AddCropsUiState.Loading,
        cropsInfoItem = viewModel.cropsInfoItem,
        plantingDate = viewModel.plantingDate,
        nameState = viewModel.nameState,
        nickNameState = viewModel.nickNameState,
        wateringIntervalState = viewModel.wateringIntervalState,
        growingDayState = viewModel.growingDayState,
        showSheet = viewModel::showCropsTypeSheet,
        showDatePicker = viewModel::showDatePicker,
        onButton = { viewModel.onButton(moveCropsDetail) },
        onBack = onBack,
    )
    if (uiState is AddCropsUiState.ShowCropsTypeSheet) {
        CropsTypeBottomSheet(
            scope = scope,
            monthlyCropsList = uiState.monthlyCropsList,
            cropsInfoList = uiState.cropsInfoList,
            onItemClick = viewModel::onCropsTypeSelected,
            onDismissRequest = viewModel::dismiss
        )
    }
    if (uiState is AddCropsUiState.ShowDatePicker) {
        TutTutDatePickerDialog(
            plantingDate = uiState.plantingDate,
            onDateSelected = viewModel::onDateSelected,
            onDismissRequest = viewModel::dismiss
        )
    }
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddCropsScreen(
    modifier: Modifier,
    editMode: Boolean,
    buttonEnabled: Boolean,
    isLoading: Boolean,
    cropsInfoItem: CropsInfoItemUiModel?,
    plantingDate: String,
    nameState: ITextFieldState,
    nickNameState: ITextFieldState,
    wateringIntervalState: DayTextFieldState,
    growingDayState: DayTextFieldState,
    showSheet: () -> Unit,
    showDatePicker: () -> Unit,
    onButton: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = if (editMode) {
                "작물 ${stringResource(id = R.string.edit)}"
            } else {
                "작물 ${stringResource(id = R.string.add)}"
            },
            needBack = true,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(screenHorizontalPadding)
        ) {
            TutTutLabel(title = stringResource(id = R.string.crops_type), space = 16)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = editMode.not(),
                        onClick = showSheet
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                cropsInfoItem?.let { cropsInfo ->
                    TutTutImage(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        url = cropsInfo.imageUrl
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = cropsInfo.name,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.planting_day))
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clickable(onClick = showDatePicker),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = format(DateFormatStrategy.FullDate(plantingDate)),
                    style = MaterialTheme.typography.labelLarge
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = "calendar-icon"
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            if (cropsInfoItem?.key == CropsKey.CUSTOM) {
                TutTutLabel(title = stringResource(id = R.string.crops_name))
                TutTutTextField(
                    state = nameState,
                    placeHolder = stringResource(id = R.string.crops_name_placeholder),
                    imeAction = ImeAction.Next,
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
            TutTutLabel(title = stringResource(id = R.string.nickname))
            TutTutTextField(
                state = nickNameState,
                placeHolder = stringResource(id = R.string.nickname_placeholder),
                imeAction = if (wateringIntervalState.disabled && growingDayState.disabled) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                },
            )
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.watering_interval))
            if (wateringIntervalState.disabled.not()) {
                TutTutTextField(
                    state = wateringIntervalState,
                    placeHolder = stringResource(id = R.string.watering_interval),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = if (growingDayState.disabled) {
                        ImeAction.Done
                    } else {
                        ImeAction.Next
                    },
                )
            }
            AddCropsCheckBox(
                text = stringResource(id = R.string.unused),
                checked = wateringIntervalState.disabled,
                onCheckedChange = wateringIntervalState::toggleDisabled
            )
            Spacer(modifier = Modifier.height(40.dp))
            if (cropsInfoItem?.key == CropsKey.CUSTOM) {
                TutTutLabel(title = stringResource(id = R.string.growing_day))
                if (growingDayState.disabled.not()) {
                    TutTutTextField(
                        state = growingDayState,
                        placeHolder = stringResource(id = R.string.growing_day),
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    )
                }
                AddCropsCheckBox(
                    text = stringResource(id = R.string.unused),
                    checked = growingDayState.disabled,
                    onCheckedChange = growingDayState::toggleDisabled
                )
                Spacer(modifier = Modifier.height(40.dp))
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
                text = if (editMode) {
                    stringResource(id = R.string.edit)
                } else {
                    stringResource(id = R.string.add)
                },
                isLoading = isLoading,
                enabled = buttonEnabled,
                onClick = onButton
            )
        }
    }
}