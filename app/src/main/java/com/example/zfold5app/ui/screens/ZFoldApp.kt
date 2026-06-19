package com.example.zfold5app.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
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

            // Hinge-aware content split — adapts to Pixel 9 Pro Fold (~50/50) and
            // Samsung Z Fold 5 (~40/60) by reading FoldingFeature.bounds from WindowManager.
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val density = LocalDensity.current
                val availableWidthPx = with(density) { maxWidth.toPx() }.toInt()

                val feature = foldStateInfo.foldingFeature
                val listFraction = remember(feature, availableWidthPx) {
                    if (feature != null && feature.bounds.left > 0 && availableWidthPx > 0) {
                        (feature.bounds.left.toFloat() / availableWidthPx).coerceIn(0.3f, 0.7f)
                    } else {
                        0.4f // fallback for non-fold or unknown bounds
                    }
                }
                val hingeWidthDp = remember(feature) {
                    with(density) { foldStateInfo.hingeWidthPx.toDp() }
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    // Left pane: article list (sized to hinge left edge)
                    ArticleListPane(
                        articles = articles,
                        selectedArticle = selectedArticle,
                        onArticleSelected = onArticleSelected,
                        modifier = Modifier
                            .weight(listFraction)
                            .fillMaxHeight(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp)
                    )

                    // Physical hinge gap (visible on devices with a separating fold).
                    // Pixel 9 Pro Fold: narrow crease (~0 dp logical); Z Fold 5: wider gap.
                    if (foldStateInfo.isSeparating && hingeWidthDp > 1.dp) {
                        Spacer(
                            modifier = Modifier
                                .width(hingeWidthDp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    } else {
                        VerticalDivider()
                    }

                    // Right pane: article detail or empty state
                    if (selectedArticle != null) {
                        ArticleDetailPane(
                            article = selectedArticle,
                            modifier = Modifier
                                .weight(1f - listFraction)
                                .fillMaxHeight()
                        )
                    } else {
                        EmptyDetailPane(
                            modifier = Modifier
                                .weight(1f - listFraction)
                                .fillMaxHeight()
                        )
                    }
                }
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
        // Use hinge bounds to position the fold line accurately.
        // Pixel 9 Pro Fold tabletop: hinge top ≈ center of display height.
        // Samsung Z Fold 5 tabletop: similar center split.
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val density = LocalDensity.current
            val availableHeightPx = with(density) { maxHeight.toPx() }.toInt()

            val feature = foldStateInfo.foldingFeature
            val topFraction = remember(feature, availableHeightPx) {
                if (feature != null && feature.bounds.top > 0 && availableHeightPx > 0) {
                    (feature.bounds.top.toFloat() / availableHeightPx).coerceIn(0.3f, 0.7f)
                } else {
                    0.5f
                }
            }
            val hingeHeightDp = remember(feature) {
                with(density) { foldStateInfo.hingeHeightPx.toDp() }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Top half (above the physical fold): article detail or placeholder
                if (selectedArticle != null) {
                    ArticleDetailPane(
                        article = selectedArticle,
                        modifier = Modifier.weight(topFraction)
                    )
                } else {
                    EmptyDetailPane(modifier = Modifier.weight(topFraction))
                }

                // Physical hinge divider — sized to actual hinge height when known
                if (foldStateInfo.isSeparating && hingeHeightDp > 1.dp) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(Modifier.height(hingeHeightDp))
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                } else {
                    HorizontalDivider(
                        thickness = 6.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                // Bottom half (below fold): article list (thumb-friendly zone)
                ArticleListPane(
                    articles = articles,
                    selectedArticle = selectedArticle,
                    onArticleSelected = onArticleSelected,
                    modifier = Modifier.weight(1f - topFraction),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 8.dp)
                )
            }
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
