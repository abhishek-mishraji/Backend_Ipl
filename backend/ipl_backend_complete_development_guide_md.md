# IPL Progressive Project - Complete Backend Development Guide

## Project Overview

This project is a complete IPL (International Premier League) Management Backend System.

The backend will support:

- User Registration/Login
- JWT Authentication
- Team Management
- Cricketer Management
- Match Management
- Voting System
- Ticket Booking
- Role Based Access
- Spring Security
- CRUD APIs
- Database Relationships

---

# Tech Stack

- Java
- Spring Boot
- Gradle Kotlin DSL
- Spring Data JPA
- Spring Security
- JWT
- H2 File Database
- Lombok

---

# Dependencies To Add

Add these dependencies from Spring Initializr:

- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- H2 Database
- Lombok

---

# Database Configuration

## application.properties

```properties
spring.application.name=ipl-project

spring.datasource.url=jdbc:h2:file:./data/ipldb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# H2 Console

Open:

```text
http://localhost:8080/h2-console
```

Use JDBC URL:

```text
jdbc:h2:file:./data/ipldb
```

---

# Final Backend Folder Structure

```text
src/main/java/com/ipl/

│
├── config/
│       Configurations.java
│       SecurityConfig.java
│
├── controller/
│       TeamController.java
│       CricketerController.java
│       MatchController.java
│       VoteController.java
│       TicketBookingController.java
│       UserLoginController.java
│
├── dto/
│       SignUp.java
│       SignUpResponse.java
│
├── entity/
│       Team.java
│       Cricketer.java
│       Match.java
│       Vote.java
│       TicketBooking.java
│       User.java
│
├── exception/
│       TeamAlreadyExistsException.java
│       TeamDoesNotExistException.java
│       TeamCricketerLimitExceededException.java
│       NoMatchesFoundException.java
│
├── repository/
│       TeamRepository.java
│       CricketerRepository.java
│       MatchRepository.java
│       VoteRepository.java
│       TicketBookingRepository.java
│       UserRepository.java
│
├── security/
│       JwtUtil.java
│       JwtRequestFilter.java
│
├── service/
│       TeamService.java
│       CricketerService.java
│       MatchService.java
│       TicketBookingService.java
│
├── service/impl/
│       TeamServiceImplJpa.java
│       CricketerServiceImplJpa.java
│       MatchServiceImplJpa.java
│       VoteServiceImpl.java
│       TicketBookingServiceImpl.java
│       UserLoginServiceImpl.java
│
└── IplApplication.java
```

---

# Resources Folder Structure

```text
src/main/resources/
│
├── application.properties
```

---

# Development Strategy

Develop one feature completely before moving to the next feature.

## Correct Development Flow

```text
Entity
  ↓
Repository
  ↓
Service Interface
  ↓
Service Implementation
  ↓
Controller
  ↓
Test APIs
  ↓
Move to next feature
```

---

# COMPLETE BACKEND ROADMAP

---

# PHASE 1 — PROJECT SETUP

## Goal

Setup Spring Boot project properly.

## Tasks

- Create Spring Boot project
- Add dependencies
- Configure H2 file database
- Disable default Spring Security login temporarily
- Run application
- Open H2 console

---

# Disable Spring Security Login Temporarily

## SecurityConfig.java

```java
package com.ipl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
```

---

# PHASE 2 — AUTHENTICATION MODULE

## Goal

Complete:

- Registration
- Login
- JWT Authentication
- User Management

---

# Files

## entity/

```text
User.java
```

## repository/

```text
UserRepository.java
```

## dto/

```text
SignUp.java
SignUpResponse.java
```

## service/impl/

```text
UserLoginServiceImpl.java
```

## controller/

```text
UserLoginController.java
```

## security/

```text
JwtUtil.java
JwtRequestFilter.java
SecurityConfig.java
```

---

# User Entity Fields

```text
userId
fullName
username
password
email
role
```

---

# Authentication APIs

## Register

```http
POST /user/register
```

Request Body:

```json
{
  "fullName": "Abhishek",
  "username": "abhi",
  "password": "Password123",
  "email": "abhi@gmail.com",
  "role": "USER"
}
```

---

## Login

```http
POST /user/login
```

Request Body:

```json
{
  "username": "abhi",
  "password": "Password123"
}
```

---

# Login Response

```json
{
  "token": "eyJhbGciOiJIUzI1Ni...",
  "roles": "USER",
  "userId": 1
}
```

---

# JWT Flow

```text
User Login
     ↓
