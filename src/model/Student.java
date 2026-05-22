package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private final int                 maxCredits;
    private final List<Registration>  registrations = new ArrayList<>();
    private final Timetable           timetable;       // Composition

    public Student(String userId, String name, String email, int maxCredits) {
        super(userId, name, email);
        this.maxCredits = maxCredits;
        this.timetable  = new Timetable(this);         // composed here, lives and dies with Student
    }

    @Override
    public void login() {
        System.out.printf("%nStudent %s (%s) logged in.%n", name, userId);
    }

    /** Sum of credits from all Confirmed registrations. */
    public int getTotalCredits() {
        return registrations.stream()
                .filter(r -> "Confirmed".equals(r.getStatus()))
                .mapToInt(r -> r.getCourse().getCredits())
                .sum();
    }

    /** True if the student has a Confirmed registration for this course (prerequisite check). */
    public boolean hasCompleted(Course course) {
        return registrations.stream()
                .anyMatch(r -> r.getCourse().equals(course)
                        && "Confirmed".equals(r.getStatus()));
    }

    // Called by RegistrationService after all validations pass
    public void addRegistration(Registration reg) {
        registrations.add(reg);
        timetable.addCourse(reg.getCourse());
    }

    public void removeRegistration(Registration reg) {
        registrations.remove(reg);
        timetable.removeCourse(reg.getCourse());
    }

    public int               getMaxCredits()    { return maxCredits; }
    public List<Registration> getRegistrations() { return registrations; }
    public Timetable         getTimetable()      { return timetable; }
}