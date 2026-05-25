package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import java.util.List;

public class FacultyController {

    private static Faculty faculty;
    private static BorderPane root;
    private static Button activeNavBtn;

    public static Scene getScene(Faculty f) {
        faculty = f;
        root = new BorderPane();

        root.getStyleClass().add("root");
        root.setLeft(buildSidebar());

        showDashboard();

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(MainApp.getCss());

        return scene;
    }

    // ═══════════════════════════════════════════════════════════
    // SIDEBAR
    // ═══════════════════════════════════════════════════════════

    private static VBox buildSidebar() {

        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        VBox header = new VBox(6);
        header.getStyleClass().add("sidebar-header");

        Label logo = new Label("🎓 CRS");
        logo.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label name = new Label(faculty.getName());
        name.setStyle(
                "-fx-text-fill: #f5a623;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        Label role = new Label(
                "Faculty  ·  " + faculty.getDepartment()
        );

        role.setStyle(
                "-fx-text-fill: #a8a8b3;" +
                "-fx-font-size: 11px;"
        );

        header.getChildren().addAll(logo, name, role);

        Button dashBtn = navBtn(
                "🏠   Dashboard",
                () -> showDashboard()
        );

        Button studentsBtn = navBtn(
                "☰   Enrolled Students",
                () -> showEnrolledStudents()
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⎋   Logout");

        logoutBtn.getStyleClass().add("sidebar-item");
        logoutBtn.setStyle("-fx-text-fill: #e94560;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);

        logoutBtn.setOnAction(e -> MainApp.logout());

        VBox.setMargin(
                logoutBtn,
                new Insets(0, 0, 16, 0)
        );

        sidebar.getChildren().addAll(
                header,
                dashBtn,
                studentsBtn,
                spacer,
                logoutBtn
        );

        setActive(dashBtn);

        return sidebar;
    }

    private static Button navBtn(
            String text,
            Runnable action
    ) {

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
            activeNavBtn.getStyleClass()
                    .remove("sidebar-item-active");
        }

        btn.getStyleClass().add("sidebar-item-active");

        activeNavBtn = btn;
    }

    // ═══════════════════════════════════════════════════════════
    // DASHBOARD
    // ═══════════════════════════════════════════════════════════

    private static void showDashboard() {

        ScrollPane scroll = styledScroll();

        VBox content = contentBox();

        Label title = new Label(
                "Welcome, " + faculty.getName() + "! 👋"
        );

        title.getStyleClass().add("dashboard-title");

        Label sub = new Label(
                faculty.getDepartment()
                + "  ·  "
                + faculty.getFacultyId()
        );

        sub.getStyleClass().add("subtitle-label");

        List<Course> courses = faculty.getAssignedCourses();

        long totalEnrolled = courses.stream()
                .mapToLong(c -> c.getEnrolledStudents().size())
                .sum();

        long totalWaitlist = courses.stream()
                .mapToLong(c -> c.getWaitlist().size())
                .sum();

        HBox statsRow = new HBox(
                16,

                statCard(
                        String.valueOf(courses.size()),
                        "Assigned Courses",
                        "#f5a623"
                ),

                statCard(
                        String.valueOf(totalEnrolled),
                        "Total Enrolled",
                        "#4ecca3"
                ),

                statCard(
                        String.valueOf(totalWaitlist),
                        "On Waitlist",
                        "#e94560"
                )
        );

        for (javafx.scene.Node n : statsRow.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }

        Label coursesHdr = new Label("My Assigned Courses");
        coursesHdr.getStyleClass().add("card-title");

        VBox.setMargin(
                coursesHdr,
                new Insets(10, 0, 0, 0)
        );

        VBox courseCards = new VBox(12);

        if (courses.isEmpty()) {

            Label empty = new Label(
                    "No courses assigned yet. Contact admin."
            );

            empty.getStyleClass().add("label-muted");

            courseCards.getChildren().add(empty);

        } else {

            for (Course c : courses) {
                courseCards.getChildren()
                        .add(buildCourseCard(c));
            }
        }

        content.getChildren().addAll(
                new VBox(4, title, sub),
                statsRow,
                coursesHdr,
                courseCards
        );

        scroll.setContent(content);

        root.setCenter(scroll);
    }

    // ═══════════════════════════════════════════════════════════
    // ENROLLED STUDENTS
    // ═══════════════════════════════════════════════════════════

    private static void showEnrolledStudents() {

        VBox content = contentBox();

        Label title = new Label("Enrolled Students");
        title.getStyleClass().add("dashboard-title");

        Label sub = new Label(
                "Select one of your courses to see its enrolled students"
        );

        sub.getStyleClass().add("subtitle-label");

        List<Course> courses = faculty.getAssignedCourses();

        if (courses.isEmpty()) {

            Label empty = new Label(
                    "No courses assigned to you yet."
            );

            empty.getStyleClass().add("label-muted");

            content.getChildren().addAll(
                    new VBox(4, title, sub),
                    empty
            );

            root.setCenter(content);

            return;
        }

        ComboBox<Course> courseBox = new ComboBox<>();

        courseBox.setItems(
                FXCollections.observableArrayList(courses)
        );

        courseBox.setPromptText("Select a course...");
        courseBox.setMaxWidth(400);

        courseBox.setCellFactory(lv -> courseCell());
        courseBox.setButtonCell(courseCell());

        TableView<Student> stuTable = styledTable();

        stuTable.setPlaceholder(
                styledPlaceholder(
                        "Select a course above to view students."
                )
        );

        VBox.setVgrow(stuTable, Priority.ALWAYS);

        stuTable.getColumns().addAll(

                strCol(
                        "Student ID",
                        100,
                        d -> new SimpleStringProperty(
                                d.getValue().getUserId()
                        )
                ),

                strCol(
                        "Name",
                        0,
                        d -> new SimpleStringProperty(
                                d.getValue().getName()
                        )
                ),

                strCol(
                        "Email",
                        0,
                        d -> new SimpleStringProperty(
                                d.getValue().getEmail()
                        )
                ),

                strCol(
                        "Credits",
                        100,
                        d -> new SimpleStringProperty(
                                d.getValue().getTotalCredits()
                                + " / "
                                + d.getValue().getMaxCredits()
                        )
                )
        );

        TableView<Student> waitTable = styledTable();

        waitTable.setPlaceholder(
                styledPlaceholder("No one on waitlist.")
        );

        waitTable.setMaxHeight(150);

        waitTable.getColumns().addAll(

                strCol(
                        "Position",
                        80,
                        d -> new SimpleStringProperty(
                                String.valueOf(
                                        waitTable.getItems()
                                                .indexOf(d.getValue()) + 1
                                )
                        )
                ),

                strCol(
                        "Student ID",
                        100,
                        d -> new SimpleStringProperty(
                                d.getValue().getUserId()
                        )
                ),

                strCol(
                        "Name",
                        0,
                        d -> new SimpleStringProperty(
                                d.getValue().getName()
                        )
                )
        );

        Label stuCountLabel = new Label();
        stuCountLabel.getStyleClass().add("subtitle-label");

        Label waitHdr = new Label("Waitlist");
        waitHdr.getStyleClass().add("card-title");

        VBox.setMargin(
                waitHdr,
                new Insets(10, 0, 0, 0)
        );

        courseBox.setOnAction(e -> {

            Course selected = courseBox.getValue();

            if (selected == null) {
                return;
            }

            stuTable.setItems(
                    FXCollections.observableArrayList(
                            selected.getEnrolledStudents()
                    )
            );

            waitTable.setItems(
                    FXCollections.observableArrayList(
                            selected.getWaitlist()
                    )
            );

            stuCountLabel.setText(
                    selected.getEnrolledStudents().size()
                    + " enrolled  ·  "
                    + selected.getAvailableSeats()
                    + " seats remaining"
            );
        });

        content.getChildren().addAll(
                new VBox(4, title, sub),
                courseBox,
                stuCountLabel,
                stuTable,
                waitHdr,
                waitTable
        );

        root.setCenter(content);
    }

    // ═══════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════

    private static HBox buildCourseCard(Course course) {

        Region bar = new Region();

        bar.setStyle(
                "-fx-background-color: #f5a623;" +
                "-fx-background-radius: 4;"
        );

        bar.setPrefWidth(4);
        bar.setMinHeight(70);

        Label nameLbl = new Label(
                course.getCourseName()
        );

        nameLbl.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );

        Label detailLbl = new Label(
                course.getCourseId()
                + "  ·  "
                + course.getCredits()
                + " credits"
                + "  ·  "
                + course.getEnrolledStudents().size()
                + " / "
                + course.getMaxSeats()
                + " enrolled"
        );

        detailLbl.getStyleClass().add("label-muted");

        VBox info = new VBox(4, nameLbl, detailLbl);

        HBox.setHgrow(info, Priority.ALWAYS);

        Label chip = new Label(
                course.getAvailableSeats()
                + " seats left"
        );

        chip.getStyleClass().add(
                course.getAvailableSeats() == 0
                        ? "chip-dropped"
                        : "chip-confirmed"
        );

        HBox card = new HBox(16, bar, info, chip);

        card.getStyleClass().add("info-card");
        card.setAlignment(Pos.CENTER_LEFT);

        return card;
    }

