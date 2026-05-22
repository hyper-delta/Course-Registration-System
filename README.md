# Course Registration System (CRS)

A comprehensive, console-based university management application built using **Java 21**. This project implements core Object-Oriented Programming (OOP) concepts, a multi-layered architecture, and safe concurrent transaction management to handle student enrollments.

## 🚀 Features

- **Role-Based Access Control:** Separate menus and functional flows for Admin, Student, and Faculty users.
- **Prerequisite Validation:** Prevents students from enrolling in advanced courses without satisfying foundational criteria.
- **Credit Limit Enforcement:** Ensures course additions strictly align with a student's maximum allowable semester credits.
- **Thread-Safe Concurrency Test:** Features a live demonstration simulating simultaneous multi-student enrollment requests to prove thread safety under tight seat constraints.
- **In-Memory Store:** Employs a unified, decoupled repository framework mimicking database interactions.

---

## 📂 Project Structure

```text
CRS/
└── src/
    ├── model/       # Data Layer (User, Student, Course, Registration, etc.)
    ├── service/     # Business Logic Layer (Registration validation & processing)
    ├── store/       # Data Access/Repository Layer (In-memory collection management)
    ├── ui/          # Presentation Layer (Admin, Student, and Faculty console menus)
    └── Main.java    # Application Entry Point & Seeder Workflow
🛠️ How to Compile & RunEnsure you have Java 21 or later installed on your system.Open a terminal inside the root CRS/ folder and execute the following commands:1. Compile the ProjectBashjavac -d out src/model/*.java src/service/*.java src/store/*.java src/ui/*.java src/Main.java
2. Run the ApplicationBashjava -cp out Main
🧪 Quick Walkthrough DemoUpon launch, the system automatically seeds sample university configurations (including user accounts and pre-linked course dependencies). You can immediately test these core scenarios:StepActionCredentials / CommandsExpected Behavior1Admin CheckLogin Option 1 → ID: A001Review assigned prerequisites and manage accounts.2Prerequisite SuccessLogin Option 2 → ID: S001Registering for CS201 succeeds because S001 has pre-completed CS101.3Prerequisite FailureLogin Option 2 → ID: S002Attempting to add CS201 blocks enrollment due to missing CS101 credits.4Concurrency RaceMain Menu Option 45 student threads race simultaneously for 2 final remaining seats in CS201. The synchronized gateway guarantees exactly 2 successful allocations without over-allocation errors.
---
