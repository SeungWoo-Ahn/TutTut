package io.tuttut.presentation.ui.screen.main.addCrops

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
import io.tuttut.presentation.ui.screen.main.selectCrops.cropsInfoList

var showSheet by mutableStateOf(false)
fun show() {
    showSheet = true
}

@Composable
fun AddCropsRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onButton: () -> Unit
) {

    AddCropsScreen(
        modifier = modifier,
        isEdit = false,
        show = { show() },
        onBack = onBack,
        onButton = onButton
    )
    CropsTypeBottomSheet(
        showSheet = showSheet,
        monthlyCrops = cropsInfoList,
        totalCrops = cropsInfoList,
        onItemClick = { },
        onDismissRequest = { showSheet = false }
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddCropsScreen(
    modifier: Modifier,
    isEdit: Boolean,
    show: () -> Unit,
    onBack: () -> Unit,
    onButton: () -> Unit
) {
    val cropsInfo = cropsInfoList[0]
    var showDatePicker by remember { mutableStateOf(false) }
    TutTutDatePickerDialog(
        showDialog = showDatePicker,
        onDateSelected = {
            Log.d("날짜 선택", "AddCropsScreen: $it")
        },
        onDismissRequest = { showDatePicker = false }
    )
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
                    .clickable { show() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TutTutImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    url = cropsInfo.imageUrl
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = cropsInfo.name, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.planting_day))
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "2024년 03월 11일", style = MaterialTheme.typography.labelLarge)
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = "calendar-icon"
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.nickname))
            TutTutTextField(
                value = "",
                placeHolder = stringResource(id = R.string.nickname_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = {},
                onResetValue = {}
            )
            Spacer(modifier = Modifier.height(40.dp))
            TutTutLabel(title = stringResource(id = R.string.watering_interval))
            TutTutTextField(
                value = "3 일",
                placeHolder = stringResource(id = R.string.watering_interval),
                supportingText = stringResource(id = R.string.input_type_decimal),
                keyboardType = KeyboardType.Decimal,
                onValueChange = {},
                onResetValue = {}
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
                TutTutSwitch()
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
                onClick = onButton
            )
        }
    }
}