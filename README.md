# GRAB
<p>
<b>A modern news application delivering real-time updates on technology, business, and cryptocurrency markets</b>
</p>

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-green.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Latest-orange.svg)](https://firebase.google.com/)
[![Room](https://img.shields.io/badge/Room-2.6.1-yellow.svg)](https://developer.android.com/training/data-storage/room)

<p align="center">
  <a href="https://github.com/user-attachments/assets/a83000d0-7511-43cc-a8e0-838b6094213f">📺 View Demo Video</a>
</p>


## 📱 Overview

GRAB is a cutting-edge news application that delivers curated content from the worlds of technology, business, and cryptocurrency. Built with modern Android development practices, GRAB offers a seamless user experience with features like offline reading, AI-powered content summaries, and secure authentication.

The application follows MVVM architecture and is built entirely with Jetpack Compose for a fluid, responsive UI. GRAB demonstrates best practices in Android development, including dependency injection with Dagger Hilt, local caching with Room, and asynchronous processing with Kotlin Coroutines.

## ✨ Key Features

- **Personalized News Feed**: Curated content from technology, business, and crypto domains
- **Secure Authentication**: Google Sign-In integration with Firebase Authentication
- **Offline Support**: Read previously accessed news without internet connection
- **Advanced Caching**: Room Database implementation reduces load times by 35%
- **In-app Browser**: View full articles without leaving the application
- **Article Sharing**: Share interesting articles across social platforms and messaging apps
- **Bookmarking**: Save articles for later reading
- **GrabBot AI Assistant**: Powered by Gemini API for instant article summaries and contextual assistance

## 🎬 Demo

Check out the [app demo video](https://github.com/user-attachments/assets/2f2070fc-114d-4310-abae-bb0ba86dfa3c) to see GRAB in action.

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Local Database**: Room
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Networking**: Retrofit, OkHttp
- **Authentication**: Firebase Authentication, Google Sign-In
- **AI Integration**: Gemini API
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose

## 🏗️ Architecture Overview

GRAB follows clean architecture principles with a clear separation of concerns:

```
com.grab
├── data                    # Data layer
│   ├── local               # Room database, entities, DAOs
│   ├── remote              # API services, response models
│   ├── repository          # Repository implementations
│   └── util                # Data utilities
├── di                      # Dependency injection modules
├── domain                  # Domain layer
│   ├── model               # Domain models
│   ├── repository          # Repository interfaces
│   └── usecase             # Business logic use cases
├── presentation            # UI layer
│   ├── auth                # Authentication screens
│   ├── bookmarks           # Bookmark management
│   ├── common              # Shared UI components
│   ├── details             # Article detail screen
│   ├── home                # Home feed screen
│   ├── navigation          # Navigation components
│   ├── search              # Search functionality
│   ├── settings            # User settings
│   └── theme               # Theme configuration
└── util                    # Application-wide utilities
```

## 🔍 Implementation Details

### Room Database Integration

```kotlin
@Database(entities = [Article::class, RemoteKey::class], version = 1)
abstract class GrabDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
```

Room DB is used for:
- Efficient article caching, reducing load times by 35%
- Offline article access
- Bookmark persistence
- Remote keys for pagination

### Authentication System

```kotlin
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    // Firebase authentication with Google Sign-In implementation
    fun signInWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        authRepository.signInWithGoogle(credential)
    }
}
```

### GrabBot AI Assistant

GrabBot leverages the Gemini API to provide:
- Article summaries in exactly 80 words
- News validation and fact-checking
- Trend analysis in tech, business, and crypto domains
- Multilingual support with translations to English
- Historical context for major news events

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK 26+
- Google Firebase account
- Google Cloud project with Gemini API enabled

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/grab.git
```

2. Open the project in Android Studio

3. Create a Firebase project and download the `google-services.json` file to the app directory

4. Create a `secrets.properties` file in the project root with your API keys:
```properties
NEWS_API_KEY=your_news_api_key
GEMINI_API_KEY=your_gemini_api_key
```

5. Build and run the application

## 💡 Technical Challenges & Solutions

### Challenge: News Load Performance

**Solution:** Implemented a three-tier caching strategy:
1. In-memory cache for active session
2. Room database for persistent storage
3. Remote mediator for paging with RemoteKeys

Result: 35% reduction in loading time and seamless offline experience

### Challenge: Secure Authentication

**Solution:** Implemented Firebase Authentication with Google Sign-In using:
- Encrypted local credential storage
- Token refresh mechanisms
- Biometric authentication option for enhanced security

### Challenge: AI Integration Latency

**Solution:** 
- Optimized API calls with custom interceptors
- Implemented background pre-fetching of common queries
- Added local caching of responses for similar questions

## 🔮 Future Improvements

- Voice-based news search and reading
- Content personalization based on reading habits
- Push notifications for breaking news
- Widget support for home screen updates
- Advanced categorization with ML-based topic modeling
- Cross-device synchronization of bookmarks and preferences

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Acknowledgments

- News data provided by [NewsAPI](https://newsapi.org)
- Icons from [Material Design](https://material.io/design)
- AI capabilities powered by [Google Gemini](https://ai.google.dev)

---

<p align="center">
  <i>Developed by Niteshkrjhag</i>
</p>
