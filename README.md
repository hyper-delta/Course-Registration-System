# Course Registration System (CRS)

A comprehensive, console-based university management application built using **Java 21**. This project implements core Object-Oriented Programming (OOP) concepts, a multi-layered architecture, and safe concurrent transaction management to handle student enrollments.

---

## 🚀 Key Features

* **Role-Based Access Control:** Separate menus and functional flows for Admin, Student, and Faculty users.
* **Prerequisite Validation:** Prevents students from enrolling in advanced courses without satisfying foundational criteria.
* **Credit Limit Enforcement:** Ensures course additions strictly align with a student's maximum allowable semester credits.
* **Thread-Safe Concurrency Test:** Features a live demonstration simulating simultaneous multi-student enrollment requests to prove thread safety under tight seat constraints.
* **In-Memory Store:** Employs a unified, decoupled repository framework mimicking database interactions.

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

🛠️ How to Compile & Run
Prerequisites

    Ensure you have Java 21 or later installed on your system. You can verify this by running java -version in your terminal.

Step-by-Step Commands

    Open a terminal inside the root CRS/ folder.

    Compile the Project: Run this command to compile all files into an output folder named out:
    Bash

    javac -d out src/model/*.java src/service/*.java src/store/*.java src/ui/*.java src/Main.java

    Run the Application: Run this command to launch the system:
    Bash

    java -cp out Main
