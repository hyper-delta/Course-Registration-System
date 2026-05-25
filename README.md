
# 🎓 Course Registration System (CRS)

A modern **JavaFX-based university course registration system** built using **Java 21**.

This project demonstrates advanced **Object-Oriented Design (OOD)** concepts, layered architecture, thread-safe concurrent processing, and a professional desktop GUI using **JavaFX** and custom **CSS styling**.

The system includes dedicated dashboards for **Students**, **Faculty**, and **Admins**, along with a live **Concurrency Visualizer** to demonstrate multithreading and synchronization concepts in real time.

---

## ✨ Features

### 👨‍🎓 Student Dashboard
- Register for courses
- Drop enrolled courses
- View registered courses
- Track completed courses
- Automatic prerequisite validation
- Credit limit enforcement
- Waitlist support for full courses

### 👨‍🏫 Faculty Dashboard
- View assigned courses
- Monitor enrolled students
- Access course details
- Faculty-course assignment management

### 👨‍💼 Admin Dashboard
- Add and manage courses
- Add students and faculty
- Assign faculty to courses
- Configure course prerequisites
- Prevent circular prerequisite chains using DFS
- Mark course completion for students
- System-wide analytics overview

### ⚡ Concurrency Visualizer
- Simulates multiple student threads registering simultaneously
- Demonstrates safe race-condition handling
- Uses `synchronized(course)` locking
- Real-time thread status updates
- Live concurrent event logging
- Prevents over-enrollment under concurrent access

---

## 🧰 Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core application development |
| JavaFX | GUI framework |
| CSS | Dark-themed UI styling |
| Multithreading | Concurrent registration simulation |
| OOP & OOD | Application architecture |
| In-Memory Repository | Data persistence simulation |

---

## 🎯 OOD Concepts Implemented

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

## 📂 Project Structure

```text
CRS/
├── src/
│
│   ├── gui/                        # JavaFX controllers & UI screens
│   │   ├── LoginController.java
│   │   ├── StudentController.java
│   │   ├── FacultyController.java
│   │   ├── AdminController.java
│   │   ├── ConcurrencyController.java
│   │   └── MainApp.java
│   │
│   ├── model/                      # Core domain models
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Faculty.java
│   │   ├── Admin.java
│   │   ├── Course.java
│   │   └── Registration.java
│   │
│   ├── service/                    # Business logic layer
│   │   └── RegistrationService.java
│   │
│   ├── store/                      # In-memory data store
│   │   └── CRSDataStore.java
│   │
│   ├── exceptions/                 # Custom exceptions
│   │   └── CRSException.java
│   │
│   └── resources/
│       └── styles/
│           └── dark-theme.css
│
├── compile.sh                      # Linux build automation
├── run.sh                          # Linux execution script
└── README.md
````

---

## ⚙️ Prerequisites

Before running the project, ensure you have:

* Java 21 or later installed
* JavaFX SDK configured
* Linux / Ubuntu / Linux Mint recommended

Check Java version:

```bash
java -version
```

---

## 🛠️ How to Compile & Run

### Recommended Method

Open terminal inside the project root folder and run:

```bash
./compile.sh && ./run.sh
```

This automatically:

* Compiles all source files
* Configures JavaFX module paths
* Launches the application

---

## 🔐 Demo Credentials

### 👨‍💼 Admin

```text
ID: A001
Password: admin123
```

### 👨‍🎓 Students

```text
IDs: S001 – S005
Password: pass123
```

### 👨‍🏫 Faculty

```text
IDs: F001, F002
Password: pass123
```

---

## ⚡ Concurrency Demonstration

The application includes a dedicated **JavaFX Concurrency Test Visualizer**.

### Simulation Flow

* 5 student threads attempt registration simultaneously
* Only 2 seats are available
* `synchronized(course)` locking prevents race conditions
* Remaining students are automatically waitlisted

### Concepts Demonstrated

* Multithreading
* Thread synchronization
* Race-condition prevention
* Real-time thread monitoring
* Concurrent transaction safety

---

## 🎨 UI Highlights

* Modern dark-themed JavaFX interface
* Responsive dashboard layouts
* Styled tables and cards
* Animated hover interactions
* Real-time visual feedback
* Professional admin panel design

---

## 📚 Academic Concepts Covered

This project demonstrates:

* Advanced Java Programming
* JavaFX GUI Development
* Object-Oriented Design
* Concurrent Programming
* Validation & Exception Handling
* Multi-layered Application Architecture

---

## 📌 Future Improvements

* Database integration (MySQL/PostgreSQL)
* REST API support
* Email notifications
* Attendance management
* Grade management
* Docker deployment
* Cloud hosting support

---

## 📄 License

This project is intended for academic and educational purposes.

```
```
