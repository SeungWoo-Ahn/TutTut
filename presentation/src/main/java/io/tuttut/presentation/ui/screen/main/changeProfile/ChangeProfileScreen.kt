package io.tuttut.presentation.ui.screen.main.changeProfile

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.tuttut.domain.model.image.ImageSource
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.CameraCircle
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.state.ITextFieldState
import io.tuttut.presentation.util.clickableWithOutRipple
import io.tuttut.presentation.util.withScreenPadding

@Composable
fun ChangeProfileRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: ChangeProfileViewModel = hiltViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = viewModel::onPhotoPickerResult
    )
    val buttonEnabled by remember { derivedStateOf { viewModel.validate() } }

    ChangeProfileScreen(
        modifier = modifier,
        isLoading = viewModel.uiState == ChangeProfileUiState.Loading,
        buttonEnabled = buttonEnabled,
        profileImage = viewModel.profileImage,
        nameState = viewModel.nameState,
        onChangeImage = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onSubmit = { viewModel.onSubmit(onBack) },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun ChangeProfileScreen(
    modifier: Modifier,
    isLoading: Boolean,
    buttonEnabled: Boolean,
    profileImage: ImageSource?,
    nameState: ITextFieldState,
    onChangeImage: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        TutTutTopBar(
            title = stringResource(id = R.string.change_profile),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(screenHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profileImage?.let { imageSource ->
                ProfileWithAlbum(
                    profileImage = imageSource,
                    onChangeImage = { if (isLoading.not()) onChangeImage() }
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            TutTutLabel(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.profile_name)
            )
            TutTutTextField(
                state = nameState,
                enabled = isLoading.not(),
                placeHolder = stringResource(id = R.string.profile_name_placeholder),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .withScreenPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            TutTutButton(
                text = stringResource(id = R.string.change),
                isLoading = isLoading,
                enabled = buttonEnabled,
                onClick = onSubmit
            )
        }
    }
}

@Composable
internal fun ProfileWithAlbum(
    modifier: Modifier = Modifier,
    profileImage: ImageSource,
    onChangeImage: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(90.dp)
            .clickableWithOutRipple(
                onClick = onChangeImage,
                interactionSource = remember(::MutableInteractionSource)
            )
    ) {
        TutTutImage(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            url = when (profileImage) {
                is ImageSource.Local -> profileImage.file.absolutePath
                is ImageSource.Remote -> profileImage.url
            }
        )
        CameraCircle(modifier = Modifier.align(Alignment.BottomEnd))
    }
}