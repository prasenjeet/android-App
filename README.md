# TechFeed Z

A Samsung Galaxy Z Fold 5 adaptive Android news-reader app built with Jetpack Compose and Jetpack WindowManager. The app demonstrates best-practice foldable UI patterns by detecting the physical fold state in real time and switching between three distinct layouts.

## Features

- **Cover Screen (single-pane)** — compact list/detail navigation with bottom nav bar and back-stack handling
- **Main Screen / Wide (two-pane)** — side-by-side article list (40 %) and detail (60 %) with a navigation rail
- **Tabletop / Tent Mode** — content above the physical hinge, thumb-friendly article list below the hinge
- **Fold State Badge** — live indicator in the top app bar showing the current fold state (Cover Screen, Tabletop Mode, Book Mode, Fully Open)
- Smooth transitions between layouts as the device is opened, closed, or tilted — no restart required

## Architecture

```
app/
└── src/main/java/com/example/zfold5app/
    ├── MainActivity.kt            # Entry point; sets up WindowSizeClass + ViewModel
    ├── model/
    │   ├── Article.kt             # Data class + sample articles
    │   └── FoldStateInfo.kt       # Wraps FoldingFeature; exputes isTableTop / isBook / isFlat
    └── ui/
        ├── screens/
        │   ├── ZFoldApp.kt        # Root composable; selects layout based on fold state
        │   ├── ArticleListPane.kt # Scrollable article list
        │   ├── ArticleDetailPane.kt # Full article reader
        │   └── FoldStateBadge.kt  # Top-bar fold state chip
        ├── viewmodel/
        │   └── MainViewModel.kt   # Holds article list + selected article state
        └── theme/
            ├── Color.kt
            ├── Theme.kt
            └── Type.kt
```

## Layout Decision Tree

```
FoldingFeature.state == HALF_OPENED && orientation == HORIZONTAL
    └── TableTopLayout   (detail on top, list on bottom)

WindowWidthSizeClass != Compact  OR  FoldingFeature.state == FLAT
    └── TwoPaneLayout    (navigation rail + list | detail side-by-side)

Else (compact / cover screen)
    └── SinglePaneLayout (bottom nav + list → detail with back-stack)
```

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose (BOM 2024.09.00) |
| Material | Material 3 + Window Size Class |
| Fold detection | Jetpack WindowManager 1.3.0 |
| State | ViewModel + StateFlow + `collectAsStateWithLifecycle` |
| Navigation | Navigation Compose 2.8.0 |

## Requirements

- Android Studio Hedgehog or newer
- Android SDK 35 (compile) / 26 (minimum)
- JDK 17
- Physical Samsung Galaxy Z Fold device **or** the Android Emulator with a foldable AVD (use the Virtual Sensor panel to simulate fold states)

## Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Run on a foldable emulator or physical device:
   ```
   ./gradlew :app:installDebug
   ```
4. To test fold states on the emulator, open **View > Tool Windows > Virtual Sensors** and adjust the hinge angle slider

## Testing Fold States

| Hinge Angle | FoldingFeature state | Layout triggered |
|---|---|---|
| 180° (fully open) | `FLAT` | Two-pane |
| 90° (tabletop) | `HALF_OPENED` + `HORIZONTAL` | Tabletop |
| 0° (closed) | `null` | Single-pane |
| Compact window | — | Single-pane |
