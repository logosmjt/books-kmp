
# Kotlin Multiplatform Bootcamp: Building a Cross-Platform Book App

GitHub Repository: [https://github.com/logosmjt/books-kmp](https://github.com/logosmjt/books-kmp)

---

## 🧭 Introduction: Why Kotlin Multiplatform?

Kotlin Multiplatform (KMP), developed by JetBrains, enables developers to share common code across Android, iOS, Web, and more — maximizing code reuse while still allowing platform-specific experiences.

This tutorial is based on the `books-kmp` project, guiding you through building a book browsing app that supports both Android and iOS. You'll learn:

- KMP project structure design
- Compose Multiplatform UI development
- Data persistence and synchronization
- API network integration
- Dependency Injection and ViewModel setup
- Cross-platform building and deployment

---

## 📁 Chapter 1: Project Structure & Architecture

```text
books-kmp/
├── androidApp/           // Android UI layer
├── iosApp/               // iOS SwiftUI project (using CocoaPods)
├── shared/               // Kotlin Multiplatform shared module
│   ├── src/commonMain/   // Shared business logic (models, usecases, repositories, viewmodels)
│   ├── src/androidMain/  // Android-specific implementations (DB, Ktor, etc.)
│   ├── src/iosMain/      // iOS-specific implementations
├── build.gradle.kts
└── libs.versions.toml
```

The project follows **Clean Architecture**, with the following layers:

- **application/**: business models and usecases
- **data/**: repositories, datasources, services (APIs)
- **presentation/**: ViewModels and UI components
- **di/**: Koin dependency injection modules

---

## 🔌 Chapter 2: Configuring the Multiplatform Module

### 1. Target Platforms

```kotlin
kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    // wasmJs() ← Web not enabled yet
}
```

### 2. Common Dependencies

```kotlin
commonMain.dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.sql.coroutines.extensions)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(libs.bundles.ktor.common)
    implementation(libs.bundles.kamel.common)
    implementation(libs.navigation.compose)
    ...
}
```

### 3. Platform-Specific Dependencies

```kotlin
androidMain.dependencies {
    implementation(libs.ktor.client.okHttp)
    implementation(libs.sql.android.driver)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.bundles.kamel.android)
}

iosMain.dependencies {
    implementation(libs.ktor.client.ios)
    implementation(libs.sql.native.driver)
}
```

---

## 🌐 Chapter 3: Integrating Google Books API

We use the [Google Books API](https://developers.google.com/books) to build a network service layer:

- Implement `BookService` using `Ktor`
- Access remote or local data via `BookRepository`

---

## 💾 Chapter 4: Multiplatform Persistence with SQLDelight

### 1. Database Setup

```kotlin
sqldelight {
    databases {
        create("BooksDatabase") {
            packageName.set("com.simple.books.db")
        }
    }
}
```

### 2. Platform-Specific Drivers

- Android uses `AndroidSqliteDriver`
- iOS uses `NativeSqliteDriver`

```kotlin
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
```

---

## 🧠 Chapter 5: ViewModel & State Management

### 1. ViewModel Example

```kotlin
class ListingViewModel(
    private val listingUseCase: ListingUseCase,
    private val queryUseCase: QueryUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val state: StateFlow<ListingState>
}
```

### 2. Dependency Injection with Koin

```kotlin
val viewModelModule = module {
    single { Dispatchers.Default }
    singleOf(::ListingViewModel)
    factory { (id: String) -> DetailViewModel(id, get()) }
}
```

---

## 🎨 Chapter 6: Building UI with Compose

### common:

- navigation: navigation-compose

```kotlin
@Composable
fun App() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
```

### android:

- Add App() in MainActivity

### iOS:

- Uses SwiftUI with `UIViewControllerRepresentable` to host Kotlin UI

---

## 🔁 Chapter 7: Data Synchronization Strategy

1. Load from local database on first launch
2. If empty, fetch from remote and cache locally
3. Manual refresh will override the cache

---

## 🧪 Chapter 8: Testing

- Manually implement `FakeBookService`
- Write unit tests for `BookRepository` and `UseCases`

---

## 🚀 Chapter 9: Build & Deployment

### Android:

```bash
./gradlew :androidApp:assembleDebug
```

### iOS:

```bash
cd iosApp
pod install
open iosApp.xcworkspace
```

---

## 🪤 Chapter 10: Lessons Learned

- dependencies version management

---

## 📌 What's Next

- wasmJs support

---

This tutorial is open-sourced at: [https://github.com/logosmjt/books-kmp](https://github.com/logosmjt/books-kmp)

If you find this Bootcamp helpful, please ⭐ star the repo, submit issues, or send a PR!

💬 Feel free to start a discussion or ask questions via issues.

Happy Multiplatform Coding! 🚀
