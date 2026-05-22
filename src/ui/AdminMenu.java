package ui;

import model.*;
import service.RegistrationService;
import store.DataStore;

import java.util.Scanner;

@SuppressWarnings("unused") //for removing the warning in line 15

public class AdminMenu {

    private final Admin               admin;
    private final DataStore           store;
    private final RegistrationService service; 
    private final Scanner             sc;

    public AdminMenu(Admin admin, DataStore store, RegistrationService service, Scanner sc) {
        this.admin   = admin;
        this.store   = store;
        this.service = service;
        this.sc      = sc;
    }

    public void show() {
        admin.login();
        boolean running = true;
        while (running) {
            System.out.println("\n+====== Admin Menu: " + admin.getName() + " ======+");
            System.out.println("|  1. Add Course                           |");
            System.out.println("|  2. Assign Faculty to Course             |");
            System.out.println("|  3. Set Prerequisite                     |");
            System.out.println("|  4. Add Student Account                  |");
            System.out.println("|  5. Add Faculty Account                  |");
            System.out.println("|  6. View All Courses                     |");
            System.out.println("|  0. Logout                               |");
            System.out.println("+==========================================+");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> addCourse();
                case "2" -> assignFaculty();
                case "3" -> setPrerequisite();
                case "4" -> addStudent();
                case "5" -> addFaculty();
                case "6" -> viewAllCourses();
                case "0" -> running = false;
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addCourse() {
        System.out.println("\n-- Add Course --");
        System.out.print("Course ID   : "); String id = sc.nextLine().trim();
        if (store.findCourseById(id).isPresent()) {
            System.out.println("[FAIL] Course ID already exists."); return;
        }
        System.out.print("Course Name : "); String name    = sc.nextLine().trim();
        System.out.print("Credits     : "); int    credits = readInt();
        System.out.print("Max Seats   : "); int    seats   = readInt();

        store.addCourse(new Course(id, name, credits, seats));
        System.out.println("[SUCCESS] Course added: " + name);
    }

    private void assignFaculty() {
        System.out.println("\n-- Assign Faculty --");
        System.out.print("Course ID  : "); String cId = sc.nextLine().trim();
        System.out.print("Faculty ID : "); String fId = sc.nextLine().trim();

        Course  course  = store.findCourseById(cId).orElse(null);
        Faculty faculty = store.findFacultyById(fId).orElse(null);

        if (course  == null) { System.out.println("[FAIL] Course not found.");  return; }
        if (faculty == null) { System.out.println("[FAIL] Faculty not found."); return; }

        course.assignFaculty(faculty);
        faculty.assignToCourse(course);
        System.out.println("[SUCCESS] " + faculty.getName() + " assigned to " + course.getCourseName());
    }

    private void setPrerequisite() {
        System.out.println("\n-- Set Prerequisite --");
        System.out.print("Target Course ID      : "); String cId   = sc.nextLine().trim();
        System.out.print("Prerequisite Course ID: "); String preId = sc.nextLine().trim();

        Course course = store.findCourseById(cId).orElse(null);
        Course prereq = store.findCourseById(preId).orElse(null);

        if (course == null) { System.out.println("[FAIL] Target course not found.");       return; }
        if (prereq == null) { System.out.println("[FAIL] Prerequisite course not found."); return; }
        if (course.equals(prereq)) { System.out.println("[FAIL] A course cannot be its own prerequisite."); return; }

        course.addPrerequisite(prereq);
        System.out.println("[SUCCESS] " + prereq.getCourseName()
                + "  -->  prerequisite for  -->  " + course.getCourseName());
    }

    private void addStudent() {
        System.out.println("\n-- Add Student Account --");
        System.out.print("Student ID  : "); String id = sc.nextLine().trim();
        if (store.findStudentById(id).isPresent()) {
            System.out.println("[FAIL] Student ID already exists."); return;
        }
        System.out.print("Name        : "); String name  = sc.nextLine().trim();
        System.out.print("Email       : "); String email = sc.nextLine().trim();
        System.out.print("Max Credits : "); int    max   = readInt();

        store.addStudent(new Student(id, name, email, max));
        System.out.println("[SUCCESS] Student account created: " + name);
    }

    private void addFaculty() {
        System.out.println("\n-- Add Faculty Account --");
        System.out.print("User ID    : "); String uid  = sc.nextLine().trim();
        System.out.print("Name       : "); String name = sc.nextLine().trim();
        System.out.print("Email      : "); String em   = sc.nextLine().trim();
        System.out.print("Faculty ID : "); String fid  = sc.nextLine().trim();
        System.out.print("Department : "); String dep  = sc.nextLine().trim();

        store.addFaculty(new Faculty(uid, name, em, fid, dep));
        System.out.println("[SUCCESS] Faculty account created: " + name);
    }

    private void viewAllCourses() {
        System.out.println("\n=== All Courses ===");
        if (store.getCourses().isEmpty()) { System.out.println("  (none)"); return; }
        for (Course c : store.getCourses()) {
            System.out.println("  " + c);
            if (!c.getPrerequisites().isEmpty()) {
                System.out.print("     Prerequisites: ");
                c.getPrerequisites().forEach(p -> System.out.print("[" + p.getCourseId() + "] "));
                System.out.println();
            }
        }
    }

    private int readInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid number, using 0."); return 0; }
    }
}