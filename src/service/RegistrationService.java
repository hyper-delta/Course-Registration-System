package service;

import model.*;
import store.DataStore;

import java.util.List;

public class RegistrationService implements Registrable {

    private final DataStore dataStore;

    public RegistrationService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // ── Step 1: check all prerequisites are completed ──────────────────────
    public boolean validatePrerequisites(Student student, Course course) {
        List<Course> prereqs = course.getPrerequisites();
        if (prereqs.isEmpty()) {
            System.out.println("  [OK] No prerequisites required.");
            return true;
        }
        for (Course prereq : prereqs) {
            if (!student.hasCompleted(prereq)) {
                System.out.println("  [FAIL] Prerequisite not met: " + prereq.getCourseName());
                return false;
            }
        }
        System.out.println("  [OK] Prerequisites satisfied.");
        return true;
    }

    // ── Step 2: at least one seat free ─────────────────────────────────────
    public boolean checkSeatAvailability(Course course) {
        int seats = course.getAvailableSeats();
        if (seats > 0) {
            System.out.println("  [OK] Seats available: " + seats);
            return true;
        }
        System.out.println("  [FAIL] No seats available.");
        return false;
    }

    // ── Step 3: adding credits won't exceed the student's limit ────────────
    public boolean checkCreditLimit(Student student, Course course) {
        int current = student.getTotalCredits();
        int adding  = course.getCredits();
        int max     = student.getMaxCredits();
        if (current + adding <= max) {
            System.out.printf("  [OK] Credits: %d + %d = %d (max %d)%n",
                    current, adding, current + adding, max);
            return true;
        }
        System.out.printf("  [FAIL] Credit limit exceeded: %d + %d > %d%n",
                current, adding, max);
        return false;
    }

    // ── Core: synchronized prevents race conditions (Phase 4) ──────────────
    @Override
    public synchronized void register(Student student, Course course) {
        System.out.printf("%n[REGISTER] %s -> %s%n", student.getName(), course.getCourseName());

        boolean alreadyIn = student.getRegistrations().stream()
                .anyMatch(r -> r.getCourse().equals(course)
                        && "Confirmed".equals(r.getStatus()));
        if (alreadyIn) {
            System.out.println("  [FAIL] Already registered for this course.");
            return;
        }

        if (!validatePrerequisites(student, course)) return;
        if (!checkSeatAvailability(course))          return;
        if (!checkCreditLimit(student, course))      return;

        Registration reg = new Registration(student, course, "Confirmed");
        dataStore.addRegistration(reg);
        student.addRegistration(reg);
        course.enrollStudent(student);

        System.out.println("  [SUCCESS] Registration confirmed!");
    }

    @Override
    public synchronized void drop(Student student, Course course) {
        System.out.printf("%n[DROP] %s -> %s%n", student.getName(), course.getCourseName());

        Registration reg = student.getRegistrations().stream()
                .filter(r -> r.getCourse().equals(course)
                        && "Confirmed".equals(r.getStatus()))
                .findFirst().orElse(null);

        if (reg == null) {
            System.out.println("  [FAIL] No active registration found for this course.");
            return;
        }

        reg.setStatus("Dropped");
        student.removeRegistration(reg);
        course.unenrollStudent(student);
        System.out.println("  [SUCCESS] Course dropped.");
    }
}