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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.tuttut.data.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.dto.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.CommentTextField
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.DiaryPagerImage
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.ReportBottomSheet
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoading
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.ui.component.loading
import io.tuttut.presentation.util.clickableWithOutRipple
import io.tuttut.presentation.util.getRelativeTime
import io.tuttut.presentation.util.withScreenPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun DiaryDetailRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveEditDiary: () -> Unit,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: DiaryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val commentUiState by viewModel.commentUiState.collectAsStateWithLifecycle()
    val comments = viewModel.comments.collectAsLazyPagingItems()
    val typedComment by viewModel.typedComment.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    when (uiState) {
        DiaryDetailUiState.Loading -> TutTutLoadingScreen()
        is DiaryDetailUiState.Success -> {
            val diary = (uiState as DiaryDetailUiState.Success).diary
            DiaryDetailScreen(
                modifier = modifier,
                user = viewModel.currentUser,
                typedComment = typedComment,
                diary = diary,
                commentUiState = commentUiState,
                comments = comments,
                memberMap = viewModel.memberMap,
                typeComment = viewModel::typeComment,
                onSend = { viewModel.onSend(onShowSnackBar, { keyboardController?.hide() }, { comments.refresh() }) },
                onEdit = { viewModel.onEdit(diary, moveEditDiary) },
                onDelete = { viewModel.showDeleteSheet = true },
                onReport = { viewModel.showReportSheet = true },
                onDeleteComment = { viewModel.onDeleteComment(it, onShowSnackBar) { comments.refresh() } },
                onBack = onBack
            )
            NegativeBottomSheet(
                showSheet = viewModel.showDeleteSheet,
                scope = scope,
                onButton = { viewModel.onDelete(diary, onBack, onShowSnackBar) },
                onDismissRequest = { viewModel.showDeleteSheet = false }
            )
            ReportBottomSheet(
                showSheet = viewModel.showReportSheet,
                scope = scope,
                onSelectReportReason = { viewModel.onReport(it, onShowSnackBar) },
                onDismissRequest = { viewModel.showReportSheet = false }
            )
        }
    }
    BackHandler(onBack = onBack)
}

@Composable
internal fun DiaryDetailScreen(
    modifier: Modifier,
    typedComment: String,
    diary: Diary,
    user: User,
    commentUiState: CommentUiState,
    comments: LazyPagingItems<Comment>,
    memberMap: HashMap<String, User>,
    typeComment: (String) -> Unit,
    onDeleteComment: (String) -> Unit,
    onSend: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = "${memberMap[diary.authorId]?.name ?: stringResource(id = R.string.unknown_user)}의 일지",
            onBack = onBack
        ) {
            MenuDropDownButton(
                isMine = diary.authorId == user.id || memberMap[diary.authorId] == null,
                onEdit = onEdit,
                onDelete = onDelete,
                onReport = onReport
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                DiaryPagerImage(
                    imgUrlList = diary.imgUrlList
                        .ifEmpty { listOf(StorageImage(DEFAULT_MAIN_IMAGE)) }
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(screenHorizontalPadding)
                ) {
                    UserProfile(
                        user = memberMap[diary.authorId] ?: User(name = stringResource(id = R.string.unknown_user)),
                        created = diary.created
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = diary.content,
                        style = MaterialTheme.typography.displayLarge,
                        lineHeight = 24.sp,
                    )
                    Spacer(modifier = Modifier.height(68.dp))
                    Text(
                        text = "${stringResource(id = R.string.comment)}${diary.commentCnt}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            when (comments.loadState.refresh) {
                LoadState.Loading -> loading(600)
                else -> {
                    items(
                        count = comments.itemCount,
                        key = comments.itemKey { it.id }
                    ) { index ->
                        comments[index]?.let { comment ->
                            CommentItem(
                                userId = user.id,
                                comment = comment,
                                memberMap = memberMap,
                                onDeleteComment = { onDeleteComment(comment.id) },
                                onReportComment = onReport
                            )
                        }
                    }
                }
            }
        }
        CommentArea(
            typedComment = typedComment,
            user = user,
            commentUiState = commentUiState,
            typeComment = typeComment,
            onSend = onSend
        )
    }
}

@Composable
internal fun CommentItem(
    modifier: Modifier = Modifier,
    userId: String,
    comment: Comment,
    memberMap: HashMap<String, User>,
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
                user = memberMap[comment.authorId] ?: User(name = stringResource(id = R.string.unknown_user)),
                created = comment.created
            )
            MenuDropDownButton(
                size = 14,
                isMine = comment.authorId == userId || memberMap[comment.authorId] == null,
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
    typedComment: String,
    commentUiState: CommentUiState,
    user: User,
    typeComment: (String) -> Unit,
    onSend: () -> Unit,
) {
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
            ProfileImage(url = user.profile.url)
            Spacer(modifier = Modifier.width(8.dp))
            CommentTextField(
                modifier = Modifier.weight(1f),
                value = typedComment,
                enabled = commentUiState == CommentUiState.Nothing,
                onValueChange = typeComment
            )
            Spacer(modifier = Modifier.width(6.dp))
            when (commentUiState) {
                CommentUiState.Loading -> TutTutLoading(size = 24)
                CommentUiState.Nothing -> {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickableWithOutRipple(
                                onClick = onSend,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        painter = painterResource(id = R.drawable.ic_send),
                        tint = if (typedComment.isEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
                        contentDescription = "ic-send"
                    )
                }
            }
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
    user: User,
    created: String,
) {
    Row(
        modifier =  modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(url = user.profile.url)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = getRelativeTime(created),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}