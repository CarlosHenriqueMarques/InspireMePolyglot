# InspireMe Polyglot

A multilingual motivational phrase app that helps you stay inspired in multiple languages.

## Features

- Display motivational phrases in multiple languages (English, Portuguese, French, Spanish)
- Select multiple languages to view phrases simultaneously
- Daily notifications to remind you to check your motivational phrase
- Manual refresh to generate new phrases
- Share phrases via WhatsApp, Instagram, or other apps
- AdMob integration for banner ads

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Update the AdMob app ID in `AndroidManifest.xml` with your own ID
4. Update the banner ad unit ID in `PhrasesScreen.kt` with your own ad unit ID
5. Build and run the project

## Requirements

- Android Studio Arctic Fox or newer
- Android SDK 21 or higher
- Kotlin 1.8.0 or higher

## Dependencies

- Jetpack Compose for UI
- ViewModel for state management
- Coroutines for asynchronous operations
- AdMob for advertisements
- Gson for JSON parsing

## Project Structure

- `data/`: Contains data models and repository
- `ui/`: Contains UI components and ViewModel
- `notifications/`: Contains notification-related code
- `assets/`: Contains the JSON file with motivational phrases

## Contributing

Feel free to submit issues and enhancement requests! 