Gym Management System

1. User Documentation

Overview
The Gym Management System is a console-based Java application designed to manage gym operations for three types of users: Admins, Trainers, and Members.  
It connects to a PostgreSQL database to store and retrieve information such as users, memberships, workout classes, and merchandise.

The system uses role-based access control so that each type of user only sees the features relevant to them.

Main Features:
- Admins: Manage users, memberships, merchandise, and view total revenue.
- Trainers: Manage workout classes, view merchandise, and purchase memberships.
- Members: Browse classes, purchase memberships, and view merchandise.

How It Works
1. Login or Register  
   - New users can register with their details (username, email, phone, address, role, and password).
   - Existing users can log in with their username and password.

2. Role-Specific Menus  
   - After login, the menu options change depending on whether you are an Admin, Trainer, or Member.

3. Database Storage  
   - All information is securely stored in a PostgreSQL database.
   - Passwords are **BCrypt hashed** for security.

---
Class Overview & Interactions

Key Classes:
- Main: Starts the application.
- MenuRouter: Directs users to the correct menu based on their role.
- User, Admin, Trainer, Member: Represent different user types.  
  - `Admin`, `Trainer`, and `Member` extend `User`.
- Membership: Represents gym memberships purchased by members and trainers.
- WorkoutClass: Represents workout classes in the gym.
- Merchandise: Represents gym products like workout gear, drinks, and snacks.
- DAO (Data Access Object) Classes:  
  - `UserJdbcDAO`, `MembershipJdbcDAO`, `WorkoutClassJdbcDAO`, `MerchandiseJdbcDAO` handle database operations.
- Service Classes:  
  - `UserService`, `MembershipService`, `WorkoutClassService`, `MerchandiseService` contain business logic.
- DBConnection: Handles connecting to the PostgreSQL database.

               ┌─────────────┐
               │    User     │
               ├─────────────┤
               │ id          │
               │ username    │
               │ password    │
               │ role        │
               └─────┬───────┘
        ┌────────────┼─────────────┐
   ┌────▼─────┐  ┌───▼─────┐   ┌───▼─────┐
   │  Admin   │  │ Trainer │   │ Member  │
   └──────────┘  └─────────┘   └─────────┘

How to Start and Use the System

Starting the Program

1. Open a **terminal** in the project directory.
2. Run:
   ```bash
   mvn compile exec:java "-Dexec.mainClass=com.chrisking.Main"

    The Main Menu will appear.

Using the Program

    Register if you are a new user.

    Login with your username and password.

    Use the menu options to navigate and perform actions.

    Exit at any time by choosing 0.

2. Development Documentation
Javadoc

Javadoc comments are included in key classes and methods.
To generate HTML documentation:

mvn javadoc:javadoc

The generated documentation will be in the target/site/apidocs folder.
Project Directory Structure

gym-manager/
│── pom.xml               # Maven dependencies & build config
│── src/
│   ├── main/
│   │   ├── java/com/chrisking/
│   │   │   ├── Main.java
│   │   │   ├── MenuRouter.java
│   │   │   ├── model/      # Entity classes (User, Membership, etc.)
│   │   │   ├── dao/        # DAO classes (database access)
│   │   │   ├── service/    # Business logic
│   │   │   └── util/       # Utility classes (DB connection, logging)
│   │   └── resources/
│   │       ├── application.properties  # DB config
│   │       └── log.txt                  # Logger output
│   └── test/                            # Unit tests
└── README.md

Build Process & Dependencies

Dependencies in pom.xml:

    org.postgresql:postgresql — PostgreSQL JDBC driver.

    org.mindrot:jbcrypt — Password hashing.

    org.slf4j:slf4j-simple — Logging.

    org.codehaus.mojo:exec-maven-plugin — Running Java with Maven.

Build & Run:

mvn compile
mvn exec:java "-Dexec.mainClass=com.chrisking.Main"

Database Setup

    Install PostgreSQL and create a database:

CREATE DATABASE gym_db;

Create a database user:

CREATE USER gym_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE gym_db TO gym_user;

Run the schema file:

\i schema.sql

Update src/main/resources/application.properties:

    DB_URL=jdbc:postgresql://localhost:5432/gym_db
    DB_USER=gym_user
    DB_PASSWORD=your_password

Cloning and Running the Project

    Clone from GitHub:

git clone https://github.com/ChrisKeyin/JavaFinal.git
cd JavaFinal

Build and run:

    mvn compile exec:java "-Dexec.mainClass=com.chrisking.Main"


Individual Report

Project created by ChrisTopher King, challanges faced include the following: One of the trickiest parts was setting up PostgreSQL correctly with the right user permissions.

Early on, I ran into authentication errors (password authentication failed and permission denied for table users) that stopped the registration process.

This required manually creating roles, assigning privileges, and verifying connection strings in DatabaseManager which took quite a while to properly figure out.

As well as Maven Execution Command Issues, running the project through Maven was initially confusing because I had to learn the exact syntax for exec:java and how to set -Dexec.mainClass.

I got repeated errors (Unknown lifecycle phase ".mainClass=...") until I understood Maven’s parameter formatting.



Author: Christopher King
Version: 1.0
