# Fold State Detection

Fold state is detected using the **Jetpack WindowManager** library (`androidx.window:window:1.3.0`). No Samsung-specific SDKs are required.

## Core API

```kotlin
WindowInfoTracker
    .getOrCreate(context)           // singleton per context
    .windowLayoutInfo(activity)     // Flow<WindowLayoutInfo>
```

`WindowLayoutInfo` contains a list of `DisplayFeature` objects. On a foldable, this list includes a `FoldingFeature` when the device is **open**. The list is **empty** when the device is closed (cover screen in use).

## Collecting fold state in Compose

```kotlin
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
```

`collectAsStateWithLifecycle` automatically pauses collection when the app is backgrounded and resumes when it returns to the foreground.

## FoldingFeature properties

| Property | Values | Meaning |
|---|---|---|
| `state` | `FLAT` | Device fully open and flat |
| `state` | `HALF_OPENED` | Device partially open (tent / tabletop / book) |
| `orientation` | `HORIZONTAL` | Hinge runs left–right → tabletop / tent mode |
| `orientation` | `VERTICAL` | Hinge runs top–bottom → book mode |
| `bounds` | `Rect` | Position of the hinge in window coordinates |
| `occlusionType` | `NONE` / `FULL` | Whether the hinge obscures content |
| `isSeparating` | `Boolean` | Whether the hinge creates two logical display areas |

## FoldStateInfo wrapper

`FoldStateInfo` (in `model/FoldStateInfo.kt`) wraps the raw feature and exposes derived properties:

```kotlin
data class FoldStateInfo(val foldingFeature: FoldingFeature? = null) {

    val isTableTop: Boolean
        get() = foldingFeature?.state == HALF_OPENED
             && foldingFeature.orientation == HORIZONTAL

    val isBook: Boolean
        get() = foldingFeature?.state == HALF_OPENED
             && foldingFeature.orientation == VERTICAL

    val isFlat: Boolean
        get() = foldingFeature?.state == FLAT

    val isClosed: Boolean   // cover screen
        get() = foldingFeature == null

    val stateLabel: String  // human-readable for the badge
}
```

## Why no Activity restart on fold?

The manifest declares:

```xml
android:configChanges="orientation|screenSize|screenLayout|
                       keyboardHidden|density|smallestScreenSize|uiMode"
```

This tells the system not to recreate the `Activity` when these values change (which they do on every fold/unfold). Instead, `WindowInfoTracker` emits a new `WindowLayoutInfo` into the flow, Compose re-composes, and the correct layout is displayed — all without losing scroll position or the selected article.

## Testing without hardware

### Android Emulator — Virtual sensors

1. Launch a **Pixel Fold** or **Galaxy Z Fold** AVD.
2. Open **Extended Controls** → **Virtual sensors** → **Hinge angle**.
3. Drag the slider:
   - **180°** → `FLAT`
   - **90°–120°** → `HALF_OPENED` (tabletop when the AVD is in landscape)
   - **0°** → device closed, no `FoldingFeature` emitted

### Resizable emulator

Use the **Form factor** dropdown in the running emulator toolbar to switch between Phone, Foldable, and Tablet to exercise `WindowWidthSizeClass` breakpoints.
