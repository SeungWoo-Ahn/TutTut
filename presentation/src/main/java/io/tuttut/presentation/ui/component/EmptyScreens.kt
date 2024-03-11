package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenVerticalPadding

@Composable
fun MonthlyCropsEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = screenVerticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.monthly_recommended_crops_empty),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}