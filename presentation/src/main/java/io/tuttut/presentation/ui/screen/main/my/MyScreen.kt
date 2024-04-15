package io.tuttut.presentation.ui.screen.main.my

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.tuttut.data.constant.PERSONAL_INFO_POLICY_URL
import io.tuttut.data.constant.SERVICE_POLICY_URL
import io.tuttut.data.model.dto.Garden
import io.tuttut.data.model.dto.User
import io.tuttut.presentation.R
import io.tuttut.presentation.theme.screenHorizontalPadding
import io.tuttut.presentation.ui.component.ChangeInfoButton
import io.tuttut.presentation.ui.component.TextButton
import io.tuttut.presentation.ui.component.TutTutImage
import io.tuttut.presentation.ui.component.TutTutLabel
import io.tuttut.presentation.ui.component.TutTutLoadingScreen
import io.tuttut.presentation.ui.component.TutTutTopBar
import io.tuttut.presentation.util.clickableWithOutRipple

@Composable
fun MyRoute(
    modifier: Modifier = Modifier,
    moveSetting: () -> Unit,
    moveChangeProfile: () -> Unit,
    moveChangeGarden: () -> Unit,
    onBack: () -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val memberList by viewModel.memberList.collectAsStateWithLifecycle()
    when (uiState) {
        MyUiState.Loading -> TutTutLoadingScreen()
        is MyUiState.Success -> {
            val data = uiState as MyUiState.Success
            val context = LocalContext.current
            MyScreen(
                modifier = modifier,
                profile = data.user,
                garden = data.garden,
                memberList = memberList,
                shareGarden = { viewModel.shareGarden(context, it) },
                openBrowser = { viewModel.openBrowser(context, it) },
                moveSetting = moveSetting,
                moveChangeProfile = moveChangeProfile,
                moveChangeGarden = moveChangeGarden,
                onBack = onBack
            )
        }
    }
    BackHandler(onBack = onBack)
}

@Composable
internal fun MyScreen(
    modifier: Modifier,
    profile: User,
    garden: Garden,
    memberList: List<User>,
    shareGarden: (Garden) -> Unit,
    openBrowser: (String) -> Unit,
    moveSetting: () -> Unit,
    moveChangeProfile: () -> Unit,
    moveChangeGarden: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier.fillMaxSize()
    ) {
        TutTutTopBar(title = stringResource(id = R.string.my), onBack = onBack) {
            Icon(
                modifier = modifier
                    .size(30.dp)
                    .clickableWithOutRipple(
                        onClick = moveSetting,
                        interactionSource = remember(::MutableInteractionSource)
                    ),
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "ic-setting"
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = screenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            myInfo(
                profile = profile,
                moveChangeProfile = moveChangeProfile
            )
            gardenInfo(
                garden = garden,
                memberList = memberList,
                shareGarden = shareGarden,
                moveChangeGarden = moveChangeGarden
            )
            policyInfo(openBrowser)
        }
    }
}

fun LazyListScope.myInfo(
    modifier: Modifier = Modifier,
    profile: User,
    moveChangeProfile: () -> Unit
) {
    item {
        Column(modifier) {
            TutTutLabel(
                title = stringResource(id = R.string.my_info),
                space = 20
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileItem(user = profile)
                ChangeInfoButton(
                    text = stringResource(id = R.string.change_profile),
                    onClick = moveChangeProfile
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        }
    }
}

internal fun LazyListScope.gardenInfo(
    modifier: Modifier = Modifier,
    garden: Garden,
    memberList: List<User>,
    shareGarden: (Garden) -> Unit,
    moveChangeGarden: () -> Unit,
) {
    item {
        Column(modifier) {
            TutTutLabel(
                title = stringResource(id = R.string.garden_info),
                space = 20
            )
            GardenCodeArea(
                gardenCode = garden.code,
                onCopy = { shareGarden(garden) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = garden.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                ChangeInfoButton(
                    text = stringResource(id = R.string.change_info),
                    onClick = moveChangeGarden
                )
            }
        }
    }
    items(
        items = memberList,
        key = { it.id }
    ) { user ->
        ProfileItem(user = user)
    }
    item {
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}

internal fun LazyListScope.policyInfo(
    openBrowser: (String) -> Unit
) {
    item {
        TutTutLabel(title = stringResource(id = R.string.policy), space = 10)
        TextButton(text = stringResource(id = R.string.service_policy), onClick = { openBrowser(SERVICE_POLICY_URL) })
        TextButton(text = stringResource(id = R.string.personal_info_policy), onClick = { openBrowser(PERSONAL_INFO_POLICY_URL) })
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
internal fun ProfileItem(
    modifier: Modifier = Modifier,
    user: User,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TutTutImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            url = user.profile.url
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
internal fun GardenCodeArea(
    modifier: Modifier = Modifier,
    gardenCode: String,
    onCopy: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.garden_code),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gardenCode,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            )
        }
        Box(
            modifier = modifier
                .size(50.dp)
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable { onCopy() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_copy),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "ic-copy"
            )
        }
    }
}
