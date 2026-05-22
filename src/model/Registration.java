package model;

public class Registration {
    private final Student student;
    private final Course  course;
    private       String  status;   // "Confirmed" | "Dropped"

    public Registration(Student student, Course course, String status) {
        this.student = student;
        this.course  = course;
        this.status  = status;
    }

    public Student getStudent() { return student; }
    public Course  getCourse()  { return course; }
    public String  getStatus()  { return status; }
    public void    setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-34s  Status: %s", course.getCourseName(), status);
    }
}