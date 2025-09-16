# SmartBox Project

## Intro
The SmartBox project was created my final-year secondary school work.  
It is a prototype smart home system where users can control lights, locks, alarms, and music via a mobile app.  
The project demonstrates my early programming skills, including Java, Spring Boot, Android development, and basic IoT integration.  
All components are self-taught and intended for demonstration purposes only.

## Full Project
The SmartBox system consists of four main components that work together:

1. **SmartBox API**  
   Handles user authentication, stores data in a PostgreSQL database, and provides endpoints for device control.

2. **SmartBox Android App**  
   Serves as the frontend interface for users to send commands and view device states.

3. **RaspberryWebsocketClient**  
   Runs on a Raspberry Pi, receiving commands from the API via WebSocket and sending them to an Arduino to control lights, locks, and alarms.  
   It also handles music playback.

4. **Admin Program**  
   Provides a simple interface for admins to manage users and their authorities. Allows viewing all users and changing their passwords.  

**Data Flow:** Commands originate from the Android app, are sent to the API, then relayed to the Raspberry Pi, which forwards instructions to the Arduino. 
Device states and updates are sent back to the app through the API or WebSocket.

## Modules
- **[SmartBox API](./SmartBoxApi/README.md):** Provides backend services, manages users and authorities, and exposes endpoints for controlling devices.  
- **[SmartBox Android App](./SmartBoxAndroidApp/README.md):** Mobile interface for users to interact with the SmartBox system.  
- **[RaspberryWebsocketClient](./RaspberryWebsocketClient/README.md):** Connects to the API via WebSocket, relays commands to Arduino, and controls music playback.  
- **[Admin Program](./AdminProgram/README.md):** Allows administrators to manage users and authorities in the database for demonstration purposes.  

## Disclaimer
This project was created as part of my final-year secondary school work.  
It reflects what I learned at the time and is entirely self-taught.  
It does not follow current best practices in security or software design and is published here purely for demonstration purposes.
