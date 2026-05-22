package ui;

import model.*;
import store.DataStore;

import java.util.Scanner;

public class FacultyMenu {

    private final Faculty   faculty;
    private final DataStore store;
    private final Scanner   sc;

    public FacultyMenu(Faculty faculty, DataStore store, Scanner sc) {
        this.faculty = faculty;
        this.store   = store;
        this.sc      = sc;
    }

    public void show() {
        faculty.login();
        boolean running = true;
        while (running) {
            System.out.println("\n+====== Faculty Menu: " + faculty.getName() + " ======+");
            System.out.println("|  1. View My Assigned Courses             |");
            System.out.println("|  2. View Enrolled Students for a Course  |");
            System.out.println("|  0. Logout                               |");
            System.out.println("+==========================================+");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewAssignedCourses();
                case "2" -> viewEnrolledStudents();
                case "0" -> running = false;
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAssignedCourses() {
        System.out.println("\n=== My Assigned Courses ===");
        var courses = faculty.getAssignedCourses();
        if (courses.isEmpty()) { System.out.println("  (none assigned)"); return; }
        courses.forEach(c -> System.out.println("  " + c));
    }

    private void viewEnrolledStudents() {
        viewAssignedCourses();
        System.out.print("\nEnter Course ID: ");
        String id     = sc.nextLine().trim();
        Course course = store.findCourseById(id).orElse(null);
        if (course == null) { System.out.println("[FAIL] Course not found."); return; }
        if (course.getFaculty() == null || !course.getFaculty().getFacultyId()
                .equals(faculty.getFacultyId())) {
            System.out.println("[FAIL] You are not assigned to this course."); return;
        }
        faculty.viewEnrolledStudents(course);
    }
}