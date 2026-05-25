package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import model.Registration;
import model.RegistrationStatus;
import service.RegistrationService;
import store.DataStore;

public class MainApp extends Application {

    // Static shared state — accessible from every controller
    public static DataStore           store;
    public static RegistrationService service;
    private static Stage              primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        store        = new DataStore();
        service      = new RegistrationService(store);

        seedData();

        stage.setTitle("Course Registration System — MCA Project");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setResizable(true);
        stage.setScene(LoginController.getScene());
        stage.show();
    }

    /** Called by every controller to navigate between screens. */
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /** Navigate back to the login screen (logout). */
    public static void logout() {
        switchScene(LoginController.getScene());
    }

    /** Loads the CSS file from the compiled output classpath. */
    public static String getCss() {
        var url = MainApp.class.getResource("/resources/styles/dark-theme.css");
        if (url == null) {
            System.err.println("[WARN] CSS not found — did you run compile.sh?");
            return "";
        }
        return url.toExternalForm();
    }

    // ── Seed data ─────────────────────────────────────────────────────────────

    private void seedData() {

    // ═══════════════════════════════════════════════════════════
    //  ADMINS
    // ═══════════════════════════════════════════════════════════
    store.addAdmin(new Admin("A001", "Dr. Admin",      "admin@uni.edu",   "admin123", "Super"));
    store.addAdmin(new Admin("A002", "Dr. Registrar",  "reg@uni.edu",     "admin123", "Standard"));

    // ═══════════════════════════════════════════════════════════
    //  FACULTY
    // ═══════════════════════════════════════════════════════════
    Faculty f1 = new Faculty("U001", "Prof. Sharma", "sharma@uni.edu", "pass123", "F001", "Computer Science");
    Faculty f2 = new Faculty("U002", "Prof. Verma",  "verma@uni.edu",  "pass123", "F002", "Mathematics");
    Faculty f3 = new Faculty("U003", "Prof. Iyer",   "iyer@uni.edu",   "pass123", "F003", "Database Systems");
    Faculty f4 = new Faculty("U004", "Prof. Khan",   "khan@uni.edu",   "pass123", "F004", "Software Engineering");
    Faculty f5 = new Faculty("U005", "Prof. Reddy",  "reddy@uni.edu",  "pass123", "F005", "Networks & Security");
    store.addFaculty(f1);
    store.addFaculty(f2);
    store.addFaculty(f3);
    store.addFaculty(f4);
    store.addFaculty(f5);

    // ═══════════════════════════════════════════════════════════
    //  COURSES
    // ═══════════════════════════════════════════════════════════

    // Computer Science core
    Course cs101 = new Course("CS101", "Intro to Programming",    3, 30);
    Course cs201 = new Course("CS201", "Data Structures",          4,  2);  // 2 seats — concurrency demo
    Course cs301 = new Course("CS301", "Algorithms",               4, 25);
    Course cs401 = new Course("CS401", "Operating Systems",        4, 20);
    Course cs501 = new Course("CS501", "Database Management",      3, 25);
    Course cs601 = new Course("CS601", "Software Engineering",     3, 20);
    Course cs701 = new Course("CS701", "Computer Networks",        3, 20);
    Course cs801 = new Course("CS801", "Machine Learning",         4, 15);

    // Mathematics
    Course ma101 = new Course("MA101", "Discrete Mathematics",     3, 30);
    Course ma201 = new Course("MA201", "Linear Algebra",           3, 25);
    Course ma301 = new Course("MA301", "Probability & Statistics", 3, 20);

    // Software Engineering
    Course se101 = new Course("SE101", "Agile & DevOps",           2, 30);

    store.addCourse(cs101);
    store.addCourse(cs201);
    store.addCourse(cs301);
    store.addCourse(cs401);
    store.addCourse(cs501);
    store.addCourse(cs601);
    store.addCourse(cs701);
    store.addCourse(cs801);
    store.addCourse(ma101);
    store.addCourse(ma201);
    store.addCourse(ma301);
    store.addCourse(se101);

    // ── Assign faculty ────────────────────────────────────────
    cs101.assignFaculty(f1); f1.assignToCourse(cs101);
    cs201.assignFaculty(f1); f1.assignToCourse(cs201);
    cs301.assignFaculty(f1); f1.assignToCourse(cs301);
    cs401.assignFaculty(f5); f5.assignToCourse(cs401);
    cs501.assignFaculty(f3); f3.assignToCourse(cs501);
    cs601.assignFaculty(f4); f4.assignToCourse(cs601);
    cs701.assignFaculty(f5); f5.assignToCourse(cs701);
    cs801.assignFaculty(f1); f1.assignToCourse(cs801);
    ma101.assignFaculty(f2); f2.assignToCourse(ma101);
    ma201.assignFaculty(f2); f2.assignToCourse(ma201);
    ma301.assignFaculty(f2); f2.assignToCourse(ma301);
    se101.assignFaculty(f4); f4.assignToCourse(se101);

    // ── Prerequisites ─────────────────────────────────────────
    cs201.addPrerequisite(cs101);   // CS201 needs CS101
    cs301.addPrerequisite(cs201);   // CS301 needs CS201
    cs401.addPrerequisite(cs201);   // CS401 needs CS201
    cs501.addPrerequisite(cs101);   // CS501 needs CS101
    cs601.addPrerequisite(cs301);   // CS601 needs CS301
    cs801.addPrerequisite(cs301);   // CS801 needs CS301
    ma201.addPrerequisite(ma101);   // MA201 needs MA101
    ma301.addPrerequisite(ma201);   // MA301 needs MA201

    // ═══════════════════════════════════════════════════════════
    //  STUDENTS
    // ═══════════════════════════════════════════════════════════
    Student s1  = new Student("S001", "Aryan Kapoor",    "aryan@uni.edu",   "pass123", 20);
    Student s2  = new Student("S002", "Priya Mehta",     "priya@uni.edu",   "pass123", 20);
    Student s3  = new Student("S003", "Riya Singh",      "riya@uni.edu",    "pass123", 20);
    Student s4  = new Student("S004", "Karan Malhotra",  "karan@uni.edu",   "pass123", 20);
    Student s5  = new Student("S005", "Neha Gupta",      "neha@uni.edu",    "pass123", 20);
    Student s6  = new Student("S006", "Amit Joshi",      "amit@uni.edu",    "pass123", 20);
    Student s7  = new Student("S007", "Sneha Patel",     "sneha@uni.edu",   "pass123", 20);
    Student s8  = new Student("S008", "Rohan Das",       "rohan@uni.edu",   "pass123", 20);
    Student s9  = new Student("S009", "Meera Nair",      "meera@uni.edu",   "pass123", 20);
    Student s10 = new Student("S010", "Vikram Bose",     "vikram@uni.edu",  "pass123", 20);
    Student s11 = new Student("S011", "Anjali Sharma",   "anjali@uni.edu",  "pass123", 20);
    Student s12 = new Student("S012", "Dev Malhotra",    "dev@uni.edu",     "pass123", 20);

    store.addStudent(s1);
    store.addStudent(s2);
    store.addStudent(s3);
    store.addStudent(s4);
    store.addStudent(s5);
    store.addStudent(s6);
    store.addStudent(s7);
    store.addStudent(s8);
    store.addStudent(s9);
    store.addStudent(s10);
    store.addStudent(s11);
    store.addStudent(s12);

    // ── Completed courses per student ─────────────────────────

    // Aryan — completed CS101 and MA101, mid-journey
    s1.markCompleted(cs101);
    s1.markCompleted(ma101);

    // Priya — completed CS101 only, just starting
    s2.markCompleted(cs101);

    // Riya — completed CS101, CS201, MA101 — can take CS301/CS401
    s3.markCompleted(cs101);
    s3.markCompleted(cs201);
    s3.markCompleted(ma101);

    // Karan — fresher, no completed courses

    // Neha — completed CS101, MA101, MA201 — deep in maths track
    s5.markCompleted(cs101);
    s5.markCompleted(ma101);
    s5.markCompleted(ma201);

    // Amit — completed CS101, CS201, CS301 — can take CS601/CS801
    s6.markCompleted(cs101);
    s6.markCompleted(cs201);
    s6.markCompleted(cs301);

    // Sneha — completed CS101 only
    s7.markCompleted(cs101);

    // Rohan — fresher, no completed courses

    // Meera — completed CS101 and CS201
    s9.markCompleted(cs101);
    s9.markCompleted(cs201);

    // Vikram — completed full maths track + CS101
    s10.markCompleted(cs101);
    s10.markCompleted(ma101);
    s10.markCompleted(ma201);
    s10.markCompleted(ma301);

    // Anjali — fresher

    // Dev — most advanced: CS101→CS201→CS301→CS401 done
    s12.markCompleted(cs101);
    s12.markCompleted(cs201);
    s12.markCompleted(cs301);
    s12.markCompleted(cs401);

    // ═══════════════════════════════════════════════════════════
    //  ACTIVE REGISTRATIONS (pre-seeded so tables aren't empty)
    // ═══════════════════════════════════════════════════════════

    // Aryan → registered for CS201 (has CS101 completed)
    enrollStudent(s1, cs201);

    // Priya → registered for CS501 (has CS101 completed)
    enrollStudent(s2, cs501);

    // Riya → registered for CS301 and CS401
    enrollStudent(s3, cs301);
    enrollStudent(s3, cs401);

    // Neha → registered for MA301 (has MA201 completed)
    enrollStudent(s5, ma301);

    // Amit → registered for CS601 and CS801 (has CS301 completed)
    enrollStudent(s6, cs601);
    enrollStudent(s6, cs801);

    // Sneha → registered for CS501 and SE101
    enrollStudent(s7, cs501);
    enrollStudent(s7, se101);

    // Meera → registered for CS401 (has CS201 completed)
    enrollStudent(s9, cs401);

    // Vikram → registered for CS501 and SE101
    enrollStudent(s10, cs501);
    enrollStudent(s10, se101);

    // Karan → fresher, registered for CS101 and MA101 (no prereqs)
    enrollStudent(s4, cs101);
    enrollStudent(s4, ma101);

    // Rohan → registered for CS101 and SE101
    enrollStudent(s8, cs101);
    enrollStudent(s8, se101);

    // Dev → registered for CS601 and CS801
    enrollStudent(s12, cs601);
    enrollStudent(s12, cs801);

    System.out.println("[INFO] Seed data loaded:");
    System.out.println("       Admins: A001, A002  |  password: admin123");
    System.out.println("       Faculty: F001-F005  |  password: pass123");
    System.out.println("       Students: S001-S012 |  password: pass123");
}

// ── Helper: enroll a student bypassing validation (for seed data only) ────
private void enrollStudent(Student student, Course course) {
    Registration reg = new Registration(student, course, RegistrationStatus.CONFIRMED);
    store.addRegistration(reg);
    student.addRegistration(reg);
    course.enrollStudent(student);
}

    public static void main(String[] args) {
        launch(args);
    }
}