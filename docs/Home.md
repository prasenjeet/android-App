# TechFeed Z — Samsung Galaxy Z Fold 5 App

A sample Android application built with **Jetpack Compose** that adapts seamlessly to all three display states of the Samsung Galaxy Z Fold 5.

## What this app demonstrates

| Capability | Technology |
|---|---|
| Fold state detection | Jetpack WindowManager 1.3 |
| Adaptive single → two-pane layout | `WindowSizeClass` |
| Tabletop / tent mode | `FoldingFeature.State.HALF_OPENED` |
| Navigation rail vs. bottom bar | `WindowWidthSizeClass` |
| Material You dynamic colour | Material 3 + Android 12+ |
| Screen continuity (no restart on fold) | `android:configChanges` in manifest |

## Pages

- [Getting Started](Getting-Started.md)
- [Architecture](Architecture.md)
- [Adaptive Layouts](Adaptive-Layouts.md)
- [Fold State Detection](Fold-State-Detection.md)
- [Screen Modes](Screen-Modes.md)
- [Adding Content](Adding-Content.md)

## Quick look

```
Cover screen  →  compact single-pane, bottom nav bar
Main screen   →  two-pane list + detail, navigation rail
Tabletop mode →  detail above fold, list below fold
```

## Requirements

- Android Studio Ladybug (2024.2+)
- Android Gradle Plugin 8.5
- Kotlin 2.0
- Minimum SDK 26 (Android 8.0)
- Target SDK 35 (Android 15)
