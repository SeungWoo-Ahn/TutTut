package io.tuttut.presentation.ui.screen.main.diaryList

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.network.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.data.network.model.Diary
import io.tuttut.data.network.model.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.NegativeBottomSheet
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.NoResults
import io.tuttut.presentation.ui.component.ReportBottomSheet
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getRelativeTime
import kotlinx.coroutines.CoroutineScope

@Composable
fun DiaryListRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    moveDiary: () -> Unit,
    moveEditDiary: () -> Unit,
    onBack: () -> Unit,
    onShowSnackBar: suspend (String, String?) -> Boolean,
    viewModel: DiaryListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DiaryListScreen(
        modifier = modifier,
        uiState = uiState,
        cropsName = viewModel.crops.nickName,
        userId = viewModel.pref.userId,
        memberMap = viewModel.memberMap,
        onDiary = { viewModel.onDiary(it, moveDiary) },
        onEdit = { viewModel.onEdit(it, moveEditDiary) },
        onDelete = viewModel::showDeleteDialog,
        onReport = { viewModel.showReportSheet = true },
        onBack = onBack,
    )
    NegativeBottomSheet(
        showSheet = viewModel.showDeleteSheet,
        scope = scope,
        onButton = { viewModel.onDelete(onShowSnackBar) },
        onDismissRequest = { viewModel.showDeleteSheet = false }
    )
    ReportBottomSheet(
        showSheet = viewModel.showReportSheet,
        scope = scope,
        onSelectReportReason = { viewModel.onReport(it, onShowSnackBar) },
        onDismissRequest = { viewModel.showReportSheet = false }
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun DiaryListScreen(
    modifier: Modifier,
    uiState: DiaryListUiState,
    cropsName: String,
    userId: String,
    memberMap: HashMap<String, User>,
    onDiary: (Diary) -> Unit,
    onEdit: (Diary) -> Unit,
    onDelete: (Diary) -> Unit,
    onReport: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = "$cropsName ${stringResource(id = R.string.diary_name)}",
            onBack = onBack
        )
        when (uiState) {
            DiaryListUiState.Loading -> TutTutLoadingScreen()
            is DiaryListUiState.Success -> {
                if (uiState.diaryList.isEmpty()) {
                    NoResults(
                        modifier = Modifier.weight(1f),
                        announce = stringResource(id = R.string.diary_empty)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = screenHorizontalPadding),
                    ) {
                        items(
                            items = uiState.diaryList,
                            key = { it.id }
                        ) { diary ->
                            DiaryItem(
                                isMine = diary.authorId == userId || memberMap[diary.authorId] == null,
                                diary = diary,
                                memberMap = memberMap,
                                onEdit = { onEdit(diary) },
                                onDelete = { onDelete(diary) },
                                onReport = onReport,
                                onClick = { onDiary(diary) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiaryItem(
    modifier: Modifier = Modifier,
    isMine: Boolean,
    diary: Diary,
    memberMap: HashMap<String, User>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReport: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TutTutImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                url = if (diary.imgUrlList.isNotEmpty()) diary.imgUrlList[0].url else DEFAULT_MAIN_IMAGE
            )
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = diary.content,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        MenuDropDownButton(
                            size = 14,
                            isMine = isMine,
                            onEdit = onEdit,
                            onDelete = onDelete,
                            onReport = onReport
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${memberMap[diary.authorId]?.name ?: stringResource(id = R.string.unknown_user)} · ${getRelativeTime(diary.created)}",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_comment),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "ic-comment"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${diary.commentCnt}",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
    }
}