package service;

import exceptions.*;
import interfaces.Registrable;
import model.*;
import store.DataStore;

public class RegistrationService implements Registrable {

    private final DataStore dataStore;

    public RegistrationService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // ── Validation (throws exceptions instead of returning boolean) ────────

    public void validatePrerequisites(Student student, Course course) {
        for (Course prereq : course.getPrerequisites()) {
            if (!student.hasCompleted(prereq)) {
                throw new PrerequisiteNotMetException(prereq.getCourseName());
            }
        }
        System.out.println("  [OK] Prerequisites satisfied.");
    }

    public void checkCreditLimit(Student student, Course course) {
        int current = student.getTotalCredits();
        int adding  = course.getCredits();
        int max     = student.getMaxCredits();
        if (current + adding > max) {
            throw new CreditLimitExceededException(current, adding, max);
        }
        System.out.printf("  [OK] Credits: %d + %d = %d (max %d)%n",
                current, adding, current + adding, max);
    }

    // ── Core operations ────────────────────────────────────────────────────

    @Override
    public void register(Student student, Course course) {
        System.out.printf("%n[REGISTER] %s -> %s%n", student.getName(), course.getCourseName());

        // These run outside the lock — they only read from the student's own data
        boolean alreadyIn = student.getRegistrations().stream()
                .anyMatch(r -> r.getCourse().equals(course)
                        && r.getStatus() == RegistrationStatus.CONFIRMED);
        if (alreadyIn) {
            throw new CRSException("Already registered for: " + course.getCourseName());
        }

        validatePrerequisites(student, course);  // throws PrerequisiteNotMetException
        checkCreditLimit(student, course);        // throws CreditLimitExceededException

        // ── Synchronized on the specific course object ────────────────────
        // Allows concurrent registrations for DIFFERENT courses,
        // while protecting seat count for THIS course atomically.
        synchronized (course) {
            if (course.getAvailableSeats() <= 0) {
                // Course full — add to waitlist instead of rejecting outright
                course.addToWaitlist(student);
                System.out.println("  [WAITLIST] No seats left. "
                        + student.getName() + " added to waitlist (position "
                        + course.getWaitlist().size() + ").");
                return;
            }

            // CHECK + ACT are atomic inside this synchronized block
            Registration reg = new Registration(student, course, RegistrationStatus.CONFIRMED);
            dataStore.addRegistration(reg);
            student.addRegistration(reg);
            course.enrollStudent(student);
        }

        System.out.println("  [SUCCESS] Registration confirmed!");
    }

    @Override
    public void drop(Student student, Course course) {
        System.out.printf("%n[DROP] %s -> %s%n", student.getName(), course.getCourseName());

        Registration reg = student.getRegistrations().stream()
                .filter(r -> r.getCourse().equals(course)
                        && r.getStatus() == RegistrationStatus.CONFIRMED)
                .findFirst()
                .orElseThrow(() -> new CRSException(
                        "No active registration for: " + course.getCourseName()));

        synchronized (course) {
            reg.setStatus(RegistrationStatus.DROPPED);
            student.removeRegistration(reg);
            course.unenrollStudent(student);

            // ── Auto-enroll next student from waitlist ─────────────────────
            Student next = course.pollWaitlist();
            if (next != null) {
                Registration waitlistReg =
                        new Registration(next, course, RegistrationStatus.CONFIRMED);
                dataStore.addRegistration(waitlistReg);
                next.addRegistration(waitlistReg);
                course.enrollStudent(next);
                System.out.println("  [WAITLIST] " + next.getName()
                        + " automatically enrolled from waitlist!");
            }
        }

        System.out.println("  [SUCCESS] Course dropped.");
    }
}