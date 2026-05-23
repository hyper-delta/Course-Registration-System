package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends User {
    private final int                maxCredits;
    private final List<Registration> registrations   = new ArrayList<>();
    private final List<Course>       completedCourses = new ArrayList<>(); // NEW
    private final Timetable          timetable;

    public Student(String userId, String name, String email,
                   String password, int maxCredits) {
        super(userId, name, email, password);
        this.maxCredits = maxCredits;
        this.timetable  = new Timetable(this);
    }

    @Override
    public void login() {
        System.out.printf("%nStudent %s (%s) logged in.%n", name, userId);
    }

    public int getTotalCredits() {
        return registrations.stream()
                .filter(r -> r.getStatus() == RegistrationStatus.CONFIRMED)
                .mapToInt(r -> r.getCourse().getCredits())
                .sum();
    }

    /**
     * Checks the COMPLETED courses list — not active registrations.
     * A student must have FINISHED a course for it to count as a prerequisite.
     */
    public boolean hasCompleted(Course course) {
        return completedCourses.contains(course);
    }

    /** Admin marks a course as completed for this student. */
    public void markCompleted(Course course) {
        if (!completedCourses.contains(course)) {
            completedCourses.add(course);
            System.out.printf("  [INFO] %s marked as completed for %s.%n",
                    course.getCourseName(), name);
        }
    }

    public void addRegistration(Registration reg) {
        registrations.add(reg);
        timetable.addCourse(reg.getCourse());
    }

    public void removeRegistration(Registration reg) {
        registrations.remove(reg);
        timetable.removeCourse(reg.getCourse());
    }

    /** For concurrency test reset — removes all registrations for one course. */
    public void clearRegistrationsForCourse(Course course) {
        registrations.removeIf(r -> r.getCourse().equals(course));
        timetable.removeCourse(course);
    }

    public int                    getMaxCredits()      { return maxCredits; }
    public List<Registration>     getRegistrations()   { return Collections.unmodifiableList(registrations); }
    public List<Course>           getCompletedCourses(){ return Collections.unmodifiableList(completedCourses); }
    public Timetable              getTimetable()       { return timetable; }
}