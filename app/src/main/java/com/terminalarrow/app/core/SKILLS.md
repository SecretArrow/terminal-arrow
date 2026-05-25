# SKILL: Ultimate Professional Android Development (MVI + Clean Architecture)

## Trigger Conditions
Use this skill for any task involving:
- Building or refactoring Android applications (Kotlin primary).
- Implementing Clean Architecture, MVI (Model-View-Intent), or MVVM.
- Handling complex system integrations (SSH, SFTP, Cloud APIs, Biometrics).
- Modernizing UI with Jetpack Compose and Material You.
- Hardening app stability (Android 14+ compatibility, Foreground Services).
- English-only codebase and documentation mandate.

---

## 0. THE "NON-NEGOTIABLE" MANIFESTO
1. **English First**: 100% of code (variables, functions, classes), comments, and commit messages must be in professional English.
2. **Crash-Proof by Design**: Every IO, Network, or DB call MUST be wrapped in structured error handling (`AppResult`).
3. **MVI Strictness**: Unidirectional data flow is law. UI observed state via `collectAsStateWithLifecycle`. Events via `onEvent(UiEvent)`.
4. **API 34+ Compliance**: Strict adherence to Android 14 Foreground Service types and permissions.
5. **No Technical Debt**: Deprecated APIs must be replaced or documented with a migration plan immediately.

---

## 1. ADVANCED ARCHITECTURE

### 1.1 The Ultimate MVI Pattern
```kotlin
// UI State (StateFlow)
sealed interface FeatureUiState {
    data object Loading : FeatureUiState
    data class Success(val data: List<Item>, val isProcessing: Boolean = false) : FeatureUiState
    data class Error(val message: String) : FeatureUiState
}

// UI Event (Intent)
sealed interface FeatureUiEvent {
    data object Refresh : FeatureUiEvent
    data class Submit(val input: String) : FeatureUiEvent
}

// UI Effect (Side Effect - Channel)
sealed interface FeatureUiEffect {
    data class ShowSnackbar(val message: String) : FeatureUiEffect
    data object PlayHapticFeedback : FeatureUiEffect
}
```

### 1.2 Module Layering
- **:core-ui**: Shared Design System (Material 3, Themes, Common Composables).
- **:core-common**: Result wrappers, high-level exceptions, standard extensions.
- **:core-network/database**: Low-level infrastructure.
- **:feature-***: Vertical slices containing Route, Screen, and ViewModel.

---

## 2. STABILITY & SYSTEM HARDENING

### 2.1 Android 14 (API 34) Foreground Services
Always declare `foregroundServiceType` in Manifest AND provide the corresponding permission.
```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
<service 
    android:name=".MyService" 
    android:foregroundServiceType="dataSync" />
```
In Code (Service):
```kotlin
ServiceCompat.startForeground(
    this, ID, notification,
    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
)
```

### 2.2 Modern System Services (API 31+)
Avoid deprecated calls. Example for Haptics/Vibration:
```kotlin
val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
} else {
    @Suppress("DEPRECATION") context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}
```

### 2.3 Global Exception Handling
Implement a top-level handler in the `Application` class to capture and log silent crashes.
```kotlin
Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
    Log.e("CRASH", "Uncaught exception in ${thread.name}", throwable)
}
```

---

## 3. UI/UX BEST PRACTICES

### 3.1 Jetpack Compose Performance
- **Keys Everywhere**: Use `key` in `items()` of `LazyColumn`/`LazyRow` to prevent massive recompositions.
- **State Hoisting**: Screens should be stateless; all state management stays in the Route/ViewModel.
- **Immersive Mode**: Use `WindowInsetsControllerCompat` to hide system bars for full-screen tools (like Terminals).
- **Material You**: Always implement `dynamicDarkColorScheme` and `dynamicLightColorScheme`.

### 3.2 Professional Feedback Loop
- **Loading**: Use `CircularProgressIndicator` for full screen, `LinearProgressIndicator` for background tasks (e.g., file transfers).
- **Error**: Always provide a "Retry" button and a clear explanation of what went wrong.
- **Empty States**: Never show a blank screen. Use an icon + "No [Items] Found" + CTA.

---

## 4. CODE QUALITY & AUTO-FIXING

### 4.1 Lint & Static Analysis
- Run `./gradlew lintDebug` before every commit.
- Zero-tolerance for `ForegroundServicePermission` and `ThreadPriority` errors.
- Enforce `ktlint` for consistent formatting.

### 4.2 Safe IO Patterns
Never assume local storage or network is available.
```kotlin
suspend fun safeFileOperation(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: IOException) {
        _uiEffect.send(Effect.ShowSnackbar("Storage full or unavailable"))
    } catch (e: Exception) {
        _uiEffect.send(Effect.ShowSnackbar("Unexpected error"))
    }
}
```

---

## 5. TESTING STRATEGY

### 5.1 Unit Testing (Back-end)
- Use **Mockito** to mock `Context` and `Repositories`.
- Use `runTest` and `StandardTestDispatcher` for Coroutines.
- Verify state transitions: `Idle -> Loading -> Success`.

### 5.2 UI Testing (Front-end)
- Use `createAndroidComposeRule`.
- Verify critical nodes: `onNodeWithText`, `onNodeWithTag`.
- Test user flows (e.g., "Add Profile" -> "Verify it appears in list").

---

## 6. RELEASE & DEPLOYMENT

### 6.1 ABI Splitting
Reduce user download size by splitting APKs.
```kotlin
splits {
    abi {
        isEnable = true
        include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        isUniversalApk = true
    }
}
```

### 6.2 ProGuard/R8 Safety
- Keep all `@Keep` and `@Entity` classes.
- Explicitly keep third-party library internals (e.g., SSHJ, AWS SDK) that use reflection.

---

## 7. SHIP-IT CHECKLIST
- [ ] Language: Is the entire UI and Code in English?
- [ ] Android 14: Are all Service permissions and types declared?
- [ ] UI State: Does every screen have Loading/Error states?
- [ ] Memory: Are all `lateinit` vars initialized? LeakCanary clean?
- [ ] DI: Is Hilt properly implemented without circular dependencies?
- [ ] Release: Is the build signed and split by ABI?
