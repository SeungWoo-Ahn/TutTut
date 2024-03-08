package io.tuttut.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding

@Composable
fun TutTutTopBar(
    title: String,
    needBack: Boolean = true,
    onBack: (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(
                start = if (needBack) 14.dp else screenHorizontalPadding,
                end = screenHorizontalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (needBack) {
            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onBack?.invoke() },
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back-icon"
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon()
        }
    }
}

@Preview
@Composable
fun PreviewTutTutTopBar() {
    TutTutTopBar(title = "텃텃 시작하기")
}