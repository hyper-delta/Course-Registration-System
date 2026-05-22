# Course Registration System (CRS)

[cite_start]A comprehensive, console-based university management application engineered using **Java 21**[cite: 627]. [cite_start]This production-grade academic tool showcases robust Object-Oriented Programming (OOP) architectures, clean multi-layered separation of concerns, and thread-safe concurrent transaction handling for student enrollments[cite: 278, 684].

---

## 🚀 Key Features

* [cite_start]**Role-Based Access Control (RBAC):** Custom, decoupled dashboard menus and functional workflows tailored for **Admin**, **Student**, and **Faculty** users[cite: 362, 481, 559].
* [cite_start]**Prerequisite Validation:** Intelligently restricts enrollment in advanced coursework if foundational criteria are unmet[cite: 239].
* [cite_start]**Credit Limit Enforcement:** Monitored constraint verification to ensure student additions never exceed maximum allowable term credits[cite: 265].
* [cite_start]**Thread-Safe Concurrency Test:** A built-in stress-test routine executing simultaneous student registration requests to demonstrate race-condition defenses under minimal seat capacity[cite: 670].
* [cite_start]**In-Memory Unified Store:** Features a fully decoupled, central repository architecture acting as a mock data access layer[cite: 322].

---

## 📂 Project Directory Structure

```text
CRS/
└── src/
    ├── model/       # Data Layer (User blueprints, Course composition, and state models)
    ├── service/     # Business Logic Layer (Registration verification, prerequisite rules)
    ├── store/       # Data Access Layer (Centralized in-memory repository)
    ├── ui/          # Presentation Layer (CLI layout consoles for menus and inputs)
    └── Main.java    # Application entry point, system configuration, and seed engine

    🛠️ Step-by-Step Compilation & ExecutionFollow these exact steps to run the application on your local machine.PrerequisitesEnsure you have Java Development Kit (JDK) 21 or higher installed. Check your version by running java -version in your terminal.Execution WorkflowOpen your Terminal: Navigate to the root directory of the project where your src/ folder is located (the CRS/ root folder).  Compile the Source Code:
Compile all packages simultaneously into an isolated binaries output directory (out/) by executing: 

javac -d out src/model/*.java src/service/*.java src/store/*.java src/ui/*.java src/Main.java

Run the Application:
Execute the compiled bytecode pointing directly to the Main class definitions file:  
java -cp out Main

🧪 Simulation Walkthrough GuideThe application comes pre-configured with standard mock university objects (seeded with standard administrative profiles, pre-linked course dependencies, and dummy student sets) to facilitate immediate functional validation.  StepTarget ScenarioExecution ParametersExpected System Behavior1Admin AuditingMain Menu 1 → ID: A001 Logs in as Dr. Admin. Grants access to assign faculty, set dynamic course prerequisites, and review institutional status.2Prerequisite Validation (Pass)Main Menu 2 → ID: S001 Registering for CS201 succeeds perfectly because Aryan Kapoor has pre-completed CS101 during configuration seeding.3Prerequisite Validation (Fail)Main Menu 2 → ID: S002 Attempting to add CS201 as Priya Mehta fails instantly, throwing a validation warning due to missing CS101 tracking records.4Concurrency Stress-TestMain Menu 4 Spawns an execution pool firing 5 distinct student threads simultaneously targeting a single 2-seat class (CS201). The synchronized architecture strictly locks overflow, admitting exactly 2 candidates while rejecting 3 gra