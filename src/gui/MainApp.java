package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
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
        // Admin
        store.addAdmin(new Admin("A001", "Dr. Admin", "admin@uni.edu", "admin123", "Super"));

        // Faculty
        Faculty f1 = new Faculty("U001", "Prof. Sharma", "sharma@uni.edu",
                "pass123", "F001", "Computer Science");
        Faculty f2 = new Faculty("U002", "Prof. Verma", "verma@uni.edu",
                "pass123", "F002", "Mathematics");
        store.addFaculty(f1);
        store.addFaculty(f2);

        // Courses (CS201 has 2 seats for concurrency demo)
        Course cs101 = new Course("CS101", "Intro to Programming",  3, 30);
        Course cs201 = new Course("CS201", "Data Structures",       4,  2);
        Course cs301 = new Course("CS301", "Algorithms",            4, 25);
        Course ma101 = new Course("MA101", "Discrete Mathematics",  3, 30);
        store.addCourse(cs101);
        store.addCourse(cs201);
        store.addCourse(cs301);
        store.addCourse(ma101);

        // Assign faculty
        cs101.assignFaculty(f1); f1.assignToCourse(cs101);
        cs201.assignFaculty(f1); f1.assignToCourse(cs201);
        cs301.assignFaculty(f1); f1.assignToCourse(cs301);
        ma101.assignFaculty(f2); f2.assignToCourse(ma101);

        // Prerequisites
        cs201.addPrerequisite(cs101);
        cs301.addPrerequisite(cs201);

        // Students
        Student s1 = new Student("S001", "Aryan Kapoor",   "aryan@uni.edu",  "pass123", 20);
        Student s2 = new Student("S002", "Priya Mehta",    "priya@uni.edu",  "pass123", 20);
        Student s3 = new Student("S003", "Riya Singh",     "riya@uni.edu",   "pass123", 20);
        Student s4 = new Student("S004", "Karan Malhotra", "karan@uni.edu",  "pass123", 20);
        Student s5 = new Student("S005", "Neha Gupta",     "neha@uni.edu",   "pass123", 20);
        store.addStudent(s1);
        store.addStudent(s2);
        store.addStudent(s3);
        store.addStudent(s4);
        store.addStudent(s5);

        // S001 has already completed CS101 (for prerequisite demo)
        s1.markCompleted(cs101);
    }

    public static void main(String[] args) {
        launch(args);
    }
}