Validate Credentials
     ↓
Generate JWT Token
     ↓
Frontend Stores Token
     ↓
Every Request Sends Token
     ↓
Backend Validates Token
```

---

# PHASE 3 — TEAM MODULE

## Goal

Complete Team CRUD using Spring Data JPA.

---

# Files

## entity/

```text
Team.java
```

## repository/

```text
TeamRepository.java
```

## service/

```text
TeamService.java
```

## service/impl/

```text
TeamServiceImplJpa.java
```

## controller/

```text
TeamController.java
```

---

# Team Entity Fields

```text
teamId
teamName
location
ownerName
establishmentYear
```

---

# Team APIs

```http
GET    /team
GET    /team/{id}
POST   /team
PUT    /team/{id}
DELETE /team/{id}
```

---

# Team Repository

```java
public interface TeamRepository extends JpaRepository<Team, Integer> {

    Team findByTeamId(int teamId);

    Team findByTeamName(String name);
}
```

---

# Team Service Responsibilities

- Add team
- Get all teams
- Get team by ID
- Update team
- Delete team
- Sort teams by name
- Duplicate team validation

---

# PHASE 4 — CRICKETER MODULE

## Goal

Implement Team ↔ Cricketer relationships and CRUD.

---

# Relationship

```text
Many Cricketers
       ↓
One Team
```

Use:

```java
@ManyToOne
@JoinColumn
```

---

# Files

## entity/

```text
Cricketer.java
```

## repository/

```text
CricketerRepository.java
```

## service/

```text
CricketerService.java
```

## service/impl/

```text
CricketerServiceImplJpa.java
```

## controller/

```text
CricketerController.java
```

---

# Cricketer Entity Fields

```text
cricketerId
team
cricketerName
age
nationality
experience
role
totalRuns
totalWickets
```

---

# Cricketer APIs

```http
GET    /cricketer
GET    /cricketer/{id}
POST   /cricketer
PUT    /cricketer/{id}
DELETE /cricketer/{id}
GET    /cricketer/team/{teamId}
```

---

# Cricketer Repository

```java
public interface CricketerRepository extends JpaRepository<Cricketer, Integer> {

    Cricketer findByCricketerId(int cricketerId);

    List<Cricketer> findByTeam_TeamId(int teamId);

    long countByTeam_TeamId(int teamId);
}
```

---

# Cricketer Service Responsibilities

- Add cricketer
- Get all cricketers
- Get cricketer by ID
- Update cricketer
- Delete cricketer
- Get cricketers by team
- Sort by experience
- Maximum 11 players validation

---

# PHASE 5 — MATCH MODULE

## Goal

Manage IPL matches and team relationships.

---

# Match Relationships

```text
Match
 ├── First Team
 ├── Second Team
 └── Winner Team
```

---

# Files

## entity/

```text
Match.java
```

## repository/

```text
MatchRepository.java
```

## service/

```text
MatchService.java
```

## service/impl/

```text
MatchServiceImplJpa.java
```

## controller/

```text
MatchController.java
```

---

# Match Entity Fields

```text
matchId
firstTeam
secondTeam
matchDate
venue
result
status
winnerTeam
```

---

# Match APIs

```http
GET    /match
GET    /match/{id}
POST   /match
PUT    /match/{id}
DELETE /match/{id}
GET    /match/status/{status}
```

---

# Match Repository

```java
public interface MatchRepository extends JpaRepository<Match, Integer> {

    Match findByMatchId(int matchId);

