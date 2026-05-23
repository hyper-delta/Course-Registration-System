package model;

public class Registration {
    private final Student          student;
    private final Course           course;
    private       RegistrationStatus status;

    public Registration(Student student, Course course, RegistrationStatus status) {
        this.student = student;
        this.course  = course;
        this.status  = status;
    }

    public Student           getStudent() { return student; }
    public Course            getCourse()  { return course; }
    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-34s  Status: %s", course.getCourseName(), status);
    }
}