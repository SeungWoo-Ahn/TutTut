package io.tuttut.presentation.ui.screen.main.addDiary

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.presentation.R
import io.tuttut.presentation.ui.component.AddImageButton
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTextForm
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.XCircle
import io.tuttut.presentation.ui.state.ITextFieldState
import io.tuttut.presentation.util.withScreenPadding

@Composable
fun AddDiaryRoute(
    modifier: Modifier = Modifier,
    moveDiaryDetail: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: AddDiaryViewModel = hiltViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(3),
        onResult = viewModel::onPhotoPickerResult
    )
    val buttonEnabled by remember { derivedStateOf { viewModel.validate() } }

    AddDiaryScreen(
        modifier = modifier,
        editMode = viewModel.editMode,
        isLoading = viewModel.uiState == AddDiaryUiState.Loading,
        buttonEnabled = buttonEnabled,
        contentState = viewModel.contentState,
        imageList = viewModel.imageList,
        addImage = {
            if (viewModel.imageList.size < 3) {
                launcher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        },
        deleteImage = viewModel::deleteImage,
        onButton = { viewModel.onButton(moveDiaryDetail) },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun AddDiaryScreen(
    modifier: Modifier,
    editMode: Boolean,
    isLoading: Boolean,
    buttonEnabled: Boolean,
    contentState: ITextFieldState,
    imageList: List<ImageSource>,
    addImage: () -> Unit,
    deleteImage: (ImageSource) -> Unit,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                AddImageButton(
                    count = imageList.size,
                    total = 3,
                    onClick = { if (isLoading.not()) addImage() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items = imageList) { index, image ->
                        DiaryImageItem(
                            image = image,
                            isPrimitive = index == 0,
                            onDelete = { if (isLoading.not()) deleteImage(image) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            TutTutTextForm(
                modifier = Modifier
                    .height(300.dp),
                state = contentState,
                placeHolder = stringResource(id = R.string.diary_placeholder),
                enabled = isLoading.not(),
            )
            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.write_complete),
                isLoading = isLoading,
                enabled = buttonEnabled,
                onClick = onButton
            )
        }
    }
}

@Composable
fun DiaryImageItem(
    image: ImageSource,
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
                url = when (image) {
                    is ImageSource.Local -> image.file.absolutePath
                    is ImageSource.Remote -> image.url
                }
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