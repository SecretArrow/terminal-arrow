# SKILL: Professional Android App Development

## Trigger Conditions
Use this skill whenever the task involves:
- Creating or modifying Android applications (Kotlin/Java)
- Architecting Android projects (MVVM, MVI, Clean Architecture)
- Implementing background services, workers, or scheduled tasks
- Building modern Android UI with Jetpack Compose or XML
- Configuring CI/CD pipelines for Android (GitHub Actions)
- Producing release builds (ABI splits, app bundles, ProGuard)
- Crash prevention, error handling, and stability hardening
- Handling Android lifecycle, permissions, or system integrations

---

## STEP 0 — READ THIS SKILL BEFORE WRITING A SINGLE LINE OF CODE
This file is **mandatory reading** before any Android work. Skipping it produces fragile, unarchitectured, crash-prone apps. Every section applies to every task unless explicitly noted otherwise.

---

## 1. PROJECT ARCHITECTURE

### 1.1 Module Structure (always multi-module for non-trivial apps)
```
root/
├── app/                      # Shell module — DI graph root, Application class
├── core/
│   ├── core-data/            # Repository implementations, data sources
│   ├── core-domain/          # Use cases, domain models, repository interfaces
│   ├── core-network/         # Retrofit/Ktor setup, interceptors, DTOs
│   ├── core-database/        # Room database, DAOs, entities
│   ├── core-datastore/       # Proto DataStore / Preferences DataStore
│   ├── core-ui/              # Shared composables, themes, design tokens
│   └── core-common/          # Pure Kotlin utilities, extensions, Result wrapper
├── feature/
│   ├── feature-home/
│   ├── feature-auth/
│   └── feature-settings/
└── build-logic/              # Convention plugins (Gradle), shared build config
```

### 1.2 Architectural Pattern — MVI + Clean Architecture
```
UI Layer        ViewModel (StateFlow, UiState, UiEvent, UiEffect)
Domain Layer    UseCases — one public function, no Android imports
Data Layer      Repositories → RemoteDataSource + LocalDataSource
```

**Non-negotiable rules:**
- `ViewModel` never holds a reference to `Context`, `Activity`, or `Fragment`
- `UseCase` never imports anything from `android.*` (pure Kotlin)
- `Repository` interface lives in `:core-domain`; implementation lives in `:core-data`
- Every layer communicates through the layer directly below it — no layer skipping

### 1.3 Dependency Injection — Hilt
```kotlin
// Application
@HiltAndroidApp
class App : Application()

// ViewModel
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel()

// Provide third-party deps
@Module @InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}
```

---

## 2. APP LIFECYCLE MANAGEMENT

### 2.1 Activity & Fragment Lifecycle Rules
- Never perform fragment transactions in `onResume()` — use `onViewCreated()` or `lifecycleScope`
- Never access views after `onDestroyView()` — always null binding references
- Use `ViewBinding` or `Compose` — never `findViewById` in production code
- Always cancel coroutines in the correct scope; prefer `viewModelScope` and `lifecycleScope`

```kotlin
// Correct: cancel-safe coroutine in Fragment
viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state -> render(state) }
    }
}
```

### 2.2 ViewModel State Survival
```kotlin
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,          // survives process death
    private val getDetailUseCase: GetDetailUseCase
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])
    
    val uiState: StateFlow<DetailUiState> = getDetailUseCase(id)
        .map { result -> result.toUiState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailUiState.Loading)
}
```

### 2.3 Process Death & State Restoration
- Use `SavedStateHandle` for all navigation arguments and critical transient state
- Use `rememberSaveable` in Compose for UI-local state that must survive config changes
- Store large data in Room/DataStore, never in `onSaveInstanceState` bundles

---

## 3. BACKGROUND WORK

### 3.1 Choosing the Right API
| Use Case | API |
|---|---|
| Deferrable, guaranteed work | `WorkManager` |
| Real-time user-facing work | `Foreground Service` |
| Repeating alarm-exact timing | `AlarmManager` (with exact alarm permission) |
| Short async tasks | `Coroutine` in `viewModelScope` |
| Location, audio, BT | `Foreground Service` with correct type |

