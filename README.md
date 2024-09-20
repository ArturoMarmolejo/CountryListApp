# Country App

This is a simpleAndroid application that displays a list of countries fetched from a public API.

## Features

* Displays a list of countries with their names, capitals, and region.
* Uses coroutines for asynchronous operations.
* Built with traditional XML views for the user interface.

## API

The app uses the following publicAPI to fetch the list of countries:

[https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json](https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json)

## Screenshots

<img src="https://github.com/ArturoMarmolejo/CountryListApp/blob/master/app/src/main/res/drawable/screenshot_1.png" width="300">

## Architecture

The app follows a simple Model-View-ViewModel (MVVM) and Clean Architecture principles without any dependency injection framework.

* **Data:** Represents the country data.
* **Presentation:** Displays the list of countries using a `RecyclerView`.


## Libraries

* Kotlin Coroutines
* Retrofit
* RecyclerView
* Viewmodel
* Lifecycle
* Junit and Mockk for Unit Testing

## How to run

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or device.

## Future improvements

* Implement a search feature to filter countries.
* Add detailed information screens for each country.
* Use a dependency injection framework like Dagger/Hilt.
* Implement a Domain Layer with use cases if we have more functionalities/repository methods to add
* Add SlidingPaneLayout in case we have a detail screen to display, for bigger devices
* Use Jetpack Navigation in case we have more screens to display
* Migrate to Jetpack Compose since it is the recommended approach for new applications
