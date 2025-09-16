# SmartBox API

## Overview
This API was part of my final-year project in secondary school.  
It demonstrates my programming skills prior to starting formal studies.  
I learned Spring Boot entirely on my own through YouTube tutorials and Udemy courses.

The API provides basic user and authority management and is intended for demonstration purposes only. It is **not production-ready**.

## Prerequisites
- Java 8+
- Maven 3.6+
- PostgreSQL database

## Setup & Usage

1. **Update configuration**
    - Edit `application.properties` to provide the correct database connection information.

2. **Install dependencies:**
   ```bash
   mvn install
   ```
3. **Run the API**
    ```bash
    mvn spring-boot:run
    ```
4. **Manual database setup**
- Add a user to the users table with a password generated using encoder_test.
   (NOT SAFE OR BEST PRACTICE — DO NOT USE IN A REAL-WORLD APPLICATION)
- Add roles to the authorities table (ROLE_USER and ROLE_ADMIN).

5. Test the API
Visit the endpoint:
```
http://localhost:8080/user/showUser
```
  - When using `curl`, include HTTP Basic Authentication header.
  - When using a browser, login credentials will be requested.

The endpoint will return the user data in JSON format.

## Known Limitations
- When a user changes their password, it is sent to the Raspberry Pi through the WebSocket. Using a private key on the Raspberry Pi would be a more secure approach.  
- The current WebSocket implementation is not ideal and limits scalability, especially with many devices.  
- Authentication practices are not secure by modern standards.  
- Using session keys, cookies, or token-based authentication would improve security.  
- Messages are not persisted, so offline devices may miss updates.

## Disclaimer
This project was part of my final-year project in secondary school.
It was created before I formally learned Java programming, and everything was self-taught through online resources.
It reflects what I learned at the time, but does not follow current best practices in security or software design.
It is published here for demonstration purposes.