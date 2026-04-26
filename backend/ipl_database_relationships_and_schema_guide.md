# IPL Backend Project - Database Schema & Relationships Guide

# Project Database Overview

This project contains the following main tables:

```text
1. users
2. team
3. cricketer
4. match
5. vote
6. ticket_booking
```

These tables are interconnected using database relationships.

---

# COMPLETE DATABASE RELATIONSHIP MAP

```text
TEAM 1 ─────────────── * CRICKETER
CRICKETER * ────────── 1 TEAM

TEAM 1 ─────────────── * MATCH (as First Team)
TEAM 1 ─────────────── * MATCH (as Second Team)
TEAM 1 ─────────────── * MATCH (as Winner Team)
MATCH * ────────────── 1 TEAM (firstTeam)
MATCH * ────────────── 1 TEAM (secondTeam)
MATCH * ────────────── 1 TEAM (winnerTeam)

USER 1 ─────────────── * TICKET_BOOKING
TICKET_BOOKING * ───── 1 USER

MATCH 1 ────────────── * TICKET_BOOKING
TICKET_BOOKING * ───── 1 MATCH

TEAM 1 ─────────────── * VOTE
VOTE * ─────────────── 1 TEAM

CRICKETER 1 ────────── * VOTE
VOTE * ─────────────── 1 CRICKETER
```

---

# DATABASE EXPLANATION (CODE-ALIGNED)

This section explains the actual database design used by the current entity classes.

## Database Purpose

The database supports these backend modules:

- Authentication/User management
- Team and cricketer management
- Match scheduling and result tracking
- Voting by category
- Ticket booking for matches

## Naming Pattern Used

- Table names: lowercase with underscore where needed (`ticket_booking`)
- Primary keys: `<entity>_id`
- Foreign keys: reference parent table primary key
- Entity mapping: JPA `@ManyToOne` for child-to-parent and `@OneToMany(mappedBy = ...)` for parent-to-child

## RELATIONSHIP REFERENCE (ENTITY + FK)

| Parent Entity | Child Entity             | Cardinality | FK Column in Child | JPA Mapping                                                        |
| ------------- | ------------------------ | ----------- | ------------------ | ------------------------------------------------------------------ |
| Team          | Cricketer                | 1 -> \*     | `team_id`          | `Team @OneToMany(mappedBy = "team")`, `Cricketer @ManyToOne`       |
| Team          | Match (first team role)  | 1 -> \*     | `first_team_id`    | `Team @OneToMany(mappedBy = "firstTeam")`, `Match @ManyToOne`      |
| Team          | Match (second team role) | 1 -> \*     | `second_team_id`   | `Team @OneToMany(mappedBy = "secondTeam")`, `Match @ManyToOne`     |
| Team          | Match (winner team role) | 1 -> \*     | `winner_team_id`   | `Team @OneToMany(mappedBy = "winnerTeam")`, `Match @ManyToOne`     |
| User          | TicketBooking            | 1 -> \*     | `user_id`          | `User @OneToMany(mappedBy = "user")`, `TicketBooking @ManyToOne`   |
| Match         | TicketBooking            | 1 -> \*     | `match_id`         | `Match @OneToMany(mappedBy = "match")`, `TicketBooking @ManyToOne` |
| Team          | Vote                     | 1 -> \*     | `team_id`          | `Team @OneToMany(mappedBy = "team")`, `Vote @ManyToOne`            |
| Cricketer     | Vote                     | 1 -> \*     | `cricketer_id`     | `Cricketer @OneToMany(mappedBy = "cricketer")`, `Vote @ManyToOne`  |

## COLUMN REFERENCE (ALL TABLES)

### 1) users

| Column Name | Java Type | SQL Type (Typical) | Null | Key / Rule                               |
| ----------- | --------- | ------------------ | ---- | ---------------------------------------- |
| `user_id`   | `Integer` | `INT IDENTITY`     | No   | Primary Key                              |
| `full_name` | `String`  | `VARCHAR(100)`     | No   | Required                                 |
| `username`  | `String`  | `VARCHAR(30)`      | No   | Unique (`uk_users_username`)             |
| `password`  | `String`  | `VARCHAR(100)`     | No   | Required                                 |
| `email`     | `String`  | `VARCHAR(120)`     | No   | Unique (`uk_users_email`)                |
| `role`      | `String`  | `VARCHAR(20)`      | No   | Allowed by validation: `USER` or `ADMIN` |

Indexes in entity:

- `idx_users_username`
- `idx_users_email`

### 2) team