### 3.2 WorkManager — Canonical Pattern
```kotlin
// Define
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            syncRepository.sync()
            Result.success()
        } catch (e: HttpException) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        } catch (e: Exception) {
            Result.failure(workDataOf("error" to e.message))
        }
    }
}

// Schedule
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(true)
    .build()

val request = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
    .setConstraints(constraints)
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
    .build()

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "sync", ExistingPeriodicWorkPolicy.KEEP, request
)
```

### 3.3 Foreground Service
```kotlin
class MusicService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification()
        // API 34+: must declare foreground service type
        ServiceCompat.startForeground(
            this, NOTIFICATION_ID, notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
        return START_STICKY
    }
}
```
Always declare in `AndroidManifest.xml`:
```xml
<service
    android:name=".MusicService"
    android:foregroundServiceType="mediaPlayback"
    android:exported="false" />
```

---

## 4. MODERN UI/UX — JETPACK COMPOSE

### 4.1 Screen Architecture (single-responsibility)
```kotlin
// Route (navigation entry point — handles ViewModel wiring)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(uiState = uiState, onPostClick = onNavigateToDetail)
}

// Screen (pure, stateless — easily testable and previewable)
@Composable
fun HomeScreen(uiState: HomeUiState, onPostClick: (String) -> Unit) {
    when (uiState) {
        HomeUiState.Loading -> LoadingIndicator()
        is HomeUiState.Error -> ErrorView(message = uiState.message)
        is HomeUiState.Success -> PostList(posts = uiState.posts, onPostClick = onPostClick)
    }
}
```

### 4.2 Design System
- Define all design tokens in a single `Theme.kt` — colors, typography, shapes, spacing
- Use `MaterialTheme` tokens everywhere — never hardcode colors or sizes
- Support both light and dark themes from day one using `DynamicColorScheme` (API 31+) with fallback palette
- Use `WindowSizeClass` for adaptive layouts (phone / tablet / foldable)

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= 31 ->
            if (darkTheme) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, content = content)
}
```

### 4.3 Navigation — Compose Navigation + Type-Safe Routes
```kotlin
// Define type-safe routes (Kotlin Serialization)
@Serializable object HomeRoute
@Serializable data class DetailRoute(val id: String)

// NavHost
NavHost(navController, startDestination = HomeRoute) {
    composable<HomeRoute> { HomeRoute(onNavigateToDetail = { navController.navigate(DetailRoute(it)) }) }
    composable<DetailRoute> { DetailRoute(onBack = navController::popBackStack) }
}
```

### 4.4 UX Non-Negotiables
- Every loading state shows a skeleton or shimmer — never a blank screen
- Every error state shows a retry button + human-readable message
- Every destructive action requires a confirmation dialog
- Empty states show an illustration + CTA — never just blank
- All network images use Coil with `placeholder`, `error`, and `crossfade`
- Minimum touch target: 48dp × 48dp for all interactive elements
- All text must pass WCAG AA contrast ratio (4.5:1 minimum)

---

## 5. CRASH PREVENTION & ERROR HANDLING

### 5.1 Global Crash Handler
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            FirebaseCrashlytics.getInstance().recordException(throwable)
            // Optionally restart to a safe Activity
            restartApp()
        }
    }
}
```

### 5.2 Result Wrapper — Never Throw Across Layers
```kotlin
sealed class AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error(val exception: AppException) : AppResult<Nothing>()
    data object Loading : AppResult<Nothing>()
}

sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(cause: Throwable) : AppException("No internet connection", cause)
    class ServerException(val code: Int, message: String) : AppException(message)
    class UnknownException(cause: Throwable) : AppException("Unexpected error", cause)
}

// Repository implementation
override suspend fun getPosts(): AppResult<List<Post>> = withContext(Dispatchers.IO) {
    try {
        val response = api.getPosts()
        AppResult.Success(response.map { it.toDomain() })
    } catch (e: IOException) {
        AppResult.Error(AppException.NetworkException(e))
    } catch (e: HttpException) {
        AppResult.Error(AppException.ServerException(e.code(), e.message()))
    } catch (e: Exception) {
        AppResult.Error(AppException.UnknownException(e))
    }
}
```

