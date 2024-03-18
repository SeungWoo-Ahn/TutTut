package io.tuttut.presentation.ui.screen.main.addDiary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.tuttut.data.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.withScreenPadding
import io.tuttut.presentation.ui.component.AddImageButton
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTextForm
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.XCircle

@Composable
fun AddDiaryRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    AddDiaryScreen(
        modifier = modifier,
        editMode = false,
        typedContent = "",
        imageList = listOf(DEFAULT_MAIN_IMAGE),
        typeContent = {},
        addImage = {},
        deleteImage = {},
        onButton = {},
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddDiaryScreen(
    modifier: Modifier,
    editMode: Boolean,
    typedContent: String,
    imageList: List<String>,
    typeContent: (String) -> Unit,
    addImage: () -> Unit,
    deleteImage: () -> Unit,
    onButton: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = if (editMode) stringResource(id = R.string.edit_diary)
                    else stringResource(id = R.string.write_diary),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .withScreenPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                AddImageButton(
                    count = imageList.size,
                    total = 3,
                    onClick = addImage
                )
                Spacer(modifier = Modifier.width(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items = imageList) { index, url ->
                        DiaryImageItem(
                            url = url,
                            isPrimitive = index == 0,
                            onDelete = deleteImage
                        )
                    }
                }
            }
            TutTutTextForm(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 30.dp),
                value = typedContent,
                placeHolder = stringResource(id = R.string.diary_placeholder),
                onValueChange = typeContent
            )
            TutTutButton(
                text = stringResource(id = R.string.write_complete),
                isLoading = false,
                onClick = onButton
            )
        }

    }
}

@Composable
fun DiaryImageItem(
    url: String,
    isPrimitive: Boolean,
    onDelete: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(78.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomStart)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            TutTutImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(MaterialTheme.shapes.medium),
                url = url
            )
            if (isPrimitive) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(vertical = 4.dp)
                        .align(Alignment.BottomCenter),
                    text = stringResource(id = R.string.primitive_photo),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        XCircle(
            modifier = Modifier.align(Alignment.TopEnd),
            size = 16,
            onClick = onDelete
        )
    }
}