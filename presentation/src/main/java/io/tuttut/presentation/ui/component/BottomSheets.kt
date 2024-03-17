package io.tuttut.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    windowInsets: WindowInsets = WindowInsets(top = 100.dp),
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
    onItemClick: (CropsInfo) -> Unit,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBottomSheet(
    showSheet: Boolean,
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    TutTutBottomSheet(
        showSheet = showSheet,
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
                text = stringResource(id = R.string.delete_warning),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            TutTutButton(
                text = stringResource(id = R.string.delete),
                isLoading = false,
                buttonColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.error,
                onClick = onDelete
            )
            Spacer(modifier = Modifier.height(10.dp))
            TutTutButton(
                text = stringResource(id = R.string.close),
                isLoading = false,
                buttonColor = MaterialTheme.colorScheme.inverseSurface,
                onClick = onDismissRequest
            )
        }
    }
}