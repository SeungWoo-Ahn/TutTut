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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.tuttut.data.constant.DEFAULT_MAIN_IMAGE
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.dto.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.DeleteBottomSheet
import io.tuttut.presentation.ui.component.MenuDropDownButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.getRelativeTime
import kotlinx.coroutines.CoroutineScope

@Composable
fun DiaryListRoute(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    onBack: () -> Unit,
    moveDetail: () -> Unit,
) {
    DiaryListScreen(
        modifier = modifier,
        cropsName = "소중한 감자",
        onBack = onBack,
        onDiary = moveDetail
    )
    DeleteBottomSheet(
        showSheet = false,
        scope = scope,
        onDelete = { },
        onDismissRequest = { }
    )
    BackHandler(onBack = onBack)
}

@Composable
internal fun DiaryListScreen(
    modifier: Modifier,
    cropsName: String,
    onBack: () -> Unit,
    onDiary: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TutTutTopBar(
            title = "$cropsName ${stringResource(id = R.string.diary_name)}",
            onBack = onBack
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenHorizontalPadding),
            state = rememberLazyListState()
        ) {

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
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = diary.content,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 16.sp,
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