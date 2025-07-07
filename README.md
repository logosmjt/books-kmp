# 📚 Books-KMP

A Kotlin Multiplatform (KMP) demo project for exploring cross-platform app development using **Clean
Architecture**, **JetBrains Compose Multiplatform**, **Ktor**, **SQLDelight**, and **Koin**.

👉 [books-kmp](https://github.com/logosmjt/books-kmp)

---

## ✨ Features

- 🔍 Search books using the Google Books API
- 📚 Display book listings and detailed pages
- 📦 Persist data locally using SQLDelight
- 🧠 Shared business logic across platforms
- 📱 Android and iOS UI powered by Compose Multiplatform
- 🌐 Web support (WASM) in future

---

## 🧱 Architecture

The project follows **Clean Architecture** principles:
```
shared/
├── application/ # Domain models & usecases
├── data/ # Repository, network and database layer
├── presentation/ # ViewModels & UI state management
├── di/ # Dependency injection using Koin
```

### 🔄 Layer Breakdown

- **application**: use cases and domain models
- **data**: fetch from API or DB, map into domain
- **presentation**: Compose UI + ViewModels
- **di**: Koin modules abstract platform-specific dependencies

---

## 🛠️ Tech Stack

| Layer                | Library/Tool                                                                 |
|----------------------|------------------------------------------------------------------------------|
| UI                   | [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) |
| Dependency Injection | [Koin 4.0.0](https://insert-koin.io)                                         |
| Network              | [Ktor 3.1.1](https://ktor.io)                                                |
| Database             | [SQLDelight 2.0.1](https://cashapp.github.io/sqldelight/)                    |
| Image Loading        | [Kamel 1.0.5](https://github.com/alialbaali/Kamel)                           |
| Build                | Kotlin Gradle + CocoaPods                                                    |
| Testing              | Manual test doubles                                                          |

---

## 🌍 Platform Support

| Platform      | Status      | Notes                     |
|---------------|-------------|---------------------------|
| ✅ Android     | Complete    | Uses Compose + ViewModel  |
| ✅ iOS         | Complete    | Integrated via CocoaPods  |
| 🧪 Web (WASM) | In Progress | Compose HTML + wasmJsMain |

---

## 📦 Dependencies

**Shared Libraries:**

- `kotlinx.coroutines`
- `ktor-client-core`, `ktor-client-json`
- `sqlDelight-runtime`, `coroutines-extensions`
- `koin-core`, `koin-compose`
- `compose-runtime`, `compose-material3`, `voyager`

**Android-specific:**

- `ktor-client-okhttp`
- `sqlDelight-android-driver`
- `koin-android`, `androidx.lifecycle.viewmodel`

**iOS-specific:**

- `ktor-client-ios`
- `sqlDelight-native-driver`

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog+
- Kotlin 2.1.20+
- Compose Multiplatform 1.8.0+
- Xcode 15+ (for iOS build)
- CocoaPods installed

### Clone the Repo

```bash
git clone https://github.com/logosmjt/books-kmp.git
cd books-kmp
```

💡 Why Kotlin Multiplatform?
Compared to Flutter or React Native:

✅ Pros:

True code sharing (business logic, models, usecases, even DB)

Full power of platform-specific APIs via expect/actual

Modern UI with Compose

Gradual adoption: you can use KMP incrementally

⚠️ Cons:

Web/WASM support is still young

Some libraries lack wasmNative compatibility

More boilerplate for expect/actual

iOS interop sometimes requires tweaking

