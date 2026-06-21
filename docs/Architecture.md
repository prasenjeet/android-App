# Architecture

## Overview

The app follows a single-Activity, unidirectional-data-flow architecture built entirely with Jetpack Compose. There are no Fragments or XML layouts.

```
MainActivity
    └── ZFold5AppTheme
            └── ZFoldApp          ← reads WindowInfoTracker + WindowSizeClass
                    ├── SinglePaneLayout   (cover screen)
                    ├── TwoPaneLayout      (main screen, flat open)
                    └── TableTopLayout     (half-open horizontal)
```

## Layer breakdown

### `model/`

| File | Purpose |
|---|---|
| `Article.kt` | `Article` data class and `sampleArticles` list |
| `FoldStateInfo.kt` | Wraps `FoldingFeature`; exposes `isTableTop`, `isBook`, `isFlat`, `stateLabel` |

### `ui/viewmodel/`

`MainViewModel` holds two `StateFlow`s:

- `selectedArticle: StateFlow<Article?>` — which article the user tapped
- `showDetail: StateFlow<Boolean>` — whether to navigate to the detail pane (single-pane only)

The ViewModel survives configuration changes (fold/unfold, rotation) automatically.

### `ui/screens/`

| File | Purpose |
|---|---|
| `ZFoldApp.kt` | Root composable; fold-state router; contains all three layout variants |
| `ArticleListPane.kt` | Lazy scrollable list of `ArticleCard` composables |
| `ArticleDetailPane.kt` | Full article view with hero block and body; also `EmptyDetailPane` |
| `FoldStateBadge.kt` | Live badge in the top bar showing the current fold state |

### `ui/theme/`

Standard Material 3 theme wiring: `Color.kt`, `Type.kt`, `Theme.kt`.  
Material You dynamic colour is enabled by default on Android 12+.

## Data flow

```
User taps article
    → MainViewModel.selectArticle(article)
        → _selectedArticle.value = article
            → ZFoldApp re-composes
                → correct pane shows the article
```

## Configuration change handling

`android:configChanges` in the manifest lists `orientation|screenSize|screenLayout|smallestScreenSize|density|uiMode`. This prevents Activity recreation on fold/unfold, giving the OS a chance to deliver `WindowLayoutInfo` updates smoothly instead of restarting from scratch.

Because `MainViewModel` is retained across these non-restart config changes, the selected article and scroll position are preserved when the user opens or closes the fold.
