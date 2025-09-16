# RaspberryWebsocketClient

## Intro
This client runs on a Raspberry Pi and opens a WebSocket connection with the API to interpret updates.  
The Raspberry Pi is connected to an Arduino, which handles the lights and door lock. The Raspberry Pi acts as a middleman, 
sending instructions over a serial connection. It also handles music playback.

The program has not been tested, and the usage instructions are conceptual because the physical mockup is no longer available.  
Additionally, the program that interprets the serial connection has been lost.

## Prerequisites
- Running API + database (see [API](https://github.com/cstefc/gip/tree/main/SmartBoxApi))
- An account in the database

## Usage
1. Update `settings.properties` with the correct login credentials.
2. Update `WebsocketClient.class` to include the correct WebSocket URL and USB port.
3. Update `MusicPlayer` to point to the correct music file location.
4. Install dependencies:
```bash
   mvn install
```
5. Run the application
``` bash
    mvn run
```

## Know limitations
- Username and password are saved in plain text.
- The Arduino is required; a better approach would be to use the Raspberry Pi’s GPIO pins for physical control.
- Java is not the most efficient choice for Raspberry Pi applications.
- Music files must be present on the Raspberry Pi manually; a Samba or network share would improve usability.
- Overall security is weak in this part of the application.

## Disclaimer
This project was part of my final-year project in secondary school.
It was created before I formally learned Java programming, and everything was self-taught through online resources.
It reflects what I learned at the time, but does not follow current best practices in security or software design.
It is published here for demonstration purposes.