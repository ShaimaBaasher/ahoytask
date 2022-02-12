# Weather APP
A sample android app that shows how to use ViewModels and Room together with Coroutine with Flow & HILT, in Kotlin by Clean Architecture.

## Implemented by Clean Architecture
The following project is structured with 3 layers:

- Presentation
- Domain
- Data

## Features
- Kotlin based android application .
- UI calls method from ViewModel.
- ViewModel executes Use case
- Each Repository returns data from a Data Source (Cached or Remote) .
- Rerofit for networking .
- HILT for Dependency injection .
- Jetpack compose navigation component

## Scenario
Used http://api.openweathermap.org as a public api 

## At a glance

    get a list of Weather from api .
    show list of next days info .
    user save weather on favourite list locally.
    In the Item of each Weather, showed Weather name, temperature, humidity.
    When user taps on favourite, new page will be show weather details.
    user can switch from celsius to fahrenheit and vice versa..
    And:
        Supported offline mode

## TODO
- Add Timer for notifications
- fix room duplicate data
