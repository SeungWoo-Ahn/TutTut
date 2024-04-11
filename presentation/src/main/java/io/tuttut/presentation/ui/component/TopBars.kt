package io.tuttut.presentation.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.util.clickableWithOutRipple

@Composable
fun TutTutTopBar(
    modifier: Modifier = Modifier,
    title: String,
    needBack: Boolean = true,
    onBack: (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(
                start = if (needBack) 14.dp else screenHorizontalPadding,
                end = if (trailingIcon != null) 18.dp else screenHorizontalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (needBack) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clickableWithOutRipple(
                        onClick = { onBack?.invoke() },
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back-icon"
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(text = title, style = MaterialTheme.typography.headlineLarge)
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon()
        }
    }
}