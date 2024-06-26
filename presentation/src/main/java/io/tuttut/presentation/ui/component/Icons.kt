package io.tuttut.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.tuttut.presentation.R

@Composable
fun XCircle(
    modifier: Modifier = Modifier,
    size: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size((size / 4 * 3).dp),
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "x-icon",
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun CameraCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(30.dp)
            .background(
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "ic-camera"
        )
    }
}