    List<Match> findAllByStatus(String status);
}
```

---

# Match Service Responsibilities

- Add match
- Get match
- Get matches by status
- Update match
- Delete match
- Match scheduling
- Team relationships

---

# Match Status Values

Use exact values:

```text
Pending
Scheduled
Completed
```

---

# PHASE 6 — CUSTOM EXCEPTIONS

## Goal

Handle business validation logic.

---

# Exception Files

```text
TeamAlreadyExistsException.java
TeamDoesNotExistException.java
TeamCricketerLimitExceededException.java
NoMatchesFoundException.java
```

---

# Exception Usage

| Exception                           | Purpose               |
| ----------------------------------- | --------------------- |
| TeamAlreadyExistsException          | Duplicate team        |
| TeamDoesNotExistException           | Team not found        |
| TeamCricketerLimitExceededException | More than 11 players  |
| NoMatchesFoundException             | No matches for status |

---

# PHASE 7 — VOTING MODULE

## Goal

Allow users to vote.

---

# Files

## entity/

```text
Vote.java
```

## repository/

```text
VoteRepository.java
```

## service/impl/

```text
VoteServiceImpl.java
```

## controller/

```text
VoteController.java
```

---

# Vote Entity Fields

```text
voteId
email
category
cricketer
team
```

---

# Voting APIs

```http
GET  /vote
POST /vote
GET  /vote/count
```

---

# Vote Categories

Use exact values:

```text
Team
Batsman
Bowler
All-rounder
Wicketkeeper
```

---

# Vote Repository

```java
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Long countByCategory(String category);
}
```

---

# PHASE 8 — TICKET BOOKING MODULE

## Goal

Book tickets for scheduled matches.

---

# Files

## entity/

```text
TicketBooking.java
```

## repository/

```text
TicketBookingRepository.java
```

## service/

```text
TicketBookingService.java
```

## service/impl/

```text
TicketBookingServiceImpl.java
```

## controller/

```text
TicketBookingController.java
```

---

# TicketBooking Entity Fields

```text
bookingId
email
match
numberOfTickets
```

---

# Ticket APIs

```http
GET    /ticket
POST   /ticket
DELETE /ticket/{id}
GET    /ticket/user/{email}
```

---

# Important Business Rule

ONLY:

```text
Scheduled
```

matches can book tickets.

---

# PHASE 9 — FINAL JWT SECURITY

## Goal

Secure all APIs.

---

# USER Permissions

USER can:

- Register
- Login
- View teams
- View cricketers
- View matches
- Vote
- Book tickets
- View own tickets

---

# ADMIN Permissions

ADMIN can:

- Register
- Login
- Create team
- Update team
- Delete team
- Create cricketer
- Update cricketer
- Delete cricketer
- Create match
- Update match
- Delete match
- View all votes
- View all tickets

---

# Security Files

```text
JwtUtil.java
JwtRequestFilter.java
SecurityConfig.java
Configurations.java
```

---

# Configurations.java

## Responsibilities

- BCryptPasswordEncoder Bean
- CORS Configuration

---

# SecurityConfig.java Responsibilities

- Configure authentication
- Configure authorization
- JWT security
- Secure APIs
- Role based access

---

# API Development Flow

```text
Frontend
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

---

# Testing Strategy

Use:

- Postman
- Thunder Client

---

# What To Test For Every Module

- Create
- Read
- Update
- Delete
- Validation
- Exceptions
- Relationships
- Database entries

---

# Important Rules

## Rule 1

Never move to next module until current module fully works.

---

## Rule 2

Always test APIs after completing controller.

---

## Rule 3

Always verify database records in H2 console.

---

## Rule 4

Build feature-by-feature.

---

# Recommended Lombok Usage

For entities:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
```

Avoid:

```java
@Data
```

on JPA entities.

---

# Final Development Order

```text
1. Project Setup
2. Authentication + JWT
3. Team Module
4. Cricketer Module
5. Match Module
6. Exceptions
7. Voting Module
8. Ticket Booking Module
9. Final JWT Security
10. Full API Testing
```

---

# Final Goal

Build a complete production-style IPL backend system with:

- JWT Authentication
- Role Based Access
- CRUD APIs
- Spring Security
- JPA Relationships
- Voting System
- Ticket Booking System
- Team Management
- Match Scheduling
- Exception Handling
- Clean Layered Architecture
