````md
# 🎓 Course Registration System (CRS)

A modern JavaFX-based university course registration system built using **Java 21**.  
This project demonstrates advanced **Object-Oriented Design (OOD)** concepts, layered architecture, thread-safe concurrent processing, and a professional desktop GUI using JavaFX and custom CSS styling.

The system includes dedicated dashboards for **Students, Faculty, and Admins**, along with a live **Concurrency Visualizer** to demonstrate multithreading and synchronization concepts in real time.

---

# 🚀 Key Features

## 👨‍🎓 Student Dashboard
- Register for courses
- Drop enrolled courses
- View registered courses
- Track completed courses
- Automatic prerequisite validation
- Credit limit enforcement
- Waitlist support for full courses

## 👨‍🏫 Faculty Dashboard
- View assigned courses
- Monitor enrolled students
- Access course details
- Faculty-course assignment management

## 👨‍💼 Admin Dashboard
- Add/manage courses
- Add students and faculty
- Assign faculty to courses
- Configure prerequisites
- Prevent circular prerequisite chains
- Mark course completion for students
- System-wide analytics dashboard

## ⚡ Concurrency Visualizer
- Simulates multiple student threads registering simultaneously
- Demonstrates race conditions safely
- Uses synchronized(course) locking
- Real-time thread status updates
- Live event logging
- Prevents over-enrollment under concurrent access

---

# 🧰 Tech Stack

- Java 21
- JavaFX
- CSS (Dark Theme UI)
- Multithreading & Synchronization
- OOP & OOD Principles
- In-Memory Repository Architecture

---

# 🎯 OOD Concepts Implemented

- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Composition
- Separation of Concerns
- Layered Architecture
- Thread Synchronization
- Modular Controller-Based Design

---

# 📂 Project Structure

```text
CRS/
├── src/
│
│   ├── gui/                # JavaFX controllers & screens
│   │   ├── LoginController.java
│   │   ├── StudentController.java
│   │   ├── FacultyController.java
│   │   ├── AdminController.java
│   │   ├── ConcurrencyController.java
│   │   └── MainApp.java
│
│   ├── model/              # Core domain models
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Faculty.java
│   │   ├── Admin.java
│   │   ├── Course.java
│   │   └── Registration.java
│
│   ├── service/            # Business logic layer
│   │   └── RegistrationService.java
│
│   ├── store/              # In-memory data store
│   │   └── CRSDataStore.java
│
│   ├── exceptions/         # Custom exceptions
│   │   └── CRSException.java
│
│   └── resources/
│       └── styles/
│           └── dark-theme.css
│
├── compile.sh              # Linux build automation
├── run.sh                  # Linux run automation
└── README.md
````

---

# ⚙️ Prerequisites

Before running the project, ensure you have:

* Java 21 or later installed
* JavaFX SDK configured
* Linux / Linux Mint / Ubuntu environment recommended

Check Java version:

```bash
java -version
```

---

# 🛠️ How to Compile & Run

## Recommended Method

Open terminal inside the project root folder:

```bash
./compile.sh && ./run.sh
```

This automatically:

* Compiles all modules
* Configures JavaFX paths
* Launches the application

---

# 🔐 Demo Credentials

## 👨‍💼 Admin

```text
ID: A001
Password: admin123
```

## 👨‍🎓 Students

```text
IDs: S001 – S005
Password: pass123
```

## 👨‍🏫 Faculty

```text
IDs: F001, F002
Password: pass123
```

---

# ⚡ Concurrency Demonstration

The application includes a dedicated JavaFX Concurrency Test Visualizer.

### Simulation Flow

* 5 student threads attempt registration simultaneously
* Only 2 seats are available
* synchronized(course) locking prevents race conditions
* Remaining students are automatically waitlisted

### Concepts Demonstrated

* Multithreading
* Thread synchronization
* Race condition prevention
* Real-time thread monitoring
* Concurrent transaction safety

---

# 🎨 UI Highlights

* Modern dark-themed JavaFX interface
* Responsive dashboard layout
* Animated hover interactions
* Styled tables and cards
* Real-time visual feedback
* Professional admin panel design

---

# 📚 Academic Concepts Covered

This project was developed as part of an MCA Java academic project and demonstrates:

* Advanced Java Programming
* GUI Development using JavaFX
* Object-Oriented Design
* Concurrent Programming
* Data Validation
* Multi-layered Application Architecture

---

# 📌 Future Improvements

* Database integration (MySQL/PostgreSQL)
* REST API support
* Email notifications
* Attendance management
* Grade management
* Docker deployment
* Cloud hosting support

---

# 📄 License

This project is intended for academic and educational purposes.

```
```
