package gui;

import exceptions.CRSException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import java.util.List;
import java.util.function.Function;

public class StudentController {

    private static Student student;
    private static BorderPane root;
    private static Button activeNavBtn;

    public static Scene getScene(Student s) {
        student = s;

        root = new BorderPane();
        root.getStyleClass().add("root");

        root.setLeft(buildSidebar());

        showDashboard();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(MainApp.getCss());

        return scene;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private static VBox buildSidebar() {

        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        VBox header = new VBox(6);
        header.getStyleClass().add("sidebar-header");

        Label logo = new Label("🎓 CRS");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label name = new Label(student.getName());
        name.setStyle("-fx-text-fill: #4ecca3; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label role = new Label("Student · " + student.getUserId());
        role.setStyle("-fx-text-fill: #a8a8b3; -fx-font-size: 11px;");

        header.getChildren().addAll(logo, name, role);

        Button dashBtn = navBtn("🏠 Dashboard", StudentController::showDashboard);
        Button catalogBtn = navBtn("📚 Course Catalog", StudentController::showCatalog);
        Button regBtn = navBtn("📋 My Registrations", StudentController::showMyRegistrations);
        Button ttBtn = navBtn("🗓 Timetable", StudentController::showTimetable);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⎋ Logout");
        logoutBtn.getStyleClass().add("sidebar-item");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setStyle("-fx-text-fill: #e94560;");
        logoutBtn.setOnAction(e -> MainApp.logout());

        sidebar.getChildren().addAll(
                header,
                dashBtn,
                catalogBtn,
                regBtn,
                ttBtn,
                spacer,
                logoutBtn
        );

        setActive(dashBtn);

        return sidebar;
    }

    private static Button navBtn(String text, Runnable action) {

        Button btn = new Button(text);

        btn.getStyleClass().add("sidebar-item");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setOnAction(e -> {
            setActive(btn);
            action.run();
        });

        return btn;
    }

    private static void setActive(Button btn) {

        if (activeNavBtn != null) {
            activeNavBtn.getStyleClass().remove("sidebar-item-active");
        }

        btn.getStyleClass().add("sidebar-item-active");
        activeNavBtn = btn;
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private static void showDashboard() {

        ScrollPane scroll = styledScroll();
        VBox content = contentBox();

        Label title = new Label("Welcome back, " + student.getName() + " 👋");
        title.getStyleClass().add("dashboard-title");

        Label subtitle = new Label("Academic Overview");
        subtitle.getStyleClass().add("subtitle-label");

        HBox stats = new HBox(16);

        long enrolled = student.getRegistrations().stream()
                .filter(r -> r.getStatus() == RegistrationStatus.CONFIRMED)
                .count();

        long waitlisted = MainApp.store.getCourses().stream()
                .filter(c -> c.getWaitlist().contains(student))
                .count();

        stats.getChildren().addAll(
                statCard(student.getTotalCredits() + "/" + student.getMaxCredits(),
                        "Credits", "#e94560"),

                statCard(String.valueOf(enrolled),
                        "Enrolled", "#4ecca3"),

                statCard(String.valueOf(waitlisted),
                        "Waitlisted", "#f5a623"),

                statCard(String.valueOf(student.getCompletedCourses().size()),
                        "Completed", "#a8a8b3")
        );

        for (Node n : stats.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }

        TableView<Registration> regTable = registrationTable(false);
        regTable.setMaxHeight(220);

        content.getChildren().addAll(
                new VBox(4, title, subtitle),
                stats,
                regTable
        );

        scroll.setContent(content);

        root.setCenter(scroll);
    }

    // =========================================================
    // COURSE CATALOG
    // =========================================================

    private static void showCatalog() {

        VBox content = contentBox();

        Label title = new Label("Course Catalog");
        title.getStyleClass().add("dashboard-title");

        Label sub = new Label("Register / Drop Courses");
        sub.getStyleClass().add("subtitle-label");

        TextField search = new TextField();
        search.setPromptText("🔍 Search course...");
        search.getStyleClass().add("input-field");
        search.setMaxWidth(350);

        TableView<Course> table = catalogTable();

        List<Course> allCourses = MainApp.store.getCourses();

        search.textProperty().addListener((obs, oldVal, query) -> {

            String q = query.toLowerCase().trim();

            if (q.isEmpty()) {
                table.setItems(FXCollections.observableArrayList(allCourses));
                return;
            }

            table.setItems(FXCollections.observableArrayList(
                    allCourses.stream()
                            .filter(c ->
                                    c.getCourseId().toLowerCase().contains(q)
                                            || c.getCourseName().toLowerCase().contains(q))
                            .toList()
            ));
        });

        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(
                new VBox(4, title, sub),
                search,
                table
        );

        root.setCenter(content);
    }

    // =========================================================
    // MY REGISTRATIONS
    // =========================================================

    private static void showMyRegistrations() {

        VBox content = contentBox();

        Label title = new Label("My Registrations");
        title.getStyleClass().add("dashboard-title");

        TableView<Registration> table = registrationTable(true);

        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(title, table);

        root.setCenter(content);
    }

    // =========================================================
    // TIMETABLE
    // =========================================================

    private static void showTimetable() {

        VBox content = contentBox();

        Label title = new Label("My Timetable");
        title.getStyleClass().add("dashboard-title");

        VBox cards = new VBox(12);

        List<Registration> confirmed = student.getRegistrations().stream()
                .filter(r -> r.getStatus() == RegistrationStatus.CONFIRMED)
                .toList();

        if (confirmed.isEmpty()) {

            Label empty = new Label("No active courses.");
            empty.getStyleClass().add("label-muted");

            cards.getChildren().add(empty);

        } else {

            confirmed.forEach(r ->
                    cards.getChildren().add(
                            courseCard(r.getCourse(), "#4ecca3", "ENROLLED")
                    )
            );
        }

        content.getChildren().addAll(title, cards);

        root.setCenter(content);
    }

    // =========================================================
    // TABLES
    // =========================================================

    private static TableView<Course> catalogTable() {

        TableView<Course> table = new TableView<>();

        table.getStyleClass().add("table-view");

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(FXCollections.observableArrayList(MainApp.store.getCourses()));

        TableColumn<Course, String> idCol =
                strCol("ID", 80, Course::getCourseId);

        TableColumn<Course, String> nameCol =
                strCol("Course Name", 0, Course::getCourseName);

        TableColumn<Course, String> creditCol =
                strCol("Credits", 80,
                        c -> String.valueOf(c.getCredits()));

        TableColumn<Course, String> seatsCol =
                strCol("Seats", 100,
                        c -> c.getAvailableSeats() + "/" + c.getMaxSeats());

        TableColumn<Course, String> facultyCol =
                strCol("Faculty", 0,
                        c -> c.getFaculty() != null
                                ? c.getFaculty().getName()
                                : "—");

        TableColumn<Course, String> prereqCol =
                strCol("Prerequisites", 0, c -> {

                    List<Course> prereqs = c.getPrerequisites();

                    return prereqs.isEmpty()
                            ? "None"
                            : prereqs.stream()
                            .map(Course::getCourseId)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("");
                });

        TableColumn<Course, Void> actionCol = new TableColumn<>("Action");

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button();

            {
                btn.setOnAction(e -> {

                    Course course = getTableView().getItems().get(getIndex());

                    handleAction(course);

                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Course course = getTableView().getItems().get(getIndex());

                styleBtn(btn, course);

                setGraphic(btn);
            }
        });

        table.getColumns().addAll(
                idCol,
                nameCol,
                creditCol,
                seatsCol,
                facultyCol,
                prereqCol,
                actionCol
        );

        return table;
    }

    private static TableView<Registration> registrationTable(boolean withDrop) {

        TableView<Registration> table = new TableView<>();

        table.getStyleClass().add("table-view");

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(FXCollections.observableArrayList(student.getRegistrations()));

        TableColumn<Registration, String> nameCol =
                strCol("Course Name", 0,
                        r -> r.getCourse().getCourseName());

        TableColumn<Registration, String> creditCol =
                strCol("Credits", 80,
                        r -> String.valueOf(r.getCourse().getCredits()));

        TableColumn<Registration, String> statusCol =
                strCol("Status", 120,
                        r -> r.getStatus().name());

        table.getColumns().addAll(nameCol, creditCol, statusCol);

        if (withDrop) {

            TableColumn<Registration, Void> dropCol = new TableColumn<>("Action");

            dropCol.setCellFactory(col -> new TableCell<>() {

                private final Button dropBtn = new Button("Drop");

                {
                    dropBtn.setOnAction(e -> {

                        Registration reg =
                                getTableView().getItems().get(getIndex());

                        try {

                            MainApp.service.drop(student, reg.getCourse());

                            showMyRegistrations();

                        } catch (CRSException ex) {

                            alert("Drop Failed", ex.getMessage());
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {

                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        return;
                    }

                    Registration reg =
                            getTableView().getItems().get(getIndex());

                    if (reg.getStatus() == RegistrationStatus.CONFIRMED) {
                        setGraphic(dropBtn);
                    } else {
                        setGraphic(null);
                    }
                }
            });

            table.getColumns().add(dropCol);
        }

        return table;
    }

    // =========================================================
    // COMPONENTS
    // =========================================================

    private static VBox statCard(String value, String label, String color) {

        Label val = new Label(value);

        val.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;"
        );

        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");

        VBox card = new VBox(6, val, lbl);

        card.getStyleClass().add("info-card");

        return card;
    }

    private static HBox courseCard(Course c, String color, String chipText) {

        Label name = new Label(c.getCourseName());

        name.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        Label detail = new Label(
                c.getCourseId()
                        + " • "
                        + c.getCredits()
                        + " credits"
        );

        detail.getStyleClass().add("label-muted");

        VBox info = new VBox(4, name, detail);

        Label chip = new Label(chipText);

        chip.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 6 12;" +
                        "-fx-background-radius: 10;"
        );

        HBox card = new HBox(20, info, chip);

        card.setAlignment(Pos.CENTER_LEFT);

        card.getStyleClass().add("info-card");

        return card;
    }

    // =========================================================
    // ACTIONS
    // =========================================================

    private static void styleBtn(Button btn, Course course) {

        btn.setDisable(false);

        if (student.hasCompleted(course)) {

            btn.setText("Completed");

            btn.setDisable(true);

            return;
        }

        boolean registered = student.getRegistrations().stream()
                .anyMatch(r ->
                        r.getCourse().equals(course)
                                && r.getStatus() == RegistrationStatus.CONFIRMED);

        if (registered) {

            btn.setText("Drop");

        } else if (course.getWaitlist().contains(student)) {

            btn.setText("Waitlisted");

            btn.setDisable(true);

        } else {

            btn.setText("Register");
        }
    }

    private static void handleAction(Course course) {

        boolean registered = student.getRegistrations().stream()
                .anyMatch(r ->
                        r.getCourse().equals(course)
                                && r.getStatus() == RegistrationStatus.CONFIRMED);

        try {

            if (registered) {
                MainApp.service.drop(student, course);
            } else {
                MainApp.service.register(student, course);
            }

        } catch (CRSException e) {

            alert("Error", e.getMessage());
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private static <S> TableColumn<S, String> strCol(
            String title,
            double width,
            Function<S, String> mapper) {

        TableColumn<S, String> col = new TableColumn<>(title);

        col.setCellValueFactory(data ->
                new SimpleStringProperty(mapper.apply(data.getValue())));

        if (width > 0) {
            col.setMinWidth(width);
            col.setMaxWidth(width);
        }

        return col;
    }

    private static ScrollPane styledScroll() {

        ScrollPane sp = new ScrollPane();

        sp.setFitToWidth(true);

        return sp;
    }

    private static VBox contentBox() {

        VBox box = new VBox(20);

        box.setPadding(new Insets(30));

        box.getStyleClass().add("content-area");

        return box;
    }

    private static void alert(String title, String msg) {

        Alert a = new Alert(Alert.AlertType.ERROR);

        a.setTitle(title);

        a.setHeaderText(null);

        a.setContentText(msg);

        a.showAndWait();
    }
}