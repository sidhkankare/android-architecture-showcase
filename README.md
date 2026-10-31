# 🏛️ Android Architecture Patterns Showcase

[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Min SDK](https://img.shields.io/badge/minSdk-24-orange.svg)](https://developer.android.com/about/versions/nougat)
[![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](LICENSE)

> **A comprehensive demonstration of Android architecture patterns through a unified Movie Database application**

This project showcases **four major Android architecture patterns** (MVP, MVVM, MVI, and Clean Architecture) by implementing the same feature set across different modules. Perfect for learning, comparison, and demonstrating architectural expertise.

---

## 🎯 Project Purpose

- **Learn**: Understand how different architectures solve the same problems
- **Compare**: See side-by-side implementations with identical features
- **Practice**: Production-ready code following Android best practices
- **Showcase**: Demonstrate architectural knowledge to potential employers

---

## 🏗️ Implemented Architectures

### 1. MVP (Model-View-Presenter)
```
View ↔ Presenter ↔ Model
```
- **View**: Activity/Fragment (passive, only renders)
- **Presenter**: Business logic + View updates
- **Model**: Data layer (Repository pattern)

**Best for**: Simple apps, legacy code maintenance

### 2. MVVM (Model-View-ViewModel)
```
View → ViewModel → Model
      ← LiveData ←
```
- **View**: Activity/Fragment (observes ViewModel)
- **ViewModel**: Exposes LiveData/StateFlow
- **Model**: Repository + Data sources

**Best for**: Most Android apps, lifecycle-aware logic

### 3. MVI (Model-View-Intent)
```
View → Intent → ViewModel → State → View
```
- **Intent**: User actions (sealed class)
- **State**: Single immutable state object
- **ViewModel**: State machine + side effects

**Best for**: Complex state management, reactive apps

### 4. Clean Architecture (with MVVM)
```
Presentation → Domain → Data
(ViewModel)   (UseCase) (Repository)
```
- **Presentation**: UI + ViewModel
- **Domain**: Use cases (business logic)
- **Data**: Repository + API + Database

**Best for**: Large-scale, testable, maintainable apps

---

## ✨ Features (Implemented in Each Pattern)

### Core Functionality
- 📱 **Movie List**: Browse popular movies from TMDB API
- 🔍 **Search**: Real-time movie search with debouncing
- ⭐ **Favorites**: Offline storage using Room database
- 📖 **Details**: Movie information with poster images
- 🔄 **Pull-to-Refresh**: Update movie list
- 🌙 **Dark Mode**: Full theme support

### Technical Features
- 🌐 REST API integration (Retrofit)
- 💾 Local database (Room)
- 🖼️ Image loading & caching (Coil/Glide)
- ⚡ Asynchronous operations (Coroutines + Flow)
- 🧪 Unit & UI tests (JUnit, Espresso)
- 📦 Dependency Injection (Hilt)
- 🎨 Jetpack Compose + XML views

---

## 🛠️ Tech Stack

| Category | Technologies |
|----------|-------------|
| **Language** | Kotlin 100% |
| **UI** | Jetpack Compose, XML, Material Design 3 |
| **Architecture Components** | ViewModel, LiveData, Lifecycle, Room, Navigation |
| **Dependency Injection** | Hilt (Dagger) |
| **Networking** | Retrofit, OkHttp, Gson |
| **Image Loading** | Coil (Compose), Glide (XML) |
| **Async** | Coroutines, Flow, RxJava (MVP demo) |
| **Testing** | JUnit 5, Mockito, Espresso, Turbine |
| **Code Quality** | Detekt, Ktlint, SonarQube |
| **CI/CD** | GitHub Actions |

---

## 📊 Architecture Comparison

| Feature | MVP | MVVM | MVI | Clean |
|---------|-----|------|-----|-------|
| **Learning Curve** | 🟢 Easy | 🟢 Easy | 🟡 Medium | 🔴 Hard |
| **Testability** | 🟢 Good | 🟢 Good | 🟢 Excellent | 🟢 Excellent |
| **Boilerplate** | 🔴 High | 🟡 Medium | 🟡 Medium | 🔴 Very High |
| **State Management** | 🔴 Poor | 🟡 Good | 🟢 Excellent | 🟢 Excellent |
| **Scalability** | 🔴 Limited | 🟡 Good | 🟢 Great | 🟢 Excellent |
| **Compose Support** | 🔴 Difficult | 🟢 Native | 🟢 Native | 🟢 Native |
| **Lifecycle Aware** | 🔴 Manual | 🟢 Built-in | 🟢 Built-in | 🟢 Built-in |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- TMDB API Key ([Get it free](https://www.themoviedb.org/settings/api))

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/android-architecture-showcase.git
   cd android-architecture-showcase
   ```

2. **Add your TMDB API Key**
   
   Create `local.properties` in the project root:
   ```properties
   sdk.dir=/path/to/your/Android/sdk
   TMDB_API_KEY=your_api_key_here
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the app**
   - Open in Android Studio
   - Select a run configuration:
     - `app` - Main launcher (choose pattern)
     - `feature-mvp` - Run MVP directly
     - `feature-mvvm` - Run MVVM directly
     - `feature-mvi` - Run MVI directly
     - `feature-clean` - Run Clean Architecture directly

---

## 📂 Project Structure

```
android-architecture-showcase/
├── app/                    # Main navigation module
├── feature-mvp/           # MVP implementation
├── feature-mvvm/          # MVVM implementation
├── feature-mvi/           # MVI implementation
├── feature-clean/         # Clean Architecture implementation
├── core/                  # Shared modules
│   ├── data/             # Network + Database
│   ├── domain/           # Business logic
│   └── ui/               # Shared UI components
├── docs/                  # Architecture documentation
└── buildSrc/             # Dependency management
```

See [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) for detailed breakdown.

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [MVP Pattern](docs/architecture/MVP_PATTERN.md) | Model-View-Presenter explained |
| [MVVM Pattern](docs/architecture/MVVM_PATTERN.md) | Model-View-ViewModel guide |
| [MVI Pattern](docs/architecture/MVI_PATTERN.md) | Model-View-Intent walkthrough |
| [Clean Architecture](docs/architecture/CLEAN_ARCHITECTURE.md) | Layered architecture deep-dive |
| [Pattern Comparison](docs/comparison/PATTERN_COMPARISON.md) | Side-by-side analysis |
| [When to Use What](docs/comparison/WHEN_TO_USE.md) | Decision guide |
| [API Setup](docs/setup/API_CONFIGURATION.md) | TMDB API integration |

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### Generate Coverage Report
```bash
./gradlew jacocoTestReport
```

**Test Coverage**: 70%+ (target)

---

## 🎨 Screenshots

| Pattern Selection | MVP | MVVM | MVI | Clean |
|------------------|-----|------|-----|-------|
| ![](screenshots/home.png) | ![](screenshots/mvp_screen.png) | ![](screenshots/mvvm_screen.png) | ![](screenshots/mvi_screen.png) | ![](screenshots/clean_screen.png) |

---

## 🏆 What I Learned / Key Takeaways

### Architecture Insights
- **MVP**: Great for understanding separation of concerns, but high coupling between View and Presenter
- **MVVM**: Android's recommended approach, excellent lifecycle integration
- **MVI**: Best for complex state, unidirectional flow prevents bugs
- **Clean Architecture**: Maximum testability and scalability, worth the boilerplate for large apps

### Android Development
- ✅ Multi-module architecture in practice
- ✅ Jetpack components (ViewModel, Room, Navigation)
- ✅ Dependency injection with Hilt
- ✅ Reactive programming with Coroutines & Flow
- ✅ Testing strategies for each architecture
- ✅ CI/CD pipeline with GitHub Actions

### Best Practices
- ✅ Separation of concerns
- ✅ Single Responsibility Principle
- ✅ SOLID principles in action
- ✅ Repository pattern for data access
- ✅ Error handling & loading states
- ✅ Offline-first approach

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@your-username](https://github.com/your-username)
- LinkedIn: [Your Name](https://linkedin.com/in/your-profile)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- [TMDB API](https://www.themoviedb.org/) for movie data
- [Android Architecture Components](https://developer.android.com/topic/architecture) documentation
- [Google's Architecture Samples](https://github.com/android/architecture-samples)
- The Android community for best practices

---

## 📈 Project Stats

![GitHub stars](https://img.shields.io/github/stars/your-username/android-architecture-showcase?style=social)
![GitHub forks](https://img.shields.io/github/forks/your-username/android-architecture-showcase?style=social)
![GitHub issues](https://img.shields.io/github/issues/your-username/android-architecture-showcase)
![GitHub pull requests](https://img.shields.io/github/issues-pr/your-username/android-architecture-showcase)

---

## 🗺️ Roadmap

- [ ] Add Kotlin Multiplatform support
- [ ] Implement offline-first with WorkManager
- [ ] Add pagination (Paging 3)
- [ ] Integrate Firebase Analytics
- [ ] Add performance monitoring
- [ ] Implement App Shortcuts
- [ ] Add Widget support
- [ ] Create video tutorials

---

**⭐ If you find this project useful, please consider giving it a star!**

