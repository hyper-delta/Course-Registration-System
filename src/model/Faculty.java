package model;

import java.util.ArrayList;
import java.util.List;

public class Faculty extends User {
    private final String       facultyId;
    private final String       department;
    private final List<Course> assignedCourses = new ArrayList<>();

    public Faculty(String userId, String name, String email,
                   String facultyId, String department) {
        super(userId, name, email);
        this.facultyId  = facultyId;
        this.department = department;
    }

    @Override
    public void login() {
        System.out.printf("%nFaculty %s | %s | %s logged in.%n", name, facultyId, department);
    }

    public void assignToCourse(Course course) {
        if (!assignedCourses.contains(course)) assignedCourses.add(course);
    }

    public void viewEnrolledStudents(Course course) {
        System.out.println("\n+------ Enrolled Students: " + course.getCourseName() + " ------+");
        List<Student> students = course.getEnrolledStudents();
        if (students.isEmpty()) {
            System.out.println("|  (no students enrolled)");
        } else {
            students.forEach(s -> System.out.printf("|  %-10s  %s%n", s.getUserId(), s.getName()));
        }
        System.out.println("+------ Total: " + students.size() + " ------+");
    }

    public String       getFacultyId()       { return facultyId; }
    public String       getDepartment()      { return department; }
    public List<Course> getAssignedCourses() { return assignedCourses; }
}