| Column Name          | Java Type | SQL Type (Typical) | Null | Key / Rule              |
| -------------------- | --------- | ------------------ | ---- | ----------------------- |
| `team_id`            | `Integer` | `INT IDENTITY`     | No   | Primary Key             |
| `team_name`          | `String`  | `VARCHAR(50)`      | No   | Unique (`uk_team_name`) |
| `location`           | `String`  | `VARCHAR(60)`      | No   | Required                |
| `owner_name`         | `String`  | `VARCHAR(100)`     | No   | Required                |
| `establishment_year` | `Integer` | `INT`              | No   | Range validated         |

Indexes in entity:

- `idx_team_name`
- `idx_team_location`

### 3) cricketer

| Column Name      | Java Type              | SQL Type (Typical) | Null | Key / Rule           |
| ---------------- | ---------------------- | ------------------ | ---- | -------------------- |
| `cricketer_id`   | `Integer`              | `INT IDENTITY`     | No   | Primary Key          |
| `team_id`        | `Integer` (via `Team`) | `INT`              | No   | FK -> `team.team_id` |
| `cricketer_name` | `String`               | `VARCHAR(100)`     | No   | Required             |
| `age`            | `Integer`              | `INT`              | No   | Range validated      |
| `nationality`    | `String`               | `VARCHAR(60)`      | No   | Required             |
| `experience`     | `Integer`              | `INT`              | No   | Min validated        |
| `role`           | `String`               | `VARCHAR(30)`      | No   | Required             |
| `total_runs`     | `Integer`              | `INT`              | No   | Min validated        |
| `total_wickets`  | `Integer`              | `INT`              | No   | Min validated        |

Indexes in entity:

- `idx_cricketer_team_id`
- `idx_cricketer_name`
- `idx_cricketer_role`

### 4) match

| Column Name      | Java Type              | SQL Type (Typical) | Null | Key / Rule                                          |
| ---------------- | ---------------------- | ------------------ | ---- | --------------------------------------------------- |
| `match_id`       | `Integer`              | `INT IDENTITY`     | No   | Primary Key                                         |
| `first_team_id`  | `Integer` (via `Team`) | `INT`              | No   | FK -> `team.team_id`                                |
| `second_team_id` | `Integer` (via `Team`) | `INT`              | No   | FK -> `team.team_id`                                |
| `winner_team_id` | `Integer` (via `Team`) | `INT`              | Yes  | FK -> `team.team_id`                                |
| `match_date`     | `LocalDate`            | `DATE`             | No   | Required                                            |
| `venue`          | `String`               | `VARCHAR(100)`     | No   | Required                                            |
| `result`         | `String`               | `VARCHAR(150)`     | Yes  | Optional until result update                        |
| `status`         | `String`               | `VARCHAR(20)`      | No   | Allowed values: `Pending`, `Scheduled`, `Completed` |

Indexes in entity:

- `idx_match_status`
- `idx_match_date`

### 5) vote

| Column Name    | Java Type                   | SQL Type (Typical) | Null | Key / Rule                                                                 |
| -------------- | --------------------------- | ------------------ | ---- | -------------------------------------------------------------------------- |
| `vote_id`      | `Integer`                   | `INT IDENTITY`     | No   | Primary Key                                                                |
| `email`        | `String`                    | `VARCHAR(120)`     | No   | Email format validated                                                     |
| `category`     | `String`                    | `VARCHAR(30)`      | No   | Allowed values: `Team`, `Batsman`, `Bowler`, `All-rounder`, `Wicketkeeper` |
| `team_id`      | `Integer` (via `Team`)      | `INT`              | Yes  | FK -> `team.team_id`                                                       |
| `cricketer_id` | `Integer` (via `Cricketer`) | `INT`              | Yes  | FK -> `cricketer.cricketer_id`                                             |

Indexes in entity:

- `idx_vote_category`
- `idx_vote_email`
- `idx_vote_team_id`
- `idx_vote_cricketer_id`

Vote business consistency rule (entity validation):

- If category is `Team`, `team_id` must be set and `cricketer_id` must be null.
- If category is player category, `cricketer_id` must be set and `team_id` must be null.

### 6) ticket_booking

| Column Name         | Java Type               | SQL Type (Typical) | Null | Key / Rule             |
| ------------------- | ----------------------- | ------------------ | ---- | ---------------------- |
| `booking_id`        | `Integer`               | `INT IDENTITY`     | No   | Primary Key            |
| `user_id`           | `Integer` (via `User`)  | `INT`              | No   | FK -> `users.user_id`  |
| `match_id`          | `Integer` (via `Match`) | `INT`              | No   | FK -> `match.match_id` |
| `number_of_tickets` | `Integer`               | `INT`              | No   | Min 1                  |

