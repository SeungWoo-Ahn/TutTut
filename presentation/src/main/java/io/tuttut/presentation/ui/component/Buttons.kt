package io.tuttut.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.buttonHeight
import io.tuttut.presentation.theme.screenHorizontalPadding

@Composable
fun GoogleLoginButton(isLoading: Boolean, onLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(start = 14.dp)
            .clickable { if (!isLoading) onLogin() },
    ) {
        Image(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.logo_google),
            contentDescription = "google-logo",
        )
        if (!isLoading) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.google_login),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            TutTutLoading(
                modifier = Modifier.align(Alignment.Center),
                size = 18,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TutTutButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    enabled: Boolean = true,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight),
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonColors(
            containerColor = buttonColor,
            contentColor = contentColor,
            disabledContainerColor = if (isLoading) buttonColor else MaterialTheme.colorScheme.inversePrimary,
            disabledContentColor = if (isLoading) contentColor else MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick
    ) {
        if (!isLoading) {
            Text(
                text = text,
                color = if (enabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            TutTutLoading(size = 18)
        }
    }
}

@Composable
fun DatePickerButton(onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(screenHorizontalPadding)
            .clickable(onClick = onClick),
        text = stringResource(id = R.string.confirm)
    )
}

@Composable
fun HarvestButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(72.dp)
            .height(36.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.large
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.harvest),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun WateringButton(isWatered: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (isWatered) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = R.drawable.ic_water),
            tint = if (isWatered) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.tertiary,
            contentDescription = "ic-water"
        )
    }
}