package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private final String courseId;
    private final String courseName;
    private final int    credits;
    private final int    maxSeats;

    private List<Student> enrolledStudents = new ArrayList<>();
    private List<Course>  prerequisites    = new ArrayList<>();  // self-referential
    private Faculty       faculty;

    public Course(String courseId, String courseName, int credits, int maxSeats) {
        this.courseId   = courseId;
        this.courseName = courseName;
        this.credits    = credits;
        this.maxSeats   = maxSeats;
    }

    public int  getAvailableSeats()        { return maxSeats - enrolledStudents.size(); }
    public void enrollStudent(Student s)   { enrolledStudents.add(s); }
    public void unenrollStudent(Student s) { enrolledStudents.remove(s); }

    public void addPrerequisite(Course c) {
        if (!prerequisites.contains(c)) prerequisites.add(c);
    }

    public void assignFaculty(Faculty f) { this.faculty = f; }

    public String        getCourseId()         { return courseId; }
    public String        getCourseName()       { return courseName; }
    public int           getCredits()          { return credits; }
    public int           getMaxSeats()         { return maxSeats; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public List<Course>  getPrerequisites()    { return prerequisites; }
    public Faculty       getFaculty()          { return faculty; }

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
        return String.format("[%-6s] %-32s | %d cr | Seats: %d/%-2d | Faculty: %s",
                courseId, courseName, credits, getAvailableSeats(), maxSeats, fac);
    }
}