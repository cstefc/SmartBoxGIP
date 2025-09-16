# SmartBox Android App

## Intro
This Android app communicates with the SmartBox API to control music, lights, locks, and alarms.  
It serves as the frontend for the SmartBox project and is currently the only way to control the lights from a mobile device.

Note that the security in this prototype is limited and should be improved before any real-world use.

## Prerequisites
- Android Studio
- Android device or emulator
- Running [SmartBox API](https://github.com/cstefc/gip/tree/main/SmartBoxApi)
- A user account with `ROLE_USER`

## Usage
1. Update the API link in `Request.java` if it runs on another location.
2. Open the project in Android Studio.
3. Run the app on a connected device or emulator.

## Known Limitations
- The connection with the API uses deprecated methods.
- Passwords are stored directly in local storage **without encryption**.
- The overall user experience (UX) could be improved.

## Disclaimer
This project was part of my final-year project in secondary school.
It was created before I formally learned Java programming, and everything was self-taught through online resources.
It reflects what I learned at the time, but does not follow current best practices in security or software design.
It is published here for demonstration purposes.