package com.example.zfold5app.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.zfold5app.R
import com.example.zfold5app.model.FoldStateInfo
import com.example.zfold5app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.map

/**
 * Root composable. Reads fold state from WindowInfoTracker and delegates to the
 * appropriate layout: single-pane (cover screen), two-pane (main open screen),
 * or tabletop (half-open horizontal).
 */
@Composable
fun ZFoldApp(
    windowSizeClass: WindowSizeClass,
    activity: Activity,
    viewModel: MainViewModel
) {
    val selectedArticle by viewModel.selectedArticle.collectAsStateWithLifecycle()
    val showDetail by viewModel.showDetail.collectAsStateWithLifecycle()

    val foldStateInfo by WindowInfoTracker
        .getOrCreate(activity)
        .windowLayoutInfo(activity)
        .map { layoutInfo ->
            FoldStateInfo(
                foldingFeature = layoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()
            )
        }
        .collectAsStateWithLifecycle(initialValue = FoldStateInfo())

    val isWideScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    when {
        foldStateInfo.isTableTop -> {
            // ── Tabletop / tent mode ──────────────────────────────────────────
            // Split vertically at the horizontal fold: list on top, detail below.
            TableTopLayout(
                foldStateInfo = foldStateInfo,
                articles = viewModel.articles,
                selectedArticle = selectedArticle,
                onArticleSelected = viewModel::selectArticle
            )
        }

        isWideScreen || foldStateInfo.isFlat -> {
            // ── Two-pane (main inner screen) ──────────────────────────────────
            TwoPaneLayout(
                windowSizeClass = windowSizeClass,
                foldStateInfo = foldStateInfo,
                articles = viewModel.articles,
                selectedArticle = selectedArticle,
                onArticleSelected = viewModel::selectArticle
            )
        }

        else -> {
            // ── Single-pane (cover screen / compact) ──────────────────────────
            SinglePaneLayout(
                foldStateInfo = foldStateInfo,
                articles = viewModel.articles,
                selectedArticle = selectedArticle,
                showDetail = showDetail,
                onArticleSelected = viewModel::selectArticle,
                onBack = viewModel::clearSelection
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Layout: Single pane — Cover screen
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SinglePaneLayout(
    foldStateInfo: FoldStateInfo,
    articles: List<com.example.zfold5app.model.Article>,
    selectedArticle: com.example.zfold5app.model.Article?,
    showDetail: Boolean,
    onArticleSelected: (com.example.zfold5app.model.Article) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(enabled = showDetail, onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showDetail) selectedArticle?.title ?: "" else stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    FoldStateBadge(
                        foldStateInfo = foldStateInfo,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {
            AppBottomNav()
        }
    ) { innerPadding ->
        if (showDetail && selectedArticle != null) {
            ArticleDetailPane(
                article = selectedArticle,
                showBackButton = false,
                contentPadding = innerPadding
            )
        } else {
            ArticleListPane(
                articles = articles,
                selectedArticle = selectedArticle,
                onArticleSelected = onArticleSelected,
                contentPadding = innerPadding
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Layout: Two-pane — Main inner screen (fully open)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TwoPaneLayout(
    windowSizeClass: WindowSizeClass,
    foldStateInfo: FoldStateInfo,
    articles: List<com.example.zfold5app.model.Article>,
    selectedArticle: com.example.zfold5app.model.Article?,
    onArticleSelected: (com.example.zfold5app.model.Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    FoldStateBadge(
                        foldStateInfo = foldStateInfo,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            // Navigation rail for wide screens
            AppNavigationRail()

            VerticalDivider()

            // Left pane: article list
            ArticleListPane(
                articles = articles,
                selectedArticle = selectedArticle,
                onArticleSelected = onArticleSelected,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp)
            )

            VerticalDivider()

            // Right pane: article detail or empty state
            if (selectedArticle != null) {
                ArticleDetailPane(
                    article = selectedArticle,
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                )
            } else {
                EmptyDetailPane(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Layout: Tabletop mode — horizontal fold, content above / controls below
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TableTopLayout(
    foldStateInfo: FoldStateInfo,
    articles: List<com.example.zfold5app.model.Article>,
    selectedArticle: com.example.zfold5app.model.Article?,
    onArticleSelected: (com.example.zfold5app.model.Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    FoldStateBadge(
                        foldStateInfo = foldStateInfo,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top half (above the physical fold): article detail or placeholder
            if (selectedArticle != null) {
                ArticleDetailPane(
                    article = selectedArticle,
                    modifier = Modifier.weight(1f)
                )
            } else {
                EmptyDetailPane(modifier = Modifier.weight(1f))
            }

            // Visual indicator of the physical fold hinge
            HorizontalDivider(
                thickness = 6.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Bottom half (below the physical fold): article list (thumb-friendly zone)
            ArticleListPane(
                articles = articles,
                selectedArticle = selectedArticle,
                onArticleSelected = onArticleSelected,
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared navigation components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AppBottomNav() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Article, contentDescription = "Feed") },
            label = { Text("Feed") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Saved") },
            label = { Text("Saved") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}

@Composable
private fun AppNavigationRail() {
    NavigationRail {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Article, contentDescription = "Feed") },
            label = { Text("Feed") }
        )
        NavigationRailItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Saved") },
            label = { Text("Saved") }
        )
        NavigationRailItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
        Spacer(Modifier.weight(1f))
    }
}
