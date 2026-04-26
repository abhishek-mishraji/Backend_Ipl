APP
│
├── server
│ │
│ ├── src
│ │ │
│ │ └── main
│ │ │
│ │ ├── java
│ │ │ │
│ │ │ └── com
│ │ │ │
│ │ │ └── edutech
│ │ │ │
│ │ │ └── progressive
│ │ │ │
│ │ │ ├── config
│ │ │ │ ├── Configurations.java
│ │ │ │ ├── DatabaseConnectionManager.java
│ │ │ │ └── SecurityConfig.java
│ │ │ │
│ │ │ ├── controller
│ │ │ │ ├── CricketerController.java
│ │ │ │ ├── MatchController.java
│ │ │ │ ├── TeamController.java
│ │ │ │ ├── TicketBookingController.java
│ │ │ │ ├── UserLoginController.java
│ │ │ │ └── VoteController.java
│ │ │ │
│ │ │ ├── dao
│ │ │ │ ├── CricketerDAO.java
│ │ │ │ ├── CricketerDAOImpl.java
│ │ │ │ ├── MatchDAO.java
│ │ │ │ ├── MatchDAOImpl.java
│ │ │ │ ├── TeamDAO.java
│ │ │ │ └── TeamDAOImpl.java
│ │ │ │
│ │ │ ├── dto
│ │ │ │ ├── SignUp.java
│ │ │ │ └── SignUpResponse.java
│ │ │ │
│ │ │ ├── entity
│ │ │ │ ├── Cricketer.java
│ │ │ │ ├── Match.java
│ │ │ │ ├── Team.java
│ │ │ │ ├── TicketBooking.java
│ │ │ │ ├── User.java
│ │ │ │ └── Vote.java
│ │ │ │
│ │ │ ├── exception
│ │ │ │ ├── NoMatchesFoundException.java
│ │ │ │ ├── TeamAlreadyExistsException.java
│ │ │ │ ├── TeamCricketerLimitExceededException.java
│ │ │ │ └── TeamDoesNotExistException.java
│ │ │ │
│ │ │ ├── jwt
│ │ │ │ ├── JwtRequestFilter.java
│ │ │ │ └── JwtUtil.java
│ │ │ │
│ │ │ ├── repository
│ │ │ │ ├── CricketerRepository.java
│ │ │ │ ├── MatchRepository.java
│ │ │ │ ├── TeamRepository.java
│ │ │ │ ├── TicketBookingRepository.java
│ │ │ │ ├── UserRepository.java
│ │ │ │ └── VoteRepository.java
│ │ │ │
│ │ │ ├── service
│ │ │ │ ├── CricketerService.java
│ │ │ │ ├── MatchService.java
│ │ │ │ ├── TeamService.java
│ │ │ │ ├── TicketBookingService.java
│ │ │ │ ├── VoteService.java
│ │ │ │ │
│ │ │ │ └── impl
│ │ │ │ ├── CricketerServiceImplArraylist.java
│ │ │ │ ├── CricketerServiceImplJdbc.java
│ │ │ │ ├── CricketerServiceImplJpa.java
│ │ │ │ ├── MatchServiceImplJdbc.java
│ │ │ │ ├── MatchServiceImplJpa.java
│ │ │ │ ├── TeamServiceImplArraylist.java
│ │ │ │ ├── TeamServiceImplJdbc.java
│ │ │ │ ├── TeamServiceImplJpa.java
│ │ │ │ ├── TicketBookingServiceImpl.java
│ │ │ │ ├── UserLoginServiceImpl.java
│ │ │ │ └── VoteServiceImpl.java
│ │ │ │
│ │ │ └── IplApplication.java
│ │ │
│ │ └── resources
│ │ └── application.properties
│ │
│ ├── target
│ │
│ └── pom.xml
