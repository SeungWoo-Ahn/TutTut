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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.constant.CUSTOM_IMAGE
import io.tuttut.data.constant.CUSTOM_NAME
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.AddCropsCheckBox
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.CropsTypeBottomSheet
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutDatePickerDialog
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.state.IEditTextState
import io.tuttut.presentation.util.getFormattedDate
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddCropsRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    onBack: () -> Unit,
    onButton: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: AddCropsViewModel = hiltViewModel()
) {
    val monthlyCrops by viewModel.cropsInfoRepo.monthlyCropsList.collectAsStateWithLifecycle()

    AddCropsScreen(
        modifier = modifier,
        isLoading = viewModel.uiState == AddCropsUiState.Loading,
        editMode = viewModel.editMode,
        customMode = viewModel.customMode,
        cropsType = viewModel.cropsType,
        cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap,
        plantingDate = viewModel.plantingDateState.selectedDate,
        customNameState = viewModel.customNameState,
        nickNameState = viewModel.nickNameState,
        wateringIntervalState = viewModel.wateringIntervalState,
        growingDayState = viewModel.growingDayState,

        showSheet = viewModel.typeSheetState::show,
        showDatePicker = viewModel.plantingDateState::showPicker,
        onBack = onBack,
        onButton = { viewModel.onButton(onBack, onButton, onShowSnackBar) }
    )
    CropsTypeBottomSheet(
        showSheet = viewModel.typeSheetState.showSheet,
        scope = scope,
        monthlyCrops = monthlyCrops,
        totalCrops = viewModel.totalCrops,
        onItemClick = viewModel::onCropsType,
        onDismissRequest = viewModel.typeSheetState::dismiss
    )
    TutTutDatePickerDialog(
        showDialog = viewModel.plantingDateState.showDatePicker,
        plantingDate = viewModel.plantingDateState.selectedDate,
        onDateSelected = viewModel.plantingDateState::onDateSelected,
        onDismissRequest = viewModel.plantingDateState::dismissPicker
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddCropsScreen(
    modifier: Modifier,
    isLoading: Boolean,
    editMode: Boolean,
    customMode: Boolean,
    cropsType: String,
    cropsInfoMap: HashMap<String, CropsInfo>,
    plantingDate: String,
    customNameState: IEditTextState,
    nickNameState: IEditTextState,
    wateringIntervalState: WateringIntervalState,
    growingDayState: GrowingDayState,
    showSheet: () -> Unit,
    showDatePicker: () -> Unit,
    onBack: () -> Unit,
    onButton: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = if (editMode) "작물 ${stringResource(id = R.string.edit)}" else "작물 ${stringResource(id = R.string.add)}",
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
                    .clickable { if (!editMode) showSheet() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TutTutImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    url = cropsInfoMap[cropsType]?.imageUrl ?: CUSTOM_IMAGE
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = cropsInfoMap[cropsType]?.name ?: CUSTOM_NAME, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.planting_day))
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clickable { showDatePicker() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = getFormattedDate(plantingDate), style = MaterialTheme.typography.labelLarge)
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = "calendar-icon"
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            if (customMode) {
                TutTutLabel(title = stringResource(id = R.string.crops_name))
                TutTutTextField(
                    value = customNameState.typedText,
                    placeHolder = stringResource(id = R.string.crops_name_placeholder),
                    supportingText = stringResource(id = R.string.text_limit),
                    onValueChange = customNameState::typeText,
                    onResetValue = customNameState::resetText,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
            TutTutLabel(title = stringResource(id = R.string.nickname))
            TutTutTextField(
                value = nickNameState.typedText,
                placeHolder = stringResource(id = R.string.nickname_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = nickNameState::typeText,
                onResetValue = nickNameState::resetText,
                imeAction = if ((!customMode && wateringIntervalState.unUsed) || (customMode && wateringIntervalState.unUsed && growingDayState.unUsed)) ImeAction.Done else ImeAction.Next
            )
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.watering_interval))
            if (!wateringIntervalState.unUsed) {
                TutTutTextField(
                    value = wateringIntervalState.typedText,
                    placeHolder = stringResource(id = R.string.watering_interval),
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = wateringIntervalState::typeText,
                    onResetValue = wateringIntervalState::resetText,
                    imeAction = if (customMode && !growingDayState.unUsed) ImeAction.Next else ImeAction.Done
                )
            }
            AddCropsCheckBox(
                text = stringResource(id = R.string.unused),
                checked = wateringIntervalState.unUsed,
                onCheckedChange = wateringIntervalState::changeUsedState
            )
            Spacer(modifier = Modifier.height(40.dp))
            if (customMode) {
                TutTutLabel(title = stringResource(id = R.string.growing_day))
                if (!growingDayState.unUsed) {
                    TutTutTextField(
                        value = growingDayState.typedText,
                        placeHolder = stringResource(id = R.string.growing_day),
                        keyboardType = KeyboardType.Decimal,
                        onValueChange = growingDayState::typeText,
                        onResetValue = growingDayState::resetText
                    )
                }
                AddCropsCheckBox(
                    text = stringResource(id = R.string.unused),
                    checked = growingDayState.unUsed,
                    onCheckedChange = growingDayState::changeUsedState
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
/*            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TutTutLabel(title = stringResource(id = R.string.watering_alarm))
                TutTutSwitch(
                    checked = needAlarm,
                    onCheckedChange = onAlarmSwitch
                )
            }*/
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding()
                .padding(top = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = if (editMode) stringResource(id = R.string.edit) else stringResource(id = R.string.add),
                isLoading = isLoading,
                enabled = nickNameState.isValidate() && customNameState.isValidate() && wateringIntervalState.isValidate() && growingDayState.isValidate(),
                onClick = onButton
            )
        }
    }
}