Indexes in entity:

- `idx_ticket_booking_user_id`
- `idx_ticket_booking_match_id`

Ticket booking business rule:

- Only matches with status `Scheduled` should be allowed for booking (enforced in service layer).

---

# RELATIONSHIP TYPES

## One To One (1 → 1)

One record connected to one record.

Example:

```text
One User
    ↓
One Passport
```

---

## One To Many (1 → \*)

One record connected to many records.

Example:

```text
One Team
   ↓
Many Cricketers
```

---

## Many To One (\* → 1)

Many records connected to one record.

Example:

```text
Many Cricketers
       ↓
One Team
```

---

## Many To Many (_ ↔ _)

Many records connected to many records.

Example:

```text
Many Students
       ↔
Many Courses
```

---

# TABLE 1 — USERS

## Purpose

Stores application users.

Users can:

- Register
- Login
- Book tickets
- Vote
- Access APIs

---

# users TABLE STRUCTURE

```text
users
│
├── user_id
├── full_name
├── username
├── password
├── email
└── role
```

---

# USERS TABLE FIELDS

| Field     | Type    | Purpose           |
| --------- | ------- | ----------------- |
| user_id   | Integer | Primary Key       |
| full_name | String  | Full name of user |
| username  | String  | Login username    |
| password  | String  | User password     |
| email     | String  | User email        |
| role      | String  | USER or ADMIN     |

---

# USERS TABLE RELATIONSHIPS

```text
USER 1 ─────────────── * TICKET_BOOKING
```

Meaning:

```text
One user can book many tickets.
```

---

# VISUAL EXAMPLE

```text
Abhishek
 ├── Ticket Booking 1
 ├── Ticket Booking 2
 └── Ticket Booking 3
```

---

# ENTITY RELATIONSHIP

## User.java

```java
@JsonIgnore
@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
private List<TicketBooking> bookings;
```

---

# TABLE 2 — TEAM

## Purpose

Stores IPL teams.

---

# TEAM TABLE STRUCTURE

```text
team
│
├── team_id
├── team_name
├── location
├── owner_name
└── establishment_year
```

---

# TEAM TABLE FIELDS

| Field              | Type    | Purpose            |
| ------------------ | ------- | ------------------ |
| team_id            | Integer | Primary Key        |
| team_name          | String  | IPL team name      |
| location           | String  | Team city/location |
| owner_name         | String  | Team owner         |
| establishment_year | Integer | Founded year       |

---

# EXAMPLE DATA

```text
team_id = 1
team_name = CSK
location = Chennai
owner_name = N Srinivasan
```

---

# TEAM RELATIONSHIPS

```text
TEAM 1 ─────────────── * CRICKETER

TEAM 1 ─────────────── * MATCH (as firstTeam)

TEAM 1 ─────────────── * MATCH (as secondTeam)

TEAM 1 ─────────────── * MATCH (as winnerTeam)

TEAM 1 ─────────────── * VOTE
```

---

# VISUAL

```text
CSK
 ├── Dhoni
 ├── Jadeja
 ├── Ruturaj
 └── Pathirana
```

---

# TEAM ↔ CRICKETER RELATIONSHIP

```text
One Team
    ↓
Many Cricketers
```

---

# JPA RELATIONSHIP

## Team.java

```java
@OneToMany(mappedBy = "team")
private List<Cricketer> cricketers;

@OneToMany(mappedBy = "firstTeam")
private List<Match> firstTeamMatches;

@OneToMany(mappedBy = "secondTeam")
private List<Match> secondTeamMatches;

@OneToMany(mappedBy = "winnerTeam")
private List<Match> winnerTeamMatches;

@OneToMany(mappedBy = "team")
private List<Vote> votes;
```

---

# TABLE 3 — CRICKETER

## Purpose

Stores all cricketers.

---

# CRICKETER TABLE STRUCTURE

```text
cricketer
│
├── cricketer_id
├── team_id
├── cricketer_name
├── age
├── nationality
├── experience
├── role
├── total_runs
└── total_wickets
```

---

# CRICKETER TABLE FIELDS

| Field          | Type    | Purpose             |
| -------------- | ------- | ------------------- |
| cricketer_id   | Integer | Primary Key         |
| team_id        | Integer | Foreign Key         |
| cricketer_name | String  | Player name         |
| age            | Integer | Player age          |
| nationality    | String  | Country             |
| experience     | Integer | Experience in years |
| role           | String  | Batsman/Bowler/etc  |
| total_runs     | Integer | Career runs         |
| total_wickets  | Integer | Career wickets      |

