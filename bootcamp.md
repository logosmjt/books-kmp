# Kotlin Multiplatform Bootcamp: 构建一个跨平台图书应用

项目地址：[https://github.com/logosmjt/books-kmp](https://github.com/logosmjt/books-kmp)

---

## 🧭 前言：为什么选择 Kotlin Multiplatform？

Kotlin Multiplatform (KMP) 是 JetBrains 推出的技术栈，支持使用同一套 Kotlin 代码构建 Android、iOS、Web 等平台应用，最大化代码复用的同时保持平台特有体验。

本教程以 `books-kmp` 项目为基础，带你一步步搭建一个支持 Android 和 iOS 的图书信息应用，涵盖：

- KMP 项目结构设计
- Compose Multiplatform UI
- 数据持久化与同步
- API 网络请求
- DI 与 ViewModel 管理
- 多平台构建与部署

---

## 📁 第一章：项目结构与架构设计

```text
books-kmp/
├── androidApp/           // Android UI 层
├── iosApp/               // iOS SwiftUI 项目（使用 CocoaPods）
├── shared/               // Kotlin Multiplatform shared 模块
│   ├── src/commonMain/   // 公共业务逻辑（model, usecase, repo, viewmodel）
│   ├── src/androidMain/  // Android 特定实现（DB, Ktor 等）
│   ├── src/iosMain/      // iOS 特定实现
├── build.gradle.kts
└── libs.versions.toml
```

使用 **Clean Architecture**，模块划分如下：

- **application/**: 业务模型、usecase
- **data/**: repository、datasource、service（API）
- **presentation/**: ViewModel 与 UI 层
- **di/**: Koin 依赖注入模块

---

## 🔌 第二章：配置 Kotlin Multiplatform 模块

### 1. 支持平台

```kotlin
kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    // wasmJs() ← 本项目暂不启用 Web
}
```

### 2. KMP 必备依赖

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

### 3. Android/iOS 特定实现

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

## 🌐 第三章：接入 Google Books API

使用 [Google Books API](https://developers.google.com/books) 构建网络服务层。

- 使用 `Ktor` 构建 `BookService`
- 使用 `Result` 封装成功/失败状态
- 通过 `BookRepository` 调用远程或本地数据

---

## 💾 第四章：SQLDelight 多平台持久化

### 1. 配置数据库

```kotlin
sqldelight {
    databases {
        create("BooksDatabase") {
            packageName.set("com.simple.books.db")
        }
    }
}
```

### 2. 数据库 Driver

- Android 使用 `AndroidSqliteDriver`
- iOS 使用 `NativeSqliteDriver`

```kotlin
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
```

---

## 🧠 第五章：构建 ViewModel 与状态管理

### 1. ViewModel 示例

```kotlin
class ListingViewModel(
    private val listingUseCase: ListingUseCase,
    private val queryUseCase: QueryUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val state: StateFlow<ListingState>
}
```

### 2. 依赖注入

```kotlin
val viewModelModule = module {
    single { Dispatchers.Default }
    singleOf(::ListingViewModel)
    factory { (id: String) -> DetailViewModel(id, get()) }
}
```

---

## 🎨 第六章：使用 Compose 构建 UI

### common:

- navigation 提供屏幕导航（多平台）

```kotlin
@Composable
fun App() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
```
### android:

- 在MainActivity添加App()

### iOS:

- 使用 SwiftUI 嵌套 `UIViewControllerRepresentable` 调用 Kotlin UI

---

## 🔁 第七章：数据同步策略

1. 首次启动优先从本地 DB 加载
2. 无数据时拉取远程并缓存
3. 强制刷新覆盖缓存

---

## 🧪 第八章：测试

- 手动实现 `FakeBookService`
- 单元测试 `BookRepository` 和 `UseCase`

---

## 🚀 第九章：构建与部署

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

## 🪤 第十章：踩坑记录

- wasmJs 支持还需等待和完善

---

## 📌 结语：下一步计划

- 支持Web（wasmJs）

---

本教程同步开源于：[https://github.com/logosmjt/books-kmp](https://github.com/logosmjt/books-kmp)

如果你觉得这个 Bootcamp 有帮助，欢迎点个 ⭐ Star、提个 Issue 或 PR！

💬 有任何问题，也欢迎留言讨论或通过 issue 与我交流。

Happy Multiplatform Coding! 🚀
