package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Course {
    private final String       courseId;
    private final String       courseName;
    private final int          credits;
    private final int          maxSeats;

    private final List<Student>  enrolledStudents = new ArrayList<>();
    private final List<Course>   prerequisites    = new ArrayList<>();   // self-referential
    private final Queue<Student> waitlist         = new LinkedList<>();  // NEW
    private Faculty              faculty;

    public Course(String courseId, String courseName, int credits, int maxSeats) {
        this.courseId   = courseId;
        this.courseName = courseName;
        this.credits    = credits;
        this.maxSeats   = maxSeats;
    }

    // ── Seat management ────────────────────────────────────────────────────
    public int     getAvailableSeats()        { return maxSeats - enrolledStudents.size(); }
    public void    enrollStudent(Student s)   { enrolledStudents.add(s); }
    public void    unenrollStudent(Student s) { enrolledStudents.remove(s); }

    // ── Waitlist management ────────────────────────────────────────────────
    public void    addToWaitlist(Student s)   { waitlist.add(s); }
    public Student pollWaitlist()             { return waitlist.poll(); }

    // ── Prerequisite management ────────────────────────────────────────────
    public void addPrerequisite(Course c) {
        if (!prerequisites.contains(c)) prerequisites.add(c);
    }

    // ── Faculty assignment ─────────────────────────────────────────────────
    public void assignFaculty(Faculty f) { this.faculty = f; }

    // ── Test reset (concurrency demo only) ────────────────────────────────
    public void resetForConcurrencyTest() {
        enrolledStudents.clear();
        waitlist.clear();
    }

    // ── Getters (unmodifiable where collections are exposed) ───────────────
    public String        getCourseId()         { return courseId; }
    public String        getCourseName()       { return courseName; }
    public int           getCredits()          { return credits; }
    public int           getMaxSeats()         { return maxSeats; }
    public Faculty       getFaculty()          { return faculty; }
    public List<Student> getEnrolledStudents() { return Collections.unmodifiableList(enrolledStudents); }
    public List<Course>  getPrerequisites()    { return Collections.unmodifiableList(prerequisites); }
    public List<Student> getWaitlist()         { return List.copyOf(new ArrayList<>(waitlist)); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course c)) return false;
        return courseId.equals(c.courseId);
    }

    @Override
    public int hashCode() { return courseId.hashCode(); }

    @Override
    public String toString() {
        String fac = (faculty != null) ? faculty.getName() : "Unassigned";
        return String.format("[%-6s] %-32s | %d cr | Seats: %d/%-2d | Waitlist: %d | Faculty: %s",
                courseId, courseName, credits,
                getAvailableSeats(), maxSeats, waitlist.size(), fac);
    }
}