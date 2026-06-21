# Getting Started

## Prerequisites

| Tool | Version |
|---|---|
| Android Studio | Ladybug 2024.2 or later |
| JDK | 17 |
| Android SDK | API 35 (Android 15) |
| Gradle | 8.7 (downloaded automatically) |

## Clone and open

```bash
git clone https://github.com/prasenjeet/android-App.git
cd android-App
git checkout claude/zfold5-dual-screen-app-q5niws
```

Open the root folder in Android Studio. The IDE will sync Gradle and download all dependencies automatically.

## Build

```bash
# Debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

## Run on a Z Fold 5

1. Enable **Developer Options** on the device.
2. Enable **USB Debugging** and connect via USB.
3. In Android Studio press **Run** (▶) or:

```bash
./gradlew installDebug
adb shell am start -n com.example.zfold5app/.MainActivity
```

## Run on the Android Emulator

The **Resizable (Experimental)** AVD best tests adaptive layouts:

1. In AVD Manager create a **Resizable** device.
2. Launch it, then use **Form factor** toolbar to switch between Phone / Foldable / Tablet.
3. For fold states, open the **Virtual sensors** panel → **Hinge angle** slider.

Alternatively use the **Galaxy Z Fold 5** hardware profile available in AVD Manager.

## Project structure

```
android-App/
├── app/
│   ├── build.gradle.kts          # App-level dependencies
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/zfold5app/
│           ├── MainActivity.kt
│           ├── model/            # Data classes + sample data
│           ├── ui/
│           │   ├── screens/      # Composable screens
│           │   ├── theme/        # Material 3 theme
│           │   └── viewmodel/
│           └── res/
├── build.gradle.kts              # Project-level plugins
└── settings.gradle.kts
```

## Dependency highlights

```kotlin
// Fold state
implementation("androidx.window:window:1.3.0")

// Window size classes
implementation("androidx.compose.material3:material3-window-size-class")

// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
```
