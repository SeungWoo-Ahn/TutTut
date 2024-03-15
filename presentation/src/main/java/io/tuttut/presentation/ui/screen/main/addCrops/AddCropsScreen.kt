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
import io.tuttut.data.model.dto.CUSTOM_IMAGE
import io.tuttut.data.model.dto.CUSTOM_NAME
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.CropsTypeBottomSheet
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutCheckBox
import io.tuttut.presentation.ui.component.TutTutDatePickerDialog
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getFormattedDate

@Composable
fun AddCropsRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onButton: () -> Unit,
    viewModel: AddCropsViewModel = hiltViewModel()
) {
    val cropsType by viewModel.cropsType
    val customMode by viewModel.customMode
    val plantingDate by viewModel.plantingDate
    val typedCustomName by viewModel.typedCustomName
    val typedNickName by viewModel.typedNickName
    val typedWateringInterval by viewModel.typedWateringInterval
    val typedGrowingDay by viewModel.typedGrowingDay
    val offWateringInterval by viewModel.offWateringInterval
    val offGrowingDay by viewModel.offGrowingDay
    val needAlarm by viewModel.needAlarm
    val monthlyCrops by viewModel.cropsInfoRepo.monthlyCropsList.collectAsStateWithLifecycle()
    val cropsInfoMap = viewModel.cropsInfoRepo.cropsInfoMap

    AddCropsScreen(
        modifier = modifier,
        isEdit = viewModel.editMode,
        customMode = customMode,
        cropsType = cropsType,
        cropsInfoMap = cropsInfoMap,
        plantingDate = plantingDate,
        customName = typedCustomName,
        nickName = typedNickName,
        wateringInterval = typedWateringInterval,
        growingDay = typedGrowingDay,
        offWateringInterval = offWateringInterval,
        offGrowingDay = offGrowingDay,
        needAlarm = needAlarm,
        showSheet = { viewModel.showSheet = true },
        showDatePicker = { viewModel.showDatePicker = true },
        typeCustomName = viewModel::typeCustomName,
        typeNickName = viewModel::typeNickName,
        typeWateringInterval = viewModel::typeWateringInterval,
        typeGrowingDay = viewModel::typeGrowingDay,
        resetCustomName = viewModel::resetCustomName,
        resetNickName = viewModel::resetNickName,
        resetWateringInterval = viewModel::resetWateringInterval,
        resetGrowingDay = viewModel::resetGrowingDay,
        onOffWateringIntervalChanged = viewModel::onOffWateringIntervalChanged,
        onOffGrowingDayChanged = viewModel::onOffGrowingDayChanged,
        onAlarmSwitch = viewModel::onAlarmSwitch,
        onBack = onBack,
        onButton = onButton
    )
    CropsTypeBottomSheet(
        showSheet = viewModel.showSheet,
        monthlyCrops = monthlyCrops,
        totalCrops = viewModel.totalCrops,
        onItemClick = viewModel::onCropsType,
        onDismissRequest = { viewModel.showSheet = false }
    )
    TutTutDatePickerDialog(
        showDialog = viewModel.showDatePicker,
        onDateSelected = viewModel::onDateSelected,
        onDismissRequest = { viewModel.showDatePicker = false }
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddCropsScreen(
    modifier: Modifier,
    isEdit: Boolean,
    customMode: Boolean,
    cropsType: String,
    cropsInfoMap: HashMap<String, CropsInfo>,
    plantingDate: String,
    customName: String,
    nickName: String,
    wateringInterval: String,
    offWateringInterval: Boolean,
    growingDay: String,
    offGrowingDay: Boolean,
    needAlarm: Boolean,
    showSheet: () -> Unit,
    showDatePicker: () -> Unit,
    typeCustomName: (String) -> Unit,
    typeNickName: (String) -> Unit,
    typeWateringInterval: (String) -> Unit,
    typeGrowingDay: (String) -> Unit,
    resetCustomName: () -> Unit,
    resetNickName: () -> Unit,
    resetWateringInterval: () -> Unit,
    resetGrowingDay: () -> Unit,
    onOffWateringIntervalChanged: (Boolean) -> Unit,
    onOffGrowingDayChanged: (Boolean) -> Unit,
    onAlarmSwitch: (Boolean) -> Unit,
    onBack: () -> Unit,
    onButton: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = if (isEdit) "작물 ${stringResource(id = R.string.edit)}" else "작물 ${stringResource(id = R.string.add)}",
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
                    .clickable { showSheet() },
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
                    value = customName,
                    placeHolder = stringResource(id = R.string.crops_name_placeholder),
                    supportingText = stringResource(id = R.string.text_limit),
                    onValueChange = typeCustomName,
                    onResetValue = resetCustomName,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(40.dp))
            }
            TutTutLabel(title = stringResource(id = R.string.nickname))
            TutTutTextField(
                value = nickName,
                placeHolder = stringResource(id = R.string.nickname_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = typeNickName,
                onResetValue = resetNickName,
                imeAction = if ((!customMode && offWateringInterval) || (customMode && offWateringInterval && offGrowingDay)) ImeAction.Done else ImeAction.Next
            )
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.watering_interval))
            if (!offWateringInterval) {
                TutTutTextField(
                    value = wateringInterval,
                    placeHolder = stringResource(id = R.string.watering_interval),
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = typeWateringInterval,
                    onResetValue = resetWateringInterval,
                    imeAction = if (customMode && !offGrowingDay) ImeAction.Next else ImeAction.Done
                )
            }
            TutTutCheckBox(
                text = stringResource(id = R.string.unused),
                checked = offWateringInterval,
                onCheckedChange = onOffWateringIntervalChanged
            )
            Spacer(modifier = Modifier.height(40.dp))
            if (customMode) {
                TutTutLabel(title = stringResource(id = R.string.growing_day))
                if (!offGrowingDay) {
                    TutTutTextField(
                        value = growingDay,
                        placeHolder = stringResource(id = R.string.growing_day),
                        keyboardType = KeyboardType.Decimal,
                        onValueChange = typeGrowingDay,
                        onResetValue = resetGrowingDay
                    )
                }
                TutTutCheckBox(
                    text = stringResource(id = R.string.unused),
                    checked = offGrowingDay,
                    onCheckedChange = onOffGrowingDayChanged
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
        val buttonEnabled = if (!customMode) {
            if (offWateringInterval) {
                nickName.isNotEmpty()
            } else {
                nickName.isNotEmpty() && wateringInterval.isNotEmpty()
            }
        } else {
            if (offWateringInterval && offGrowingDay) {
                customName.isNotEmpty() && nickName.isNotEmpty()
            } else if (offWateringInterval) {
                customName.isNotEmpty() && nickName.isNotEmpty() && growingDay.isNotEmpty()
            } else if (offGrowingDay) {
                customName.isNotEmpty() && nickName.isNotEmpty() && wateringInterval.isNotEmpty()
            } else {
                customName.isNotEmpty() && nickName.isNotEmpty() && wateringInterval.isNotEmpty() && growingDay.isNotEmpty()
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
                text = if (isEdit) stringResource(id = R.string.edit) else stringResource(id = R.string.add),
                isLoading = false,
                enabled = buttonEnabled,
                onClick = onButton
            )
        }
    }
}