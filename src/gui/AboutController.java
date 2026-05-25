package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AboutController {

    public static Scene getScene() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        VBox content = new VBox(22);
        content.getStyleClass().add("content-area");
        content.setPadding(new Insets(40, 60, 40, 60));

        // ═══════════════════════════════════════════════════════
        // HEADER
        // ═══════════════════════════════════════════════════════

        Label title = new Label("ℹ Project Information");
        title.getStyleClass().add("dashboard-title");

        Label subtitle = new Label(
                "Course Registration System  ·  MCA Final Project");
        subtitle.getStyleClass().add("subtitle-label");

        VBox headerBox = new VBox(4, title, subtitle);

        // ═══════════════════════════════════════════════════════
        // PROJECT INFO CARD
        // ═══════════════════════════════════════════════════════

        VBox projectCard = new VBox(14);
        projectCard.getStyleClass().add("info-card");

        Label projectHdr = new Label("Project Overview");
        projectHdr.getStyleClass().add("card-title");

        Label p1 = info("Project Title",
                "Course Registration System (CRS)");

        Label p2 = info("Project Type",
                "University ERP / Academic Management System");

        Label p3 = info("Objective",
                "To simulate a real-world university course "
              + "registration workflow using Java, JavaFX, "
              + "OOP concepts, and concurrency.");

        projectCard.getChildren().addAll(
                projectHdr, p1, p2, p3
        );

        // ═══════════════════════════════════════════════════════
        // STUDENT DETAILS CARD
        // ═══════════════════════════════════════════════════════

        VBox studentCard = new VBox(14);
        studentCard.getStyleClass().add("info-card");

        Label studentHdr = new Label("Student Details");
        studentHdr.getStyleClass().add("card-title");

        Label s1 = info("Name", "YOUR NAME");
        Label s2 = info("Roll Number", "YOUR ROLL NUMBER");
        Label s3 = info("Course", "Master of Computer Applications (MCA)");
        Label s4 = info("Department", "Computer Applications");

        studentCard.getChildren().addAll(
                studentHdr, s1, s2, s3, s4
        );

        // ═══════════════════════════════════════════════════════
        // TECH STACK CARD
        // ═══════════════════════════════════════════════════════

        VBox techCard = new VBox(14);
        techCard.getStyleClass().add("info-card");

        Label techHdr = new Label("Technologies Used");
        techHdr.getStyleClass().add("card-title");

        FlowPane techPane = new FlowPane();
        techPane.setHgap(10);
        techPane.setVgap(10);

        techPane.getChildren().addAll(
                chip("Java"),
                chip("JavaFX"),
                chip("OOP"),
                chip("Multithreading"),
                chip("ExecutorService"),
                chip("Synchronization"),
                chip("Collections Framework"),
                chip("MVC Architecture")
        );

        techCard.getChildren().addAll(techHdr, techPane);

        // ═══════════════════════════════════════════════════════
        // OOD CONCEPTS CARD
        // ═══════════════════════════════════════════════════════

        VBox oopCard = new VBox(14);
        oopCard.getStyleClass().add("info-card");

        Label oopHdr = new Label("Object-Oriented Concepts");
        oopHdr.getStyleClass().add("card-title");

        VBox concepts = new VBox(10,
                concept("Inheritance",
                        "User → Student / Faculty / Admin"),

                concept("Encapsulation",
                        "Private fields with controlled access"),

                concept("Abstraction",
                        "Registrable interface"),

                concept("Polymorphism",
                        "Dynamic registration behavior"),

                concept("Composition",
                        "Student contains Timetable"),

                concept("Concurrency",
                        "Thread-safe seat allocation using synchronized")
        );

        oopCard.getChildren().addAll(oopHdr, concepts);

        // ═══════════════════════════════════════════════════════
        // FEATURES CARD
        // ═══════════════════════════════════════════════════════

        VBox featureCard = new VBox(14);
        featureCard.getStyleClass().add("info-card");

        Label featureHdr = new Label("Major Features");
        featureHdr.getStyleClass().add("card-title");

        VBox features = new VBox(8,
                feature("✔ Student, Faculty & Admin dashboards"),
                feature("✔ Course registration and drop"),
                feature("✔ Prerequisite validation"),
                feature("✔ Waitlist management"),
                feature("✔ Faculty assignment"),
                feature("✔ Concurrent registration simulation"),
                feature("✔ Real-time activity logging")
        );

        featureCard.getChildren().addAll(featureHdr, features);

        // ═══════════════════════════════════════════════════════
        // BACK BUTTON
        // ═══════════════════════════════════════════════════════

        Button backBtn = new Button("← Back to Login");
        backBtn.getStyleClass().add("btn-secondary");

        backBtn.setOnAction(e ->
                MainApp.switchScene(LoginController.getScene()));

        // ═══════════════════════════════════════════════════════
        // LAYOUT
        // ═══════════════════════════════════════════════════════

        HBox topRow = new HBox(18, projectCard, studentCard);
        HBox.setHgrow(projectCard, Priority.ALWAYS);
        HBox.setHgrow(studentCard, Priority.ALWAYS);

        HBox bottomRow = new HBox(18, techCard, oopCard);
        HBox.setHgrow(techCard, Priority.ALWAYS);
        HBox.setHgrow(oopCard, Priority.ALWAYS);

        content.getChildren().addAll(
                headerBox,
                topRow,
                bottomRow,
                featureCard,
                backBtn
        );

        root.setCenter(content);

        Scene scene = new Scene(root, 1180, 820);
        scene.getStylesheets().add(MainApp.getCss());

        return scene;
    }

    // ═══════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════

    private static Label info(String key, String value) {

        Label lbl = new Label(key + " :  " + value);

        lbl.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 13px;"
        );

        lbl.setWrapText(true);

        return lbl;
    }

    private static HBox concept(String title, String desc) {

        Label t = new Label(title + "  →  ");
        t.setStyle(
                "-fx-text-fill: #e94560;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        Label d = new Label(desc);
        d.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 13px;"
        );

        HBox row = new HBox(6, t, d);
        row.setAlignment(Pos.CENTER_LEFT);

        return row;
    }

    private static Label feature(String text) {

        Label lbl = new Label(text);

        lbl.setStyle(
                "-fx-text-fill: #4ecca3;" +
                "-fx-font-size: 13px;"
        );

        return lbl;
    }

    private static Label chip(String text) {

        Label chip = new Label(text);

        chip.setStyle(
                "-fx-background-color: rgba(233,69,96,0.15);" +
                "-fx-text-fill: #e94560;" +
                "-fx-padding: 8 14 8 14;" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;"
        );

        return chip;
    }
}