### 5.3 Safe Coroutine Patterns
```kotlin
// ViewModel — never let uncaught exceptions kill the app
val uiState: StateFlow<UiState> = useCase()
    .catch { emit(AppResult.Error(it.toAppException())) }
    .map { it.toUiState() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

// Structured concurrency — parallel calls with error isolation
viewModelScope.launch {
    supervisorScope {
        val postsDeferred = async { getPostsUseCase() }
        val userDeferred = async { getUserUseCase() }
        // If one fails, the other continues
        val posts = postsDeferred.await()
        val user = userDeferred.await()
    }
}
```

### 5.4 Null Safety & Type Safety Rules
- Never use `!!` (force-unwrap) — use `?: return`, `?: throw`, or `requireNotNull` with a message
- Never use raw `Any` or unchecked casts without a safe-cast guard
- Always validate navigation arguments before use via `SavedStateHandle` + `requireNotNull`
- Seal all state classes — exhaustive `when` expressions prevent unhandled states

### 5.5 Memory Leak Prevention
- Never store `Activity`, `Fragment`, or `View` references in `ViewModel` or singletons
- Always unregister `BroadcastReceiver` and listeners in the matching lifecycle callback
- Use `WeakReference` when a long-lived object must reference a short-lived one
- Run LeakCanary in debug builds always:
```kotlin
// No code needed — add dependency only in debugImplementation
debugImplementation("com.squareup.leakcanary:leakcanary-android:2.x")
```

### 5.6 ANR Prevention
- Never perform disk I/O, network calls, or heavy computation on the main thread
- Use `StrictMode` in debug builds to catch accidental main-thread violations:
```kotlin
if (BuildConfig.DEBUG) {
    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
        .detectAll().penaltyLog().penaltyDeath().build())
    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
        .detectAll().penaltyLog().build())
}
```

---

## 6. TESTING STRATEGY

### 6.1 Test Pyramid
```
Unit Tests (70%)     — UseCases, ViewModels, Repositories (fake deps), Mappers
Integration (20%)    — Room DAOs, DataStore, Repository with real Room
UI/E2E (10%)         — Compose UI tests, critical user journeys with Espresso/Maestro
```

### 6.2 ViewModel Unit Test Template
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    private val fakeUseCase = FakeGetPostsUseCase()
    private lateinit var viewModel: HomeViewModel

    @Before fun setUp() { viewModel = HomeViewModel(fakeUseCase) }

    @Test fun `emits Success state when use case returns data`() = runTest {
        fakeUseCase.emit(AppResult.Success(testPosts))
        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(HomeUiState.Success::class.java)
    }
}
```

### 6.3 Code Coverage Minimums
- Domain (UseCases): **90%**
- ViewModel: **80%**
- Repository: **70%**
- UI composables: **key flows covered**

---

## 7. CI/CD — GITHUB ACTIONS

### 7.1 Pipeline Structure
```
on: [push to main/develop, pull_request]

jobs:
  1. lint          → ktlint, detekt, Android Lint
  2. unit-test     → ./gradlew testDebugUnitTest
  3. build-debug   → ./gradlew assembleDebug
  4. build-release → ./gradlew bundleRelease (on main only)
  5. sign          → sign AAB with keystore from GitHub Secrets
  6. deploy        → upload to Firebase App Distribution / Play Store (on main only)
```

### 7.2 Full Workflow File
```yaml
# .github/workflows/android.yml
name: Android CI/CD

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

