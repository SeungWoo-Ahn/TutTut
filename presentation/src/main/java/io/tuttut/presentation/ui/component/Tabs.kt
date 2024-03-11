package io.tuttut.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.tuttut.presentation.theme.screenHorizontalPadding

@Composable
fun TutTutTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    tabs: @Composable () -> Unit
) {
    TabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        tabs = tabs
    )
}

@Composable
fun TutTutTab(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(vertical = screenHorizontalPadding),
            text = title,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun MainScreenTab(
    modifier: Modifier = Modifier,
    selectedTab: MainTab,
    onSelectTab: (MainTab) -> Unit
) {
    val tabs = listOf(MainTab.GROWING, MainTab.HARVESTED)
    TutTutTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTab.index
    ) {
        tabs.forEach { tab ->
            TutTutTab(
                title = tab.title,
                selected = tab == selectedTab,
                onClick = { onSelectTab(tab) }
            )
        }
    }
}

enum class MainTab(val index: Int, val title: String) {
    GROWING(0, "재배중"),
    HARVESTED(1, "수확 완료")
}