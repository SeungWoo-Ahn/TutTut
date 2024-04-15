package io.tuttut.presentation.ui.screen.main.addDiary

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.presentation.R
import io.tuttut.presentation.util.withScreenPadding
import io.tuttut.presentation.ui.component.AddImageButton
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTextForm
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.XCircle

@RequiresApi(Build.VERSION_CODES.KITKAT)
@Composable
fun AddDiaryRoute(
    modifier: Modifier = Modifier,
    moveDiaryDetail: () -> Unit,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: AddDiaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageList by viewModel.imageList.collectAsStateWithLifecycle()
    val typedContent by viewModel.typedContent.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(3),
        onResult = viewModel::handleImages
    )
    AddDiaryScreen(
        modifier = modifier,
        uiState = uiState,
        editMode = viewModel.editMode,
        typedContent = typedContent,
        imageList = imageList,
        typeContent = viewModel::typeContent,
        addImage = { viewModel.addImages(launcher, onShowSnackBar) },
        deleteImage = viewModel::deleteImage,
        onButton = { viewModel.onButton(onBack, moveDiaryDetail, onShowSnackBar) },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun AddDiaryScreen(
    modifier: Modifier,
    uiState: AddDiaryUiState,
    editMode: Boolean,
    typedContent: String,
    imageList: List<StorageImage>,
    typeContent: (String) -> Unit,
    addImage: () -> Unit,
    deleteImage: (Int) -> Unit,
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
                    onClick = { if (!uiState.isLoading()) addImage() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items = imageList) { index, image ->
                        DiaryImageItem(
                            url = image.url,
                            isPrimitive = index == 0,
                            onDelete = { if (!uiState.isLoading()) deleteImage(index) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            TutTutTextForm(
                modifier = Modifier
                    .height(300.dp),
                value = typedContent,
                placeHolder = stringResource(id = R.string.diary_placeholder),
                enabled = !uiState.isLoading(),
                onValueChange = typeContent
            )
            Spacer(modifier = Modifier.weight(1f))
            TutTutButton(
                text = stringResource(id = R.string.write_complete),
                isLoading = uiState.isLoading(),
                enabled = typedContent.trim().isNotEmpty(),
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