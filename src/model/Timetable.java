package model;

import java.util.ArrayList;
import java.util.List;

public class Timetable {
    private final Student      owner;
    private final List<Course> courses = new ArrayList<>();

    Timetable(Student owner) { // package-private: only Student can create it
        this.owner = owner;
    }

    void addCourse(Course c)    { if (!courses.contains(c)) courses.add(c); }
    void removeCourse(Course c) { courses.remove(c); }

    public void viewSchedule() {
    System.out.println("\n+------ Timetable: " + owner.getName() + " ------+");

    // Current semester
    if (courses.isEmpty()) {
        System.out.println("|  (no active courses this semester)");
    } else {
        courses.forEach(c -> System.out.println("|  " + c));
    }
    System.out.printf("+------ Active Credits: %d / %d ------+%n",
            owner.getTotalCredits(), owner.getMaxCredits());

    // Academic transcript
    var completed = owner.getCompletedCourses();
    if (!completed.isEmpty()) {
        System.out.println("|  -- Transcript --");
        completed.forEach(c -> System.out.println(
            "|  [DONE] " + c.getCourseName()));
    }
}
}