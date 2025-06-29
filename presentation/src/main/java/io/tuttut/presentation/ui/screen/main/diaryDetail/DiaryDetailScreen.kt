package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.presentation.R
import io.tuttut.presentation.model.CommentUiModel
import io.tuttut.presentation.model.UserUiModel
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.CommentTextField
import io.tuttut.presentation.ui.component.DiaryPagerImage
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.ReportBottomSheet
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.state.ITextFieldState
import io.tuttut.presentation.util.clickableWithOutRipple
import io.tuttut.presentation.util.withScreenPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun DiaryDetailRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveEditDiary: () -> Unit,
    onBack: () -> Unit,
    viewModel: DiaryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    DiaryDetailScreen(
        modifier = modifier,
        uiState = uiState,
        commentState = viewModel.commentState,
        onSend = {
            keyboardController?.hide()
            viewModel.onSend()
        },
        onDeleteComment = { id ->
            focusManager.clearFocus()
            viewModel.onDeleteComment(id)
        },
        onEdit = moveEditDiary,
        onDelete = { viewModel.setDeleteSheetState(true) },
        onReport = { viewModel.setReportSheetState(true) },
        onBack = onBack
    )
    NegativeBottomSheet(
        showSheet = viewModel.showDeleteSheet,
        scope = scope,
        onButton = { viewModel.onDelete(onBack) },
        onDismissRequest = { viewModel.setDeleteSheetState(false) }
    )
    ReportBottomSheet(
        showSheet = viewModel.showReportSheet,
        scope = scope,
        onSelectReportReason = viewModel::onReport,
        onDismissRequest = { viewModel.setReportSheetState(false) }
    )
    BackHandler(onBack = onBack)
}

@Composable
private fun DiaryDetailScreen(
    modifier: Modifier,
    uiState: DiaryDetailUiState,
    commentState: ITextFieldState,
    onDeleteComment: (String) -> Unit,
    onSend: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit,
    onBack: () -> Unit,
) {
    when (uiState) {
        DiaryDetailUiState.Loading -> TutTutLoadingScreen()
        is DiaryDetailUiState.Success -> {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                TutTutTopBar(
                    title = "${uiState.diary.author.name}의 일지",
                    onBack = onBack
                ) {
                    MenuDropDownButton(
                        isMine = uiState.diary.isMine,
                        onEdit = onEdit,
                        onDelete = onDelete,
                        onReport = onReport
                    )
                }
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        DiaryPagerImage(imgUrlList = uiState.diary.imageUrlList)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(screenHorizontalPadding)
                        ) {
                            UserProfile(
                                user = uiState.diary.author,
                                created = uiState.diary.created
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = uiState.diary.content,
                                style = MaterialTheme.typography.displayLarge,
                                lineHeight = 24.sp,
                            )
                            Spacer(modifier = Modifier.height(68.dp))
                            Text(
                                text = uiState.diary.commentCnt,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    items(
                        items = uiState.commentList,
                        key = { it.id }
                    ) { comment ->
                        CommentItem(
                            comment = comment,
                            onDeleteComment = { onDeleteComment(comment.id) },
                            onReportComment = onReport
                        )
                    }
                }
                CommentArea(
                    user = uiState.currentUser,
                    commentState = commentState,
                    onSend = onSend
                )
            }
        }
    }
}

@Composable
internal fun CommentItem(
    modifier: Modifier = Modifier,
    comment: CommentUiModel,
    onReportComment: () -> Unit,
    onDeleteComment: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .withScreenPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            UserProfile(
                user = comment.author,
                created = comment.created
            )
            MenuDropDownButton(
                size = 14,
                isMine = comment.isMine,
                onDelete = onDeleteComment,
                onReport = onReportComment
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 52.dp)
        ) {
            Text(
                text = comment.content,
                style = MaterialTheme.typography.displayMedium,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
internal fun CommentArea(
    modifier: Modifier = Modifier,
    user: UserUiModel,
    commentState: ITextFieldState,
    onSend: () -> Unit,
) {
    val sendEnabled by remember { derivedStateOf { commentState.isValidate() } }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.inverseSurface
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(url = user.profile)
            Spacer(modifier = Modifier.width(8.dp))
            CommentTextField(
                modifier = Modifier.weight(1f),
                value = commentState.typedText,
                enabled = true,
                onValueChange = commentState::typeText
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickableWithOutRipple(
                        onClick = { if (sendEnabled) onSend() },
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                painter = painterResource(id = R.drawable.ic_send),
                tint = if (sendEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary,
                contentDescription = "ic-send"
            )
        }
    }
}

@Composable
internal fun ProfileImage(
    modifier: Modifier = Modifier,
    url: String,
) {
    TutTutImage(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        url = url
    )
}

@Composable
internal fun UserProfile(
    modifier: Modifier = Modifier,
    user: UserUiModel,
    created: String,
) {
    Row(
        modifier =  modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(url = user.profile)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = created,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}