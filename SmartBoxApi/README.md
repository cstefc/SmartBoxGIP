# SmartBox

## Intro
This API was part of my GIP (geïntegreerde proef). 
It is meant to be an example of my programming skills before I started my studies.
I hadn't had any lessons on Spring Boot and learned myself this trough Youtube tutorials and Udemy.

## Prerequisites
- Java 8+
- Maven 3.6+
- PostgreSQL database

## Installation + execution

1. **Update `application.properties` file to correct information about the database.**
2. **Install the dependencies**
```
mvn install
```
3. **Run the API**
```
mvn spring-boot:run    
```
4. **Manual database setup**
- Add user to users table with a password generated using the encoder_test -> (NOT SAFE OR BEST PRACTICE: DO NOT USE IN REAL WORLD APPLICATION)
- Add roles to authorities table (`ROLE_USER` and `ROLE_ADMIN`)

You can test if everything works by going to following link
```
http://localhost:8080/user/showUser
```

The endpoint will return the user data in json format.