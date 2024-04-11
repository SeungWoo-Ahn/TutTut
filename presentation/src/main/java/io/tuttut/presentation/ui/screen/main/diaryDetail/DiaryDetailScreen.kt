package io.tuttut.presentation.ui.screen.main.diaryDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.data.model.dto.Comment
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.StorageImage
import io.tuttut.data.model.dto.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.CommentTextField
import io.tuttut.presentation.ui.component.DiaryPagerImage
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getCurrentDateTime
import io.tuttut.presentation.util.getRelativeTime

@Composable
fun DiaryDetailRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: DiaryDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val typedComment by viewModel.typedComment.collectAsStateWithLifecycle()

    when (uiState) {
        DiaryDetailUiState.Loading -> TutTutLoadingScreen()
        is DiaryDetailUiState.Success -> {
            DiaryDetailScreen(
                modifier = modifier,
                user = viewModel.currentUser,
                typedComment = typedComment,
                diary = (uiState as DiaryDetailUiState.Success).diary,
                commentList = listOf(
                    Comment(
                        id = "1",
                        authorId = "tQUjImvxvbfQSfguwAwMLIIUBE22",
                        content = "정말 좋네요",
                        created = getCurrentDateTime()
                    ),
                    Comment(
                        id = "2",
                        authorId = "tQUjImvxvbfQSfguwAwMLIIUBE22",
                        content = "좋습니다",
                        created = getCurrentDateTime()
                    )
                ),
                memberMap = viewModel.memberMap,
                typeComment = viewModel::typeComment,
                onSend = viewModel::onSend,
                onEdit = viewModel::onEdit,
                onDelete = viewModel::onDelete,
                onReport = viewModel::onReport,
                onEditComment = viewModel::onEditComment,
                onDeleteComment = viewModel::onDeleteComment,
                onBack = onBack
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
    commentList: List<Comment>,
    memberMap: HashMap<String, User>,
    typeComment: (String) -> Unit,
    onSend: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit,
    onEditComment: () -> Unit,
    onDeleteComment: () -> Unit,
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
                isMine = diary.authorId == user.id,
                onEdit = onEdit,
                onDelete = onDelete,
                onReport = onReport
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                DiaryPagerImage(imgUrlList = diary.imgUrlList.ifEmpty { listOf(StorageImage(DEFAULT_MAIN_IMAGE)) })
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
                        lineHeight = 18.sp,
                    )
                    Spacer(modifier = Modifier.height(68.dp))
                    Text(
                        text = "${stringResource(id = R.string.comment)}${commentList.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            items(
                items = commentList,
                key = { it.id },
            ) {
                CommentItem(
                    userId = user.id,
                    comment = it,
                    memberMap = memberMap,
                    onEditComment = onEditComment,
                    onDeleteComment = onDeleteComment,
                )
            }
        }
        CommentArea(
            typedComment = typedComment,
            user = user,
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
    onEditComment: () -> Unit,
    onDeleteComment: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = screenHorizontalPadding,
                end = screenHorizontalPadding,
                bottom = 30.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            UserProfile(
                user = memberMap[comment.authorId] ?: User(),
                created = comment.created
            )
            MenuDropDownButton(
                size = 14,
                isMine = comment.authorId == userId,
                onEdit = onEditComment,
                onDelete = onDeleteComment
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
                    horizontal = screenHorizontalPadding,
                    vertical = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(url = user.profile.url)
            Spacer(modifier = Modifier.width(8.dp))
            CommentTextField(
                modifier = Modifier.weight(1f),
                value = typedComment,
                onValueChange = typeComment
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSend() },
                painter = painterResource(id = R.drawable.ic_send),
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
    user: User,
    created: String,
) {
    Row(modifier) {
        ProfileImage(url = user.profile.url)
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.height(40.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 14.sp
            )
            Text(
                text = getRelativeTime(created),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}