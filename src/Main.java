import model.*;
import service.RegistrationService;
import store.DataStore;
import ui.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final DataStore            store   = new DataStore();
    private static final RegistrationService  service = new RegistrationService(store);
    private static final Scanner              sc      = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("+============================================+");
        System.out.println("|   Course Registration System (CRS)        |");
        System.out.println("|   MCA Java Project  |  Java 21            |");
        System.out.println("+============================================+");

        seedSampleData();

        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Student");
            System.out.println("3. Login as Faculty");
            System.out.println("4. Run Concurrency Test (Phase 4)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            switch (sc.nextLine().trim()) {
                case "1" -> loginAdmin();
                case "2" -> loginStudent();
                case "3" -> loginFaculty();
                case "4" -> runConcurrencyTest();
                case "0" -> { running = false; System.out.println("Goodbye!"); }
                default  -> System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    // ── Login flows ───────────────────────────────────────────────────────────
    private static void loginAdmin() {
        System.out.print("Admin ID: ");
        Admin admin = store.findAdminById(sc.nextLine().trim()).orElse(null);
        if (admin == null) { System.out.println("Admin not found."); return; }
        new AdminMenu(admin, store, service, sc).show();
    }

    private static void loginStudent() {
        System.out.print("Student ID: ");
        Student student = store.findStudentById(sc.nextLine().trim()).orElse(null);
        if (student == null) { System.out.println("Student not found."); return; }
        new StudentMenu(student, store, service, sc).show();
    }

    private static void loginFaculty() {
        System.out.print("Faculty ID: ");
        Faculty faculty = store.findFacultyById(sc.nextLine().trim()).orElse(null);
        if (faculty == null) { System.out.println("Faculty not found."); return; }
        new FacultyMenu(faculty, store, sc).show();
    }

    // ── Phase 4: Concurrency demonstration ────────────────────────────────────
    private static void runConcurrencyTest() {
        System.out.println("\n+====== Concurrency Test ======+");
        System.out.println("| 5 students racing for 2 seats |");
        System.out.println("| in CS201 simultaneously        |");
        System.out.println("+================================+");

        Course target = store.findCourseById("CS201").orElse(null);
        if (target == null) {
            System.out.println("[FAIL] Course CS201 not in store."); return;
        }

        // Reset CS201 to a clean state for the test
        target.getEnrolledStudents().clear();
        store.getStudents().forEach(s ->
                s.getRegistrations().removeIf(r -> r.getCourse().equals(target)));

        // 5 threads — one per student — all fire simultaneously
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (Student s : store.getStudents()) {
            pool.submit(() -> service.register(s, target));
        }
        pool.shutdown();
        try { pool.awaitTermination(10, TimeUnit.SECONDS); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.printf("%n[RESULT] %d / %d seats filled.%n",
                target.getEnrolledStudents().size(), target.getMaxSeats());
        System.out.println("(synchronized keyword ensured only "
                + target.getMaxSeats() + " registrations went through.)");
    }

    // ── Sample data seeder (Phase 5 workflow) ─────────────────────────────────
    private static void seedSampleData() {
        // Admin
        store.addAdmin(new Admin("A001", "Dr. Admin", "admin@uni.edu", "Super"));

        // Faculty
        Faculty f1 = new Faculty("U001", "Prof. Sharma", "sharma@uni.edu", "F001", "Computer Science");
        Faculty f2 = new Faculty("U002", "Prof. Verma",  "verma@uni.edu",  "F002", "Mathematics");
        store.addFaculty(f1);
        store.addFaculty(f2);

        // Courses  (CS201 has only 2 seats — perfect for concurrency demo)
        Course cs101 = new Course("CS101", "Introduction to Programming", 3, 30);
        Course cs201 = new Course("CS201", "Data Structures",             4,  2);
        Course cs301 = new Course("CS301", "Algorithms",                  4, 25);
        Course ma101 = new Course("MA101", "Discrete Mathematics",        3, 30);
        store.addCourse(cs101);
        store.addCourse(cs201);
        store.addCourse(cs301);
        store.addCourse(ma101);

        // Assign faculty
        cs101.assignFaculty(f1); f1.assignToCourse(cs101);
        cs201.assignFaculty(f1); f1.assignToCourse(cs201);
        cs301.assignFaculty(f1); f1.assignToCourse(cs301);
        ma101.assignFaculty(f2); f2.assignToCourse(ma101);

        // Prerequisites: CS201 needs CS101 | CS301 needs CS201
        cs201.addPrerequisite(cs101);
        cs301.addPrerequisite(cs201);

        // 5 students (enough for concurrency test)
        Student s1 = new Student("S001", "Aryan Kapoor",   "aryan@uni.edu",   20);
        Student s2 = new Student("S002", "Priya Mehta",    "priya@uni.edu",    20);
        Student s3 = new Student("S003", "Riya Singh",     "riya@uni.edu",     20);
        Student s4 = new Student("S004", "Karan Malhotra", "karan@uni.edu",    20);
        Student s5 = new Student("S005", "Neha Gupta",     "neha@uni.edu",     20);
        store.addStudent(s1);
        store.addStudent(s2);
        store.addStudent(s3);
        store.addStudent(s4);
        store.addStudent(s5);

        // Pre-enroll S001 in CS101 so they can demo the prerequisite flow for CS201
        Registration pre = new Registration(s1, cs101, "Confirmed");
        store.addRegistration(pre);
        s1.addRegistration(pre);
        cs101.enrollStudent(s1);

        System.out.println("[INFO] Sample data ready.");
        System.out.println("       Admin: A001 | Students: S001-S005 | Faculty: F001, F002");
    }
}