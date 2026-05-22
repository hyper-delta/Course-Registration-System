package store;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Central in-memory repository — replaces a database for this project. */
public class DataStore {

    private final List<Course>       courses       = new ArrayList<>();
    private final List<Student>      students      = new ArrayList<>();
    private final List<Faculty>      faculty       = new ArrayList<>();
    private final List<Admin>        admins        = new ArrayList<>();
    private final List<Registration> registrations = new ArrayList<>();

    public void addCourse(Course c)           { courses.add(c); }
    public List<Course> getCourses()          { return courses; }
    public Optional<Course> findCourseById(String id) {
        return courses.stream()
                .filter(c -> c.getCourseId().equalsIgnoreCase(id))
                .findFirst();
    }

    public void addStudent(Student s)         { students.add(s); }
    public List<Student> getStudents()        { return students; }
    public Optional<Student> findStudentById(String id) {
        return students.stream()
                .filter(s -> s.getUserId().equalsIgnoreCase(id))
                .findFirst();
    }

    public void addFaculty(Faculty f)         { faculty.add(f); }
    public List<Faculty> getFaculty()         { return faculty; }
    public Optional<Faculty> findFacultyById(String id) {
        return faculty.stream()
                .filter(f -> f.getFacultyId().equalsIgnoreCase(id)
                        || f.getUserId().equalsIgnoreCase(id))
                .findFirst();
    }

    public void addAdmin(Admin a)             { admins.add(a); }
    public List<Admin> getAdmins()            { return admins; }
    public Optional<Admin> findAdminById(String id) {
        return admins.stream()
                .filter(a -> a.getUserId().equalsIgnoreCase(id))
                .findFirst();
    }

    public void addRegistration(Registration r) { registrations.add(r); }
    public List<Registration> getRegistrations() { return registrations; }
}