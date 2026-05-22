package service;

import model.Course;
import model.Student;

public interface Registrable {
    void register(Student student, Course course);
    void drop(Student student, Course course);
}