package io.tuttut.presentation.ui.screen.main.changeProfile

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.CameraCircle
import io.tuttut.presentation.ui.component.TutTutButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutTextField
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.clickableWithOutRipple
import io.tuttut.presentation.util.withScreenPadding

@RequiresApi(Build.VERSION_CODES.KITKAT)
@Composable
fun ChangeProfileRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: ChangeProfileViewModel = hiltViewModel()
) {
    val profileImage by viewModel.profileImage.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = viewModel::handleImage
    )
    ChangeProfileScreen(
        modifier = modifier,
        uiState = viewModel.uiState,
        profile = profileImage,
        typedName = viewModel.nameState.typedText,
        typeName = viewModel.nameState::typeText,
        resetName = viewModel.nameState::resetText,
        onChangeImage = { viewModel.onChangeImage(launcher) },
        onSubmit = { viewModel.onSubmit(onBack, onShowSnackBar) },
        onBack = onBack
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun ChangeProfileScreen(
    modifier: Modifier,
    uiState: ChangeProfileUiState,
    profile: StorageImage,
    typedName: String,
    typeName: (String) -> Unit,
    resetName: () -> Unit,
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
            ProfileWithAlbum(
                profile = profile,
                onChangeImage = { if (!uiState.isLoading()) onChangeImage() }
            )
            Spacer(modifier = Modifier.height(36.dp))
            TutTutLabel(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.profile_name)
            )
            TutTutTextField(
                value = typedName,
                enabled = !uiState.isLoading(),
                placeHolder = stringResource(id = R.string.profile_name_placeholder),
                supportingText = stringResource(id = R.string.text_limit),
                onValueChange = typeName,
                onResetValue = resetName
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
                isLoading = uiState.isLoading(),
                enabled = typedName.trim().length in 1 .. 10,
                onClick = onSubmit
            )
        }
    }
}

@Composable
internal fun ProfileWithAlbum(
    modifier: Modifier = Modifier,
    profile: StorageImage,
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
            url = profile.url
        )
        CameraCircle(modifier = Modifier.align(Alignment.BottomEnd))
    }
}