jobs:
  lint:
    name: Lint & Static Analysis
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '17' }
      - uses: gradle/actions/setup-gradle@v3
      - run: ./gradlew ktlintCheck detekt lintDebug --continue

  unit-test:
    name: Unit Tests
    runs-on: ubuntu-latest
    needs: lint
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '17' }
      - uses: gradle/actions/setup-gradle@v3
      - run: ./gradlew testDebugUnitTest koverXmlReport
      - uses: actions/upload-artifact@v4
        if: always()
        with:
          name: test-results
          path: '**/build/reports/tests/'

  build-debug:
    name: Build Debug APK
    runs-on: ubuntu-latest
    needs: unit-test
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '17' }
      - uses: gradle/actions/setup-gradle@v3
      - run: ./gradlew assembleDebug
      - uses: actions/upload-artifact@v4
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/*.apk

  build-release:
    name: Build & Sign Release AAB
    runs-on: ubuntu-latest
    needs: unit-test
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: '17' }
      - uses: gradle/actions/setup-gradle@v3

      - name: Decode Keystore
        run: |
          echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > ${{ github.workspace }}/keystore.jks

      - name: Build Release AAB
        env:
          KEYSTORE_PATH: ${{ github.workspace }}/keystore.jks
          KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
          STORE_PASSWORD: ${{ secrets.STORE_PASSWORD }}
        run: ./gradlew bundleRelease

      - uses: actions/upload-artifact@v4
        with:
          name: release-aab
          path: app/build/outputs/bundle/release/*.aab

  deploy-firebase:
    name: Deploy to Firebase App Distribution
    runs-on: ubuntu-latest
    needs: build-release
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v4
      - uses: actions/download-artifact@v4
        with: { name: release-aab }
      - uses: w9jds/firebase-action@master
        with:
          args: appdistribution:distribute *.aab --app ${{ secrets.FIREBASE_APP_ID }} --groups "testers"
        env:
          GCP_SA_KEY: ${{ secrets.FIREBASE_SERVICE_ACCOUNT }}
```

### 7.3 Required GitHub Secrets
| Secret | Description |
|---|---|
| `KEYSTORE_BASE64` | Base64-encoded `.jks` keystore file |
| `KEY_ALIAS` | Key alias in keystore |
| `KEY_PASSWORD` | Key password |
| `STORE_PASSWORD` | Keystore password |
| `FIREBASE_APP_ID` | Firebase app ID |
| `FIREBASE_SERVICE_ACCOUNT` | Firebase GCP service account JSON |

---

## 8. RELEASE BUILD CONFIGURATION

### 8.1 ABI Split — Reduce APK Size
```kotlin
// app/build.gradle.kts
android {
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = false  // set true if also need a fat APK
        }
    }
}

// Version code must be unique per ABI for Play Store
val abiCodes = mapOf("armeabi-v7a" to 1, "arm64-v8a" to 2, "x86" to 3, "x86_64" to 4)

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val abi = output.filters.find { it.filterType == ABI }?.identifier
            val abiCode = abiCodes[abi] ?: 0
            output.versionCode.set(abiCode * 1000 + (variant.outputs.first().versionCode.get() ?: 0))
        }
    }
}
```

### 8.2 App Bundle (Preferred Over APK for Play Store)
```kotlin
android {
    bundle {
        language { enableSplit = true }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
}
```

### 8.3 ProGuard / R8 Rules
```proguard
# Keep data classes used with Moshi/Gson
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
-keep class com.yourpackage.data.model.** { *; }

# Retrofit
-keepattributes Signature, Exceptions
-keep class retrofit2.** { *; }
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Hilt
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
```

### 8.4 Build Variants
```kotlin
android {
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    flavorDimensions += "environment"
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            buildConfigField("String", "BASE_URL", "\"https://staging-api.example.com/\"")
        }
        create("production") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
        }
    }
}
```

---

## 9. SECURITY

- Store secrets in `local.properties` — never commit to git
- Use `EncryptedSharedPreferences` or `EncryptedFile` (Jetpack Security) for sensitive local data
- Pin certificates for sensitive endpoints using OkHttp `CertificatePinner`
- Enable `android:allowBackup="false"` in manifest unless backup is explicitly required
- Use `FLAG_SECURE` on Activities that display sensitive data (e.g., payment screens)
- Enable Play Integrity API for sensitive operations
- Obfuscate with R8/ProGuard in every release build — non-negotiable

```kotlin
// Certificate Pinning
val client = OkHttpClient.Builder()
    .certificatePinner(
        CertificatePinner.Builder()
            .add("api.example.com", "sha256/AAAA...==")
            .build()
    ).build()
```

---

## 10. PERFORMANCE

### 10.1 Compose Performance Rules
- Always use `key()` in `LazyColumn`/`LazyRow` items
- Use `derivedStateOf` to avoid unnecessary recompositions
- Profile with Layout Inspector and Recomposition Highlighter before shipping
- Avoid lambda allocations inside composables — hoist or use `remember`

```kotlin
// Bad — new lambda every recomposition
LazyColumn { items(posts) { post -> PostCard(post, onClick = { navigate(post.id) }) } }

// Good — stable lambda
LazyColumn { items(posts, key = { it.id }) { post -> PostCard(post, onPostClick) } }
```

### 10.2 Baseline Profiles
Generate a Baseline Profile for critical user journeys to improve startup time by up to 40%:
```kotlin
// macrobenchmark module
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule val rule = BaselineProfileRule()

    @Test fun generateProfile() = rule.collect(packageName = "com.example.app") {
        pressHome(); startActivityAndWait()
        device.findObject(By.text("Posts")).click()
    }
}
```

---

## 11. CONVENTIONS & CODE QUALITY

### 11.1 Naming Conventions
| Item | Convention | Example |
|---|---|---|
| Composable | PascalCase noun | `PostCard`, `HomeScreen` |
| ViewModel | PascalCase + ViewModel | `HomeViewModel` |
| UseCase | PascalCase verb + UseCase | `GetPostsUseCase` |
| StateFlow | camelCase + State | `uiState`, `searchState` |
| Events (user) | Sealed class + Event | `HomeEvent.Refresh` |
| Effects (side) | Sealed class + Effect | `HomeEffect.ShowSnackbar` |
| Constants | SCREAMING_SNAKE_CASE | `MAX_RETRY_COUNT` |

### 11.2 Kotlin Style Enforcements
- Enforce with `ktlint` + `detekt` in CI (block merge on violations)
- Max function length: 40 lines (detekt rule)
- Max complexity: 10 (detekt cyclomatic complexity)
- No magic numbers — always name constants
- Prefer `data class` for models, `object` for singletons, `sealed class` for states/results

### 11.3 Required Dependencies (Gradle Version Catalog)
```toml
# gradle/libs.versions.toml
[versions]
kotlin = "2.0.0"
agp = "8.5.0"
compose-bom = "2024.06.00"
hilt = "2.51.1"
room = "2.6.1"
retrofit = "2.11.0"
coroutines = "1.8.1"
lifecycle = "2.8.2"
navigation = "2.8.0"
work = "2.9.0"
coil = "2.6.0"
moshi = "1.15.1"
leakcanary = "2.14"
detekt = "1.23.6"

[libraries]
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-moshi = { group = "com.squareup.retrofit2", name = "converter-moshi", version.ref = "retrofit" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
lifecycle-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }
work-runtime = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
leakcanary = { group = "com.squareup.leakcanary", name = "leakcanary-android", version.ref = "leakcanary" }
```

---

## 12. AUTO-FIX & SELF-HEALING PATTERNS

### 12.1 Automatic Retry with Exponential Backoff
```kotlin
suspend fun <T> withRetry(
    maxAttempts: Int = 3,
    initialDelay: Long = 1_000,
    maxDelay: Long = 30_000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(maxAttempts - 1) { attempt ->
        try { return block() } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.w(e, "Attempt ${attempt + 1} failed, retrying in ${currentDelay}ms")
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // Last attempt — propagate exception if it fails
}
```

### 12.2 Database Migration Safety
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE posts ADD COLUMN bookmark INTEGER NOT NULL DEFAULT 0")
    }
}

Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
    .addMigrations(MIGRATION_1_2)
    .fallbackToDestructiveMigrationOnDowngrade() // Dev only — remove in prod
    .build()
```

### 12.3 Network Connectivity Auto-Recovery
```kotlin
class NetworkConnectivityObserver @Inject constructor(
    private val context: Context
) : Flow<Boolean> by callbackFlow({
    val manager = context.getSystemService(ConnectivityManager::class.java)
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = trySend(true).let {}
        override fun onLost(network: Network) = trySend(false).let {}
    }
    manager.registerDefaultNetworkCallback(callback)
    trySend(manager.activeNetwork != null)
    awaitClose { manager.unregisterNetworkCallback(callback) }
})
```

---

## 13. CHECKLIST BEFORE SHIPPING

Before any build is marked release-ready, verify all items below:

### Code Quality
- [ ] Zero lint errors (warnings reviewed and suppressed with justification)
- [ ] ktlint + detekt pass with zero violations
- [ ] No `TODO` / `FIXME` without an associated issue tracker reference
- [ ] No hardcoded strings in UI — all in `strings.xml`
- [ ] No hardcoded colors — all in theme

### Stability
- [ ] All coroutines use structured concurrency (no `GlobalScope`)
- [ ] No `!!` operators in production code
- [ ] All network calls wrapped in try-catch with `AppResult`
- [ ] LeakCanary shows zero leaks on all tested screens
- [ ] StrictMode shows zero violations on main thread
- [ ] App tested with "Don't keep activities" enabled in Developer Options
- [ ] App tested with process death simulation (adb shell am kill)

### Performance
- [ ] Cold start time < 1 second on mid-range device (Pixel 4a equivalent)
- [ ] No dropped frames on main screen scrolling (profiled with Systrace/Perfetto)
- [ ] APK/AAB size within target budget (define per project, typically < 30 MB)
- [ ] Image loading uses Coil with size constraints to prevent OOM

### Security
- [ ] No API keys or secrets in source code or resources
- [ ] `android:allowBackup` explicitly set
- [ ] ProGuard/R8 enabled and rules tested
- [ ] `FLAG_SECURE` on screens with sensitive data

### CI/CD
- [ ] All CI jobs pass on both `main` and `develop` branches
- [ ] ABI splits produce correct version codes
- [ ] Signed AAB uploaded to artifact store
- [ ] Release notes / changelog updated

### Testing
- [ ] Unit test coverage meets minimums (see §6.3)
- [ ] Critical UI flows covered by instrumented or Compose UI tests
- [ ] Tested on API 26 (minimum support) and latest API level
- [ ] Tested in both light and dark mode
- [ ] Tested on both phone and tablet form factors

---

## 14. CRITICAL RULES — NEVER VIOLATE

1. **Never use `GlobalScope`** — always use `viewModelScope`, `lifecycleScope`, or an injected `CoroutineScope`
2. **Never perform I/O on the main thread** — always use `Dispatchers.IO` or `Dispatchers.Default`
3. **Never store `Context` in a `ViewModel`** — use `ApplicationContext` via Hilt if unavoidable
4. **Never expose mutable state from ViewModel** — always `StateFlow`/`LiveData`, never `MutableStateFlow` publicly
5. **Never skip ProGuard in release** — always enable R8 full mode
6. **Never commit secrets to git** — use GitHub Secrets + BuildConfig injection
7. **Never merge a PR that fails CI** — branch protection rules must enforce this
8. **Never use deprecated APIs without a migration plan** — document the timeline
9. **Never skip error states in UI** — every `Loading` must have an `Error` counterpart
10. **Never use `Thread.sleep()` in coroutines** — always use `delay()`