---

# FOREIGN KEY

```text
team_id
```

references:

```text
team.team_id
```

---

# VISUAL EXAMPLE

```text
Dhoni
  │
  └── team_id = 1
          │
          ▼
        CSK
```

---

# CRICKETER RELATIONSHIP

```text
Many Cricketers
        ↓
One Team
```

---

# JPA RELATIONSHIP

## Cricketer.java

```java
@ManyToOne
@JoinColumn(name = "team_id")
private Team team;

@OneToMany(mappedBy = "cricketer")
private List<Vote> votes;
```

---

# TABLE 4 — MATCH

## Purpose

Stores IPL match information.

---

# MATCH TABLE STRUCTURE

```text
match
│
├── match_id
├── first_team_id
├── second_team_id
├── winner_team_id
├── match_date
├── venue
├── result
└── status
```

---

# MATCH TABLE FIELDS

| Field          | Type    | Purpose                     |
| -------------- | ------- | --------------------------- |
| match_id       | Integer | Primary Key                 |
| first_team_id  | Integer | Foreign Key                 |
| second_team_id | Integer | Foreign Key                 |
| winner_team_id | Integer | Foreign Key                 |
| match_date     | Date    | Match date                  |
| venue          | String  | Match stadium               |
| result         | String  | Match result                |
| status         | String  | Pending/Scheduled/Completed |

---

# MATCH RELATIONSHIPS

```text
MATCH
 ├── First Team
 ├── Second Team
 └── Winner Team
```

---

# VISUAL EXAMPLE

```text
RCB vs CSK
Venue: Wankhede
Winner: CSK
Status: Completed
```

---

# MATCH TEAM RELATIONSHIP

```text
TEAM
  │
  ├── plays many matches
  │
  ▼
MATCH
```

---

# JPA RELATIONSHIPS

## Match.java

```java
@ManyToOne
@JoinColumn(name = "first_team_id")
private Team firstTeam;
```

```java
@ManyToOne
@JoinColumn(name = "second_team_id")
private Team secondTeam;
```

```java
@ManyToOne
@JoinColumn(name = "winner_team_id")
private Team winnerTeam;

@OneToMany(mappedBy = "match")
private List<TicketBooking> bookings;
```

---

# MATCH STATUS VALUES

Use exact values:

```text
Pending
Scheduled
Completed
```

---

# TABLE 5 — VOTE

## Purpose

Stores voting information.

Users can vote for:

- Favorite Team
- Favorite Batsman
- Favorite Bowler
- Favorite All-rounder
- Favorite Wicketkeeper

---

# VOTE TABLE STRUCTURE

```text
vote
│
├── vote_id
├── email
├── category
├── team_id
└── cricketer_id
```

---

# VOTE TABLE FIELDS

| Field        | Type    | Purpose       |
| ------------ | ------- | ------------- |
| vote_id      | Integer | Primary Key   |
| email        | String  | User email    |
| category     | String  | Vote category |
| team_id      | Integer | Foreign Key   |
| cricketer_id | Integer | Foreign Key   |

---

# VOTE RELATIONSHIPS

```text
TEAM 1 ─────────────── * VOTE

CRICKETER 1 ────────── * VOTE
```

---

# ENTITY VALIDATION RULE (Vote.java)

```text
If category is Team:
   team must be set and cricketer must be null.

If category is Batsman/Bowler/All-rounder/Wicketkeeper:
   cricketer must be set and team must be null.
```

This is enforced in entity validation logic.

---

# VISUAL EXAMPLE

```text
Vote
 ├── Team Vote
 └── Cricketer Vote
```

---

# EXAMPLE

## Favorite Team Vote

```text
team_id filled
cricketer_id null
```

---

## Favorite Bowler Vote

```text
cricketer_id filled
team_id null
```

---

# VOTE CATEGORIES

Use exact values:

```text
Team
Batsman
Bowler
All-rounder
Wicketkeeper
```

---

# TABLE 6 — TICKET_BOOKING

## Purpose

Stores ticket booking information.

---

# TICKET_BOOKING TABLE STRUCTURE

```text
ticket_booking
│
├── booking_id
├── user_id
├── match_id
└── number_of_tickets
```

---

# TICKET_BOOKING TABLE FIELDS

| Field             | Type    | Purpose           |
| ----------------- | ------- | ----------------- |
| booking_id        | Integer | Primary Key       |
| user_id           | Integer | Foreign Key       |
| match_id          | Integer | Foreign Key       |
| number_of_tickets | Integer | Number of tickets |

