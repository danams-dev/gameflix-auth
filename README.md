# Module 4, Part 1 - GameFlix Authentication Service
### Author: Dana Santine
### Instructor: Dr. Alan Peslak
### Course: HCDD 412 - Complex Software Systems
### Date: 10/12/2026

This project implements user registration, login, JWT-based authentication, and a protected user profile endpoint
for the GameFlix application.

## How to Run the Project
- This project uses Java 21, Spring Boot, Spring Security, Spring Data JPA, and MySQL.

1. Make sure MySQL is installed and running.
2. Make sure a MySQL database named `gameflix_auth` exists.
3. Configure a MySQL user with permission to access the `gameflix_auth` database. My local setup uses the database 
   user `gameflix_app`, but other credentials can be used.
4. In `src/main/resources`, create an `application-local.properties` file.
5. Add the database connection information and JWT settings to `application-local.properties`.
6. Use `application.properties.example` as a reference for the required configuration keys.
7. Run the Spring Boot application from IntelliJ.
8. The API runs locally at `http://localhost:8080` when `GameflixAuthApplication` is running.

## Database Schema
The `users` table was created from the `User` entity using Spring Data JPA and Hibernate.

CREATE TABLE `users` (
`user_id` bigint NOT NULL AUTO_INCREMENT,
`created_at` datetime(6) NOT NULL,
`email` varchar(255) NOT NULL,
`name` varchar(100) NOT NULL,
`password_hash` varchar(255) NOT NULL,
`role` varchar(50) NOT NULL,
`subscription_status` varchar(50) NOT NULL,
PRIMARY KEY (`user_id`),
UNIQUE KEY `uk_users_email` (`email`)
);

## Password Hashing
- Passwords are hashed using bcrypt through Spring Security's `DelegatingPasswordEncoder`.
- The bcrypt work factor is 10, which is the default used by Spring Security and meets the assignment's minimum 
  recommendation.
- I chose bcrypt because it is an adaptive password hashing algorithm that is designed to be slow to make password 
  guessing more difficult than using a faster general-purpose hash.
- The password field is limited to a maximum of 64 characters and avoids bcrypt's 72-byte input limit.

## Database Table Management
- This project uses `spring.jpa.hibernate.ddl-auto=update`.
- The `update` setting was used so Hibernate can create or update the database table structure without deleting 
  existing data between application runs. This was useful for testing because registered users remained in the database
  while the project was being developed.

## JWT Configuration
- JWTs are signed using HS256 and expire after 3600 seconds (1 hour).
- A one-hour expiration time was chosen so users do not have to log in constantly, while still limiting the amount of
  time a stolen token would remain valid.
- The JWT signing secret is stored outside the committed source code in `application-local.properties`.

## Test Evidence
- The API was tested using Postman. 
- The Postman collection was exported as JSON, and screenshots were taken for each required test case.

The following were tested:
1. Successful registration returned `201 Created`.
2. Duplicate email registration returned `409 Conflict`.
3. Successful login returned `200 OK` with a signed JWT.
4. Login with an incorrect password returned `401 Unauthorized`.
5. `GET /api/users/me` with a valid JWT returned `200 OK` and the authenticated user's profile.
6. `GET /api/users/me` with a tampered JWT returned `401 Unauthorized`.

A MySQL query was also used to verify that passwords are stored as bcrypt hashes rather than plain text.

## Module 3 Design Changes
My Module 3 design used different endpoint names for user creation, login, and profile access.
For this assignment, the authentication routes were updated to match the Module 4 requirements.

**Implemented Routes:**
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`

The User model stayed consistent with my Module 3 ERD. It still uses the same main fields such as name,
email, role, password hash, subscription status, and account creation date.

## AI Usage and Change Log
### 1. AI Usage
I used ChatGPT to help clarify and explain certain Spring Boot and Spring Security concepts, troubleshoot errors, and 
assist with parts of the registration, login, password hashing, JWT authentication, and endpoint implementation.

**Prompts Used:**
- explain what specific Spring Security code was doing line by line
- help me check whether my JWT generation and validation were correct
- how to store secrets outside of committed source code
- how to test successful and failed authentication requests in Postman
- asked for clarification on concepts such as DTOs, JPA, bcrypt, JWT claims, and protected routes

The generated output included code examples, troubleshooting suggestions, explanations of Spring Security classes and
annotations, and guidance for testing the API.

### 2. Changes Made
I reviewed and modified AI-generated suggestions before adding them to my project.
One change involved the login response. An early version returned only `"Login successful"` after valid credentials 
were verified, and I had to change this so the login endpoint generated and returned a signed JWT because the assignment
required a token with an expiration.

I also kept the implementation simpler in certain areas rather than adding classes that seemed unnecessary. I used
the existing `AuthService` for authentication and token generation, and used a simple JSON map for the token response 
instead of adding additional response classes.

Another issue was that the initial security configuration did not yet allow the public registration and login routes,
which caused a `401 Unauthorized` response during testing. I updated the `SecurityFilterChain` so `/api/auth/register`
and `/api/auth/login` were public while other routes remained protected.

During the final testing stage, I realized that invalid registration input was returning `401 Unauthorized` instead of
the required `401 Bad Request`. I updated the security configuration to allow Spring's /error route so validation errors
could return the correct 400 response.

ChatGPT did not always provide the complete final implementation on the first attempt, so I had to verify its
suggestions against the assignment details, test each endpoint multiple times, and adjust the code when its behavior
did not match what was required.

### 3. Reflection
ChatGPT was helpful for this assignment because it gave me explanations and examples for Spring Security concepts that
were new to me, such as JWT authentication, password hashing, and security configuration.

What I found the most useful was being able to ask for step-by-step explanations while building the project so I could 
understand what the code was doing instead of just copying and pasting without understanding.

I still needed to test each part of my code and compare AI's suggestions with the assignment requirements. 
Some suggestions needed to be changed or completed by me before they actually worked correctly.

Next time, I would review the full security requirements earlier and plan the authentication flow before implementing 
each part. I would still use AI for explanations and troubleshooting, but I would continue comparing the generated 
code against my own knowledge and testing, as well as the course materials.