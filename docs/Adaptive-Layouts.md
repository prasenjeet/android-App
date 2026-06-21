# Adaptive Layouts

The app uses three distinct layouts, chosen at runtime based on `WindowSizeClass` and `FoldingFeature` state.

## Decision logic (simplified)

```kotlin
when {
    foldStateInfo.isTableTop          -> TableTopLayout()
    isWideScreen || foldStateInfo.isFlat -> TwoPaneLayout()
    else                               -> SinglePaneLayout()
}
```

`isWideScreen` is `true` when `WindowWidthSizeClass != Compact` — that is, the device reports at least **600 dp** of available width.

---

## SinglePaneLayout — Cover screen

**Triggered by:** `WindowWidthSizeClass.Compact` (≈ 360 dp on the Z Fold 5 cover display)

```
┌─────────────────┐
│   Top App Bar   │
├─────────────────┤
│                 │
│   Article List  │  ← tapping an article pushes to detail
│   (LazyColumn)  │
│                 │
├─────────────────┤
│  Bottom Nav Bar │  Feed | Saved | Settings
└─────────────────┘
```

- Navigation uses a **Bottom Navigation Bar** (thumbs reach it easily on a narrow screen).
- The `BackHandler` intercepts the system back gesture to return from detail → list.
- `FoldStateBadge` in the top bar shows "Cover Screen".

---

## TwoPaneLayout — Main inner screen

**Triggered by:** `WindowWidthSizeClass.Medium` or `Expanded` (≈ 770–930 dp on the Z Fold 5 inner display), or `FoldingFeature.State.FLAT`.

```
┌──┬──────────────┬─┬──────────────────────┐
│  │              │ │                      │
│N │  Article     │▌│   Article Detail     │
│a │  List        │▌│   (or EmptyPane)     │
│v │  40 % width  │▌│   60 % width         │
│  │              │ │                      │
└──┴──────────────┴─┴──────────────────────┘
 Nav  List pane  Hinge  Detail pane
 Rail
```

- Navigation uses a **Navigation Rail** (better ergonomics on wide screens).
- A `VerticalDivider` visually represents the hinge area between the two panes.
- `EmptyDetailPane` is shown until the user selects an article.
- Both panes are always visible — no back-stack navigation needed.

---

## TableTopLayout — Tabletop / Tent mode

**Triggered by:** `FoldingFeature.State.HALF_OPENED` + `FoldingFeature.Orientation.HORIZONTAL`

```
┌──────────────────────────┐  ← top half (above physical fold)
│   Article Detail         │
│   (or EmptyDetailPane)   │
├──────────────────────────┤  ← HorizontalDivider (6 dp) marks the hinge
│   Article List           │  ← bottom half (thumb-friendly zone)
│   (LazyColumn)           │
└──────────────────────────┘
```

- Content the user reads goes **above** the fold (natural viewing angle).
- Interactive list goes **below** the fold (in the thumb zone, device sits on a surface).
- The 6 dp `HorizontalDivider` gives a tactile visual cue for where the physical hinge is.

---

## Navigation summary

| Screen state | Navigation component | Why |
|---|---|---|
| Cover / compact | `NavigationBar` (bottom) | Reachable with thumbs on narrow screen |
| Inner / wide | `NavigationRail` (left side) | Standard Material 3 large-screen pattern |
| Tabletop | No persistent nav | Taskbar / swipe handles navigation |
