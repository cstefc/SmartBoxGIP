# Admin Program

## Intro
This program provides a simple interface to manage the users and authorities in the database. 
The goal is to be used in a demo not in production, keep this in mind when reviewing.
It allows someone with the admin role to see all the users and change their password if needed.

## Prerequisites
- Running API + database see [api](https://github.com/cstefc/gip/tree/main/SmartBoxApi)
- An admin account in the database
- Maven
- Java

## Usage

1. Update the api link in the Request class if it runs on another location

2. Install dependencies
```
mvn install
```
3. Run the program
```
mvn javafx:run
```

## Known limitations
### Initialisation
One of the pitfalls of this approach is that the program itself needs an admin user in the database.
This user isn't made beforehand so the overall setup of the project isn't as streamlined as it could be.

### Security
For simplicity certificate validation is disabled in the prototype.
In this prototype, admins can both reset and view the new password. 
These practices would not be acceptable in a production system.

### Maintainability
Lacks search and grouping functionality which makes using it with a larger userbase unproductive.

## Disclaimer
This project was part of my final-year project in secondary school. 
It was created before I formally learned Java programming, and everything was self-taught through online resources.
It reflects what I learned at the time, but does not follow current best practices in security or software design. 
It is published here for demonstration purposes.