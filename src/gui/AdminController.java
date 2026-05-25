package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminController {

    private static Admin      admin;
    private static BorderPane root;
    private static Button     activeNavBtn;

    public static Scene getScene(Admin a) {
        admin = a;
        root  = new BorderPane();
        root.getStyleClass().add("root");
        root.setLeft(buildSidebar());
        showDashboard();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(MainApp.getCss());
        return scene;
    }

    // ═══════════════════════════════════════════════════════════
    //  SIDEBAR
    // ═══════════════════════════════════════════════════════════

    private static VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        VBox header = new VBox(6);
        header.getStyleClass().add("sidebar-header");

        Label logo = new Label("🎓 CRS");
        logo.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label name = new Label(admin.getName());
        name.setStyle("-fx-text-fill: #e94560; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label role = new Label("Admin  ·  Level: " + admin.getAdminLevel());
        role.setStyle("-fx-text-fill: #a8a8b3; -fx-font-size: 11px;");
        header.getChildren().addAll(logo, name, role);

        Button dashBtn    = navBtn("🏠   Dashboard",          () -> showDashboard());
        Button coursesBtn = navBtn("📚   Manage Courses",     () -> showManageCourses());
        Button assignBtn  = navBtn("◆   Assign Faculty", () -> showAssignFaculty());
        Button prereqBtn  = navBtn("➜   Set Prerequisites",  () -> showSetPrerequisites());
        Button markBtn    = navBtn("🎓   Mark Completed",     () -> showMarkCompleted());
        Button addStuBtn  = navBtn("+   Add Student",        () -> showAddStudent());
        Button addFacBtn  = navBtn("+   Add Faculty",        () -> showAddFaculty());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⎋   Logout");
        logoutBtn.getStyleClass().add("sidebar-item");
        logoutBtn.setStyle("-fx-text-fill: #e94560;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setOnAction(e -> MainApp.logout());
        VBox.setMargin(logoutBtn, new Insets(0, 0, 16, 0));

        sidebar.getChildren().addAll(
                header, dashBtn, coursesBtn, assignBtn,
                prereqBtn, markBtn, addStuBtn, addFacBtn,
                spacer, logoutBtn);

        setActive(dashBtn);
        return sidebar;
    }

    private static Button navBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("sidebar-item");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setOnAction(e -> { setActive(btn); action.run(); });
        return btn;
    }

    private static void setActive(Button btn) {
        if (activeNavBtn != null) activeNavBtn.getStyleClass().remove("sidebar-item-active");
        btn.getStyleClass().add("sidebar-item-active");
        activeNavBtn = btn;
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 1 — DASHBOARD
    // ═══════════════════════════════════════════════════════════

    private static void showDashboard() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Admin Dashboard");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("System-wide overview  ·  " + admin.getName());
        sub.getStyleClass().add("subtitle-label");

        long confirmedRegs = MainApp.store.getRegistrations().stream()
                .filter(r -> r.getStatus() == RegistrationStatus.CONFIRMED).count();

        HBox statsRow = new HBox(16,
                statCard(String.valueOf(MainApp.store.getCourses().size()),
                        "Total Courses", "#e94560"),
                statCard(String.valueOf(MainApp.store.getStudents().size()),
                        "Students", "#4ecca3"),
                statCard(String.valueOf(MainApp.store.getFaculty().size()),
                        "Faculty Members", "#f5a623"),
                statCard(String.valueOf(confirmedRegs),
                        "Confirmed Registrations", "#a8a8b3")
        );
        for (Node n : statsRow.getChildren()) HBox.setHgrow(n, Priority.ALWAYS);

        Label tableHdr = new Label("Courses Overview");
        tableHdr.getStyleClass().add("card-title");
        VBox.setMargin(tableHdr, new Insets(10, 0, 0, 0));

        TableView<Course> table = overviewTable();
        table.setMaxHeight(320);

        content.getChildren().addAll(new VBox(4, title, sub), statsRow, tableHdr, table);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 2 — MANAGE COURSES
    // ═══════════════════════════════════════════════════════════

    private static void showManageCourses() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Manage Courses");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Add new courses and view the full catalog");
        sub.getStyleClass().add("subtitle-label");

        TextField idField    = formField("Course ID  (e.g. CS401)");
        TextField nameField  = formField("Course Name");
        TextField credField  = formField("Credits  (e.g. 3)");
        TextField seatsField = formField("Max Seats  (e.g. 30)");
        Label feedback       = feedbackLabel();

        Button addBtn = primaryBtn("ADD COURSE");
        addBtn.setOnAction(e -> {
            String id   = idField.getText().trim();
            String name = nameField.getText().trim();
            String cStr = credField.getText().trim();
            String sStr = seatsField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || cStr.isEmpty() || sStr.isEmpty()) {
                setFeedback(feedback, "All fields are required.", false); return;
            }
            if (MainApp.store.findCourseById(id).isPresent()) {
                setFeedback(feedback, "Course ID \"" + id + "\" already exists.", false); return;
            }
            try {
                MainApp.store.addCourse(
                        new Course(id, name, Integer.parseInt(cStr), Integer.parseInt(sStr)));
                setFeedback(feedback, "✓  Course \"" + name + "\" added.", true);
                idField.clear(); nameField.clear(); credField.clear(); seatsField.clear();
                showManageCourses();
            } catch (NumberFormatException ex) {
                setFeedback(feedback, "Credits and seats must be valid numbers.", false);
            }
        });

        VBox form = formCard("Add New Course",
                idField, nameField, credField, seatsField, addBtn, feedback);

        Label tableHdr = new Label("All Courses");
        tableHdr.getStyleClass().add("card-title");
        VBox.setMargin(tableHdr, new Insets(10, 0, 0, 0));

        TableView<Course> table = overviewTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(new VBox(4, title, sub), form, tableHdr, table);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 3 — ASSIGN FACULTY
    // ═══════════════════════════════════════════════════════════

    private static void showAssignFaculty() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Assign Faculty to Course");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Link a faculty member to a course using their IDs below");
        sub.getStyleClass().add("subtitle-label");

        TextField courseField  = formField("Course ID  (e.g. CS201)");
        TextField facultyField = formField("Faculty ID  (e.g. F001)");
        Label feedback = feedbackLabel();

        Button assignBtn = primaryBtn("ASSIGN FACULTY");
        assignBtn.setOnAction(e -> {
            Course  course  = MainApp.store.findCourseById(
                    courseField.getText().trim()).orElse(null);
            Faculty faculty = MainApp.store.findFacultyById(
                    facultyField.getText().trim()).orElse(null);

            if (course  == null) { setFeedback(feedback, "Course not found.",  false); return; }
            if (faculty == null) { setFeedback(feedback, "Faculty not found.", false); return; }

            course.assignFaculty(faculty);
            faculty.assignToCourse(course);
            setFeedback(feedback, "✓  " + faculty.getName()
                    + " assigned to " + course.getCourseName(), true);
            courseField.clear(); facultyField.clear();
            showAssignFaculty();
        });

        VBox form = formCard("Assignment Form", courseField, facultyField, assignBtn, feedback);

        // Reference tables side by side
        TableView<Course>  courseRef = miniCourseTable();
        TableView<Faculty> facRef    = miniFacultyTable();
        courseRef.setMaxHeight(220);
        facRef.setMaxHeight(220);

        VBox courseBox = new VBox(8, sectionLabel("Courses"), courseRef);
        VBox facBox    = new VBox(8, sectionLabel("Faculty"),  facRef);
        HBox.setHgrow(courseBox, Priority.ALWAYS);
        HBox.setHgrow(facBox,    Priority.ALWAYS);

        HBox refRow = new HBox(16, courseBox, facBox);

        content.getChildren().addAll(new VBox(4, title, sub), form, refRow);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 4 — SET PREREQUISITES
    // ═══════════════════════════════════════════════════════════

    private static void showSetPrerequisites() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Set Prerequisites");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Define course dependencies · circular chains are blocked automatically");
        sub.getStyleClass().add("subtitle-label");

        TextField targetField = formField("Target Course ID  (the course that needs a prerequisite)");
        TextField prereqField = formField("Prerequisite Course ID  (must be completed first)");
        Label feedback = feedbackLabel();

        Button setBtn = primaryBtn("SET PREREQUISITE");
        setBtn.setOnAction(e -> {
            Course target = MainApp.store.findCourseById(
                    targetField.getText().trim()).orElse(null);
            Course prereq = MainApp.store.findCourseById(
                    prereqField.getText().trim()).orElse(null);

            if (target == null) { setFeedback(feedback, "Target course not found.",       false); return; }
            if (prereq == null) { setFeedback(feedback, "Prerequisite course not found.", false); return; }
            if (target.equals(prereq)) {
                setFeedback(feedback, "A course cannot be its own prerequisite.", false); return;
            }
            if (wouldCreateCycle(target, prereq)) {
                setFeedback(feedback,
                        "⚠  Circular dependency blocked!  " + prereq.getCourseId()
                        + " already depends on " + target.getCourseId()
                        + " (directly or indirectly).", false);
                return;
            }

            target.addPrerequisite(prereq);
            setFeedback(feedback, "✓  "
                    + prereq.getCourseId() + "  →  prerequisite for  →  "
                    + target.getCourseId(), true);
            targetField.clear(); prereqField.clear();
            showSetPrerequisites();
        });

        VBox form = formCard("Prerequisite Form", targetField, prereqField, setBtn, feedback);

        // Prerequisite chain visualizer
        Label chainHdr = new Label("Current Prerequisite Chains");
        chainHdr.getStyleClass().add("card-title");
        VBox.setMargin(chainHdr, new Insets(10, 0, 0, 0));

        VBox chainBox = new VBox(10);
        chainBox.getStyleClass().add("info-card");
        boolean any = false;
        for (Course c : MainApp.store.getCourses()) {
            if (!c.getPrerequisites().isEmpty()) {
                any = true;
                Label line = new Label(buildChain(c));
                line.setStyle("-fx-text-fill: #4ecca3; -fx-font-size: 13px;");
                chainBox.getChildren().add(line);
            }
        }
        if (!any) {
            Label none = new Label("No prerequisites defined yet.");
            none.getStyleClass().add("label-muted");
            chainBox.getChildren().add(none);
        }

        content.getChildren().addAll(
                new VBox(4, title, sub), form, chainHdr, chainBox);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    private static String buildChain(Course course) {
        StringBuilder sb = new StringBuilder(course.getCourseId());
        for (Course p : course.getPrerequisites())
            sb.append("  ←  ").append(p.getCourseId());
        return sb.toString();
    }

    private static boolean wouldCreateCycle(Course target, Course newPrereq) {
        return isReachable(newPrereq, target, new HashSet<>());
    }

    private static boolean isReachable(Course from, Course goal, Set<String> visited) {
        if (from.getCourseId().equals(goal.getCourseId())) return true;
        if (!visited.add(from.getCourseId())) return false;
        for (Course p : from.getPrerequisites())
            if (isReachable(p, goal, visited)) return true;
        return false;
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 5 — MARK COMPLETED
    // ═══════════════════════════════════════════════════════════

    private static void showMarkCompleted() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Mark Course as Completed");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Record that a student has successfully finished a course");
        sub.getStyleClass().add("subtitle-label");

        TextField stuField    = formField("Student ID  (e.g. S001)");
        TextField courseField = formField("Course ID  (e.g. CS101)");
        Label feedback = feedbackLabel();

        Button markBtn = primaryBtn("MARK AS COMPLETED");
        markBtn.setOnAction(e -> {
            Student student = MainApp.store.findStudentById(
                    stuField.getText().trim()).orElse(null);
            Course course = MainApp.store.findCourseById(
                    courseField.getText().trim()).orElse(null);

            if (student == null) { setFeedback(feedback, "Student not found.", false); return; }
            if (course  == null) { setFeedback(feedback, "Course not found.",  false); return; }
            if (student.hasCompleted(course)) {
                setFeedback(feedback, student.getName()
                        + " has already completed " + course.getCourseName(), false);
                return;
            }

            student.markCompleted(course);
            setFeedback(feedback, "✓  " + course.getCourseName()
                    + " marked as completed for " + student.getName(), true);
            stuField.clear(); courseField.clear();
        });

        VBox form = formCard("Completion Form", stuField, courseField, markBtn, feedback);

        TableView<Student> stuTable = miniStudentTable();
        stuTable.setMaxHeight(220);

        content.getChildren().addAll(
                new VBox(4, title, sub), form,
                sectionLabel("Students  (ID reference)"), stuTable);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 6 — ADD STUDENT
    // ═══════════════════════════════════════════════════════════

    private static void showAddStudent() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Add Student Account");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Create a new student login for the system");
        sub.getStyleClass().add("subtitle-label");

        TextField     idField    = formField("Student ID  (e.g. S006)");
        TextField     nameField  = formField("Full Name");
        TextField     emailField = formField("Email Address");
        PasswordField pwdField   = pwdFormField("Password");
        TextField     maxField   = formField("Max Credits  (e.g. 20)");
        Label feedback = feedbackLabel();

        Button addBtn = primaryBtn("CREATE STUDENT");
        addBtn.setOnAction(e -> {
            String id   = idField.getText().trim();
            String name = nameField.getText().trim();
            String em   = emailField.getText().trim();
            String pwd  = pwdField.getText().trim();
            String maxS = maxField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || em.isEmpty()
                    || pwd.isEmpty() || maxS.isEmpty()) {
                setFeedback(feedback, "All fields are required.", false); return;
            }
            if (MainApp.store.findStudentById(id).isPresent()) {
                setFeedback(feedback, "Student ID \"" + id + "\" already exists.", false); return;
            }
            try {
                MainApp.store.addStudent(
                        new Student(id, name, em, pwd, Integer.parseInt(maxS)));
                setFeedback(feedback, "✓  Account created for " + name, true);
                idField.clear(); nameField.clear(); emailField.clear();
                pwdField.clear(); maxField.clear();
                showAddStudent();
            } catch (NumberFormatException ex) {
                setFeedback(feedback, "Max credits must be a valid number.", false);
            }
        });

        VBox form = formCard("New Student",
                idField, nameField, emailField, pwdField, maxField, addBtn, feedback);

        TableView<Student> table = miniStudentTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(
                new VBox(4, title, sub), form,
                sectionLabel("Existing Students"), table);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  VIEW 7 — ADD FACULTY
    // ═══════════════════════════════════════════════════════════

    private static void showAddFaculty() {
        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Add Faculty Account");
        title.getStyleClass().add("dashboard-title");
        Label sub = new Label("Create a new faculty login for the system");
        sub.getStyleClass().add("subtitle-label");

        TextField     uidField  = formField("User ID  (e.g. U003)");
        TextField     nameField = formField("Full Name");
        TextField     emField   = formField("Email Address");
        PasswordField pwdField  = pwdFormField("Password");
        TextField     fidField  = formField("Faculty ID  (e.g. F003)");
        TextField     deptField = formField("Department");
        Label feedback = feedbackLabel();

        Button addBtn = primaryBtn("CREATE FACULTY");
        addBtn.setOnAction(e -> {
            String uid  = uidField.getText().trim();
            String name = nameField.getText().trim();
            String em   = emField.getText().trim();
            String pwd  = pwdField.getText().trim();
            String fid  = fidField.getText().trim();
            String dept = deptField.getText().trim();

            if (uid.isEmpty() || name.isEmpty() || em.isEmpty()
                    || pwd.isEmpty() || fid.isEmpty() || dept.isEmpty()) {
                setFeedback(feedback, "All fields are required.", false); return;
            }
            if (MainApp.store.findFacultyById(fid).isPresent()) {
                setFeedback(feedback, "Faculty ID \"" + fid + "\" already exists.", false); return;
            }

            MainApp.store.addFaculty(new Faculty(uid, name, em, pwd, fid, dept));
            setFeedback(feedback, "✓  Faculty account created for " + name, true);
            uidField.clear(); nameField.clear(); emField.clear();
            pwdField.clear(); fidField.clear(); deptField.clear();
            showAddFaculty();
        });

        VBox form = formCard("New Faculty",
                uidField, nameField, emField, pwdField, fidField, deptField, addBtn, feedback);

        TableView<Faculty> table = miniFacultyTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(
                new VBox(4, title, sub), form,
                sectionLabel("Existing Faculty"), table);
        scroll.setContent(content);
        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    //  TABLE BUILDERS
    // ═══════════════════════════════════════════════════════════

    private static TableView<Course> overviewTable() {
        TableView<Course> t = styledTable();
        t.setItems(FXCollections.observableArrayList(MainApp.store.getCourses()));
        t.setPlaceholder(styledPlaceholder("No courses yet."));
        t.getColumns().addAll(
                strCol("ID",           70, d -> d.getValue().getCourseId()),
                strCol("Name",          0, d -> d.getValue().getCourseName()),
                strCol("Credits",      70, d -> String.valueOf(d.getValue().getCredits())),
                strCol("Seats",        90, d -> d.getValue().getAvailableSeats()
                        + " / " + d.getValue().getMaxSeats()),
                strCol("Enrolled",     80, d -> String.valueOf(
                        d.getValue().getEnrolledStudents().size())),
                strCol("Waitlist",     80, d -> String.valueOf(
                        d.getValue().getWaitlist().size())),
                strCol("Faculty",       0, d -> d.getValue().getFaculty() != null
                        ? d.getValue().getFaculty().getName() : "—"),
                strCol("Prerequisites", 0, d -> {
                    List<Course> p = d.getValue().getPrerequisites();
                    return p.isEmpty() ? "None"
                            : p.stream().map(Course::getCourseId)
                                    .reduce((a, b) -> a + ", " + b).orElse("");
                })
        );
        return t;
    }

    private static TableView<Course> miniCourseTable() {
        TableView<Course> t = styledTable();
        t.setItems(FXCollections.observableArrayList(MainApp.store.getCourses()));
        t.getColumns().addAll(
                strCol("Course ID", 90, d -> d.getValue().getCourseId()),
                strCol("Name",       0, d -> d.getValue().getCourseName()),
                strCol("Faculty",    0, d -> d.getValue().getFaculty() != null
                        ? d.getValue().getFaculty().getName() : "—")
        );
        return t;
    }

    private static TableView<Faculty> miniFacultyTable() {
        TableView<Faculty> t = styledTable();
        t.setItems(FXCollections.observableArrayList(MainApp.store.getFaculty()));
        t.getColumns().addAll(
                strCol("Faculty ID",  90, d -> d.getValue().getFacultyId()),
                strCol("Name",         0, d -> d.getValue().getName()),
                strCol("Department",   0, d -> d.getValue().getDepartment())
        );
        return t;
    }

    private static TableView<Student> miniStudentTable() {
        TableView<Student> t = styledTable();
        t.setItems(FXCollections.observableArrayList(MainApp.store.getStudents()));
        t.getColumns().addAll(
                strCol("Student ID",  90, d -> d.getValue().getUserId()),
                strCol("Name",         0, d -> d.getValue().getName()),
                strCol("Credits",     90, d -> d.getValue().getTotalCredits()
                        + " / " + d.getValue().getMaxCredits()),
                strCol("Completed",   90, d -> String.valueOf(
                        d.getValue().getCompletedCourses().size()))
        );
        return t;
    }

    // ═══════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════

    private static VBox statCard(String value, String label, String color) {
        Label val = new Label(value);
        val.setStyle("-fx-text-fill: " + color
                + "; -fx-font-size: 28px; -fx-font-weight: bold;");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        VBox card = new VBox(6, val, lbl);
        card.getStyleClass().add("info-card");
        card.setAlignment(Pos.CENTER_LEFT);
        return card;
    }

    private static VBox formCard(String heading, Node... nodes) {
        Label head = new Label(heading);
        head.getStyleClass().add("card-title");
        VBox card = new VBox(12);
        card.getStyleClass().add("info-card");
        card.getChildren().add(head);
        card.getChildren().addAll(nodes);
        return card;
    }

    private static TextField formField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("input-field");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private static PasswordField pwdFormField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.getStyleClass().add("input-field");
        pf.setMaxWidth(Double.MAX_VALUE);
        return pf;
    }

    private static Button primaryBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("btn-primary");
        btn.setPrefWidth(220);
        return btn;
    }

    private static Label feedbackLabel() {
        Label lbl = new Label();
        lbl.setVisible(false);
        lbl.setWrapText(true);
        return lbl;
    }

    private static void setFeedback(Label lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.getStyleClass().removeAll("label-success", "label-error");
        lbl.getStyleClass().add(success ? "label-success" : "label-error");
        lbl.setVisible(true);
    }

    private static Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("card-title");
        VBox.setMargin(lbl, new Insets(10, 0, 0, 0));
        return lbl;
    }

    private static <S> TableView<S> styledTable() {
        TableView<S> t = new TableView<>();
        t.getStyleClass().add("table-view");
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return t;
    }

    private static <S> TableColumn<S, String> strCol(
        String title,
        double fixedW,
        javafx.util.Callback<TableColumn.CellDataFeatures<S, String>, String> mapper) {

    TableColumn<S, String> col = new TableColumn<>(title);

    col.setCellValueFactory(data ->
            new SimpleStringProperty(mapper.call(data)));

    if (fixedW > 0) {
        col.setMinWidth(fixedW);
        col.setMaxWidth(fixedW);
    }

    return col;
}

    private static ScrollPane styledScroll() {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.getStyleClass().add("scroll-pane");
        return sp;
    }

    private static VBox contentBox() {
        VBox box = new VBox(20);
        box.getStyleClass().add("content-area");
        box.setPadding(new Insets(32, 36, 32, 36));
        return box;
    }

    private static Label styledPlaceholder(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("label-muted");
        return lbl;
    }
}