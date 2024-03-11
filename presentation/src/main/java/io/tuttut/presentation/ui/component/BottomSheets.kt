package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutTutBottomSheet(
    showSheet: Boolean,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    windowInsets: WindowInsets = WindowInsets(top = 240.dp),
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            sheetState = sheetState,
            windowInsets = windowInsets,
            scrimColor = Color.Transparent,
            dragHandle = null,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropsTypeBottomSheet(
    showSheet: Boolean,
    monthlyCrops: List<CropsInfo>,
    totalCrops: List<CropsInfo>,
    onItemClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    TutTutBottomSheet(
        showSheet = showSheet,
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
                    .clickable { onDismissRequest() },
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
            onItemClick = onItemClick
        )
    }
}