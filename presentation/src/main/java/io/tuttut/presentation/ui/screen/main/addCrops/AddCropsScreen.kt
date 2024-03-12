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
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.CropsTypeBottomSheet
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutDatePickerDialog
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutSwitch
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
    val plantingDate by viewModel.plantingDate
    val typedNickName by viewModel.typedNickName
    val typedWateringInterval by viewModel.typedWateringInterval
    val needAlarm by viewModel.needAlarm

    AddCropsScreen(
        modifier = modifier,
        isEdit = viewModel.editMode,
        cropsType = cropsType,
        plantingDate = plantingDate,
        nickName = typedNickName,
        wateringInterval = typedWateringInterval,
        needAlarm = needAlarm,
        showSheet = { viewModel.showSheet = true },
        showDatePicker = { viewModel.showDatePicker = true },
        typeNickName = viewModel::typeNickName,
        typeWateringInterval = viewModel::typeWateringInterval,
        resetNickName = viewModel::resetNickName,
        resetWateringInterval = viewModel::resetWateringInterval,
        onAlarmSwitch = viewModel::onAlarmSwitch,
        onBack = onBack,
        onButton = onButton
    )
    CropsTypeBottomSheet(
        showSheet = viewModel.showSheet,
        monthlyCrops = emptyList(),
        totalCrops = emptyList(),
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
    cropsType: CropsInfo,
    plantingDate: String,
    nickName: String,
    wateringInterval: String,
    needAlarm: Boolean,
    showSheet: () -> Unit,
    showDatePicker: () -> Unit,
    typeNickName: (String) -> Unit,
    typeWateringInterval: (String) -> Unit,
    resetNickName: () -> Unit,
    resetWateringInterval: () -> Unit,
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
                    url = cropsType.imageUrl
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = cropsType.name, style = MaterialTheme.typography.labelLarge)
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
            TutTutLabel(title = stringResource(id = R.string.nickname))
            TutTutTextField(
                value = nickName,
                placeHolder = stringResource(id = R.string.nickname_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = typeNickName,
                onResetValue = resetNickName,
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.watering_interval))
            TutTutTextField(
                value = wateringInterval,
                placeHolder = stringResource(id = R.string.watering_interval),
                supportingText = stringResource(id = R.string.input_type_decimal),
                keyboardType = KeyboardType.Decimal,
                onValueChange = typeWateringInterval,
                onResetValue = resetWateringInterval
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
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
                enabled = nickName.isNotEmpty() && wateringInterval.isNotEmpty(),
                onClick = onButton
            )
        }
    }
}