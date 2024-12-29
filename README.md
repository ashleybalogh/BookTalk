# BookTalk

BookTalk is a social platform for book lovers, combining TikTok-style content consumption with Goodreads-like features and book club functionalities.

## Features

- Book Discovery Feed
- Social Feed with TikTok-style vertical scrolling
- Reading Management
- E-Reader Integration
- Audiobook Features
- Community Engagement (Book Clubs & Buddy Reads)

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit
- **Async Operations**: Coroutines & Flow
- **Image Loading**: Coil
- **Testing**: JUnit, Mockk, Turbine
- **Static Analysis**: ktlint, detekt

## Project Setup

### Requirements
- Android Studio Hedgehog | 2023.1.1
- JDK 17
- Android SDK 34
- Kotlin 1.9.10

### Building the Project
1. Clone the repository
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Run the app using the 'app' configuration

### Running Tests
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run ktlint
./gradlew ktlintCheck

# Run detekt
./gradlew detekt
```

## Architecture

The project follows Clean Architecture principles with MVVM pattern:

```
com.booktalk
├── core
│   ├── di        # Dependency Injection
│   ├── util      # Utilities
│   └── ui        # Common UI Components
├── data
│   ├── local     # Local Storage
│   └── remote    # Network
├── domain
│   ├── model     # Domain Models
│   └── repository # Repository Interfaces
└── features      # Feature Modules
    └── base      # Base Components
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
