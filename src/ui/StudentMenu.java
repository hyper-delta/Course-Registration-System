package ui;

import model.*;
import service.RegistrationService;
import store.DataStore;

import java.util.Scanner;

public class StudentMenu {

    private final Student             student;
    private final DataStore           store;
    private final RegistrationService service;
    private final Scanner             sc;

    public StudentMenu(Student student, DataStore store, RegistrationService service, Scanner sc) {
        this.student = student;
        this.store   = store;
        this.service = service;
        this.sc      = sc;
    }

    public void show() {
        student.login();
        boolean running = true;
        while (running) {
            System.out.println("\n+====== Student Menu: " + student.getName() + " ======+");
            System.out.println("|  1. View Available Courses               |");
            System.out.println("|  2. Register for a Course                |");
            System.out.println("|  3. Drop a Course                        |");
            System.out.println("|  4. View My Registrations                |");
            System.out.println("|  5. View My Timetable                    |");
            System.out.println("|  0. Logout                               |");
            System.out.println("+==========================================+");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewCourses();
                case "2" -> registerCourse();
                case "3" -> dropCourse();
                case "4" -> viewRegistrations();
                case "5" -> student.getTimetable().viewSchedule();
                case "0" -> running = false;
                default  -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewCourses() {
        System.out.println("\n=== Available Courses ===");
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

    private void registerCourse() {
        viewCourses();
        System.out.print("\nEnter Course ID to register: ");
        String  id     = sc.nextLine().trim();
        Course  course = store.findCourseById(id).orElse(null);
        if (course == null) { System.out.println("[FAIL] Course not found."); return; }
        service.register(student, course);
    }

    private void dropCourse() {
        viewRegistrations();
        System.out.print("\nEnter Course ID to drop: ");
        String id     = sc.nextLine().trim();
        Course course = store.findCourseById(id).orElse(null);
        if (course == null) { System.out.println("[FAIL] Course not found."); return; }
        service.drop(student, course);
    }

    private void viewRegistrations() {
        System.out.println("\n=== My Registrations: " + student.getName() + " ===");
        var regs = student.getRegistrations();
        if (regs.isEmpty()) { System.out.println("  (none)"); return; }
        regs.forEach(r -> System.out.println("  " + r));
        System.out.printf("  Confirmed credits: %d / %d%n",
                student.getTotalCredits(), student.getMaxCredits());
    }
}