---

# TICKET_BOOKING RELATIONSHIPS

```text
USER 1 ─────────────── * TICKET_BOOKING

MATCH 1 ────────────── * TICKET_BOOKING
```

---

# JPA RELATIONSHIPS

## TicketBooking.java

```java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "match_id")
private Match match;
```

---

# VISUAL EXAMPLE

```text
Abhishek
   │
   ├── RCB vs CSK
   ├── GT vs MI
   └── SRH vs RR
```

---

# IMPORTANT BUSINESS RULE

ONLY:

```text
Scheduled
```

matches can be booked.

---

# PRIMARY KEY VS FOREIGN KEY

---

# PRIMARY KEY

Unique identifier.

Examples:

```text
user_id
team_id
cricketer_id
match_id
```

---

# FOREIGN KEY

References another table.

Examples:

```text
cricketer.team_id
```

references:

```text
team.team_id
```

---

# VISUAL

```text
TEAM TABLE
────────────
team_id = 1

CRICKETER TABLE
────────────
team_id = 1
```

Connected.

---

# SIMPLE REAL WORLD UNDERSTANDING OF DATABASE RELATIONSHIPS

Think of database relationships like real-life connections.

Example:

```text
One School
    ↓
Many Students
```

This means:

```text
School and Students are related.
```

Same concept is used inside your IPL project.

---

# HOW TO THINK ABOUT RELATIONSHIPS

Every table has:

## 1. Primary Key

Unique identity.

Example:

```text
team_id
user_id
match_id
```

These uniquely identify records.

---

## 2. Foreign Key

Connection to another table.

Example:

```text
team_id inside cricketer table
```

means:

```text
This cricketer belongs to this team.
```

---

# VISUAL THINKING METHOD

You should think like this:

```text
Parent Table
      ↓
Child Table
```

Example:

```text
TEAM
  ↓
CRICKETER
```

Meaning:

```text
Team owns cricketers.
```

---

# HOW DATABASE INTERNALLY STORES RELATIONSHIPS

Suppose:

```text
TEAM TABLE
```

| team_id | team_name |
| ------- | --------- |
| 1       | CSK       |
| 2       | RCB       |

---

Now:

```text
CRICKETER TABLE
```

| cricketer_name | team_id |
| -------------- | ------- |
| Dhoni          | 1       |
| Jadeja         | 1       |
| Virat          | 2       |

---

Database understands:

```text
Dhoni belongs to CSK
Virat belongs to RCB
```

because:

```text
team_id is connected.
```

---

# COMPLETE RELATIONSHIP DIAGRAM

```text
                TEAM
                  │
      ┌───────────┼───────────┐
      │           │           │
      ▼           ▼           ▼
 CRICKETER      MATCH        VOTE
                    │
                    ▼
             TICKET_BOOKING
                    ▲
                    │
                   USER
```

---

# JPA RELATIONSHIP ANNOTATIONS

| Annotation  | Meaning     |
| ----------- | ----------- |
| @OneToOne   | 1 ↔ 1       |
| @OneToMany  | 1 ↔ \*      |
| @ManyToOne  | \* ↔ 1      |
| @ManyToMany | _ ↔ _       |
| @JoinColumn | Foreign Key |

---

# MOST IMPORTANT RELATIONSHIPS IN PROJECT

| Relationship          | Type                                        |
| --------------------- | ------------------------------------------- |
| Team → Cricketer      | OneToMany                                   |
| Cricketer → Team      | ManyToOne                                   |
| Team → Match          | OneToMany (firstTeam/secondTeam/winnerTeam) |
| Match → Team          | ManyToOne (firstTeam/secondTeam/winnerTeam) |
| User → TicketBooking  | OneToMany                                   |
| TicketBooking → User  | ManyToOne                                   |
| Match → TicketBooking | OneToMany                                   |
| TicketBooking → Match | ManyToOne                                   |
| Vote → Team           | ManyToOne                                   |
| Vote → Cricketer      | ManyToOne                                   |

---

# FINAL DATABASE FLOW

```text
Frontend
   ↓
Controller
   ↓
Service
   ↓
DAO
   ↓
Repository
   ↓
Database Tables
```

---

# FINAL GOAL

Build a complete IPL backend system with:

- Authentication
- Authorization
- Team Management
- Cricketer Management
- Match Scheduling
- Voting System
- Ticket Booking
- JWT Security
- Layered Architecture
- JPA Relationships
- Industry Standard Backend Design