    private static ListCell<Course> courseCell() {

        return new ListCell<Course>() {

            @Override
            protected void updateItem(
                    Course c,
                    boolean empty
            ) {

                super.updateItem(c, empty);

                if (empty || c == null) {
                    setText(null);
                    return;
                }

                setText(
                        "[" + c.getCourseId() + "] "
                        + c.getCourseName()
                );
            }
        };
    }

    private static VBox statCard(
            String value,
            String label,
            String color
    ) {

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
        card.setAlignment(Pos.CENTER_LEFT);

        return card;
    }

    private static <S> TableView<S> styledTable() {

        TableView<S> t = new TableView<S>();

        t.getStyleClass().add("table-view");

        t.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        return t;
    }

    private static <S> TableColumn<S, String> strCol(
            String title,
            double fixedW,
            javafx.util.Callback<
                    TableColumn.CellDataFeatures<S, String>,
                    javafx.beans.value.ObservableValue<String>> mapper) {

        TableColumn<S, String> col =
                new TableColumn<S, String>(title);

        col.setCellValueFactory(cellData ->
                mapper.call(cellData)
        );

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

        box.setPadding(
                new Insets(32, 36, 32, 36)
        );

        return box;
    }

    private static Label styledPlaceholder(String text) {

        Label lbl = new Label(text);

        lbl.getStyleClass().add("label-muted");

        return lbl;
    }
}