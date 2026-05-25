package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AboutController {

    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        VBox content = new VBox(28);
        content.getStyleClass().add("content-area");
        content.setPadding(new Insets(44, 60, 44, 60));
        content.setAlignment(Pos.TOP_CENTER);

        // ── Hero header ───────────────────────────────────────
        Label logo = new Label("🎓");
        logo.setStyle("-fx-font-size: 52px;");

        Label title = new Label("Course Registration System");
        title.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("MCA Java Project  ·  JavaFX GUI  ·  Java 21");
        subtitle.getStyleClass().add("subtitle-label");

        VBox hero = new VBox(8, logo, title, subtitle);
        hero.setAlignment(Pos.CENTER);

        // ── Three info cards in a row ─────────────────────────
        VBox projectCard = infoCard("Project Overview",
            new String[][]{
                {"Name",     "Course Registration System (CRS)"},
                {"Purpose",  "Simulate a university ERP module"},
                {"Data",     "In-memory  ·  Collections & Queue"},
                {"GUI",      "JavaFX 21  ·  Dark theme  ·  CSS"},
                {"Version",  "Java 21 LTS"}
            }
        );

        VBox techCard = infoCard("Tech Stack",
            new String[][]{
                {"Language",    "Java 21"},
                {"UI",          "JavaFX 21  ·  Scene Builder 26"},
                {"Concurrency", "ExecutorService  ·  synchronized"},
                {"Collections", "ArrayList  ·  LinkedList Queue"},
                {"Pattern",     "MVC  ·  Repository  ·  Interface"}
            }
        );

        VBox oodCard = infoCard("OOD Concepts Applied",
            new String[][]{
                {"Inheritance",   "User → Student, Faculty, Admin"},
                {"Abstraction",   "Registrable interface"},
                {"Encapsulation", "Private fields + getters/setters"},
                {"Polymorphism",  "RegistrationService implements Registrable"},
                {"Composition",   "Student owns Timetable"}
            }
        );

        HBox cardsRow = new HBox(20, projectCard, techCard, oodCard);
        cardsRow.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(projectCard, Priority.ALWAYS);
        HBox.setHgrow(techCard,    Priority.ALWAYS);
        HBox.setHgrow(oodCard,     Priority.ALWAYS);

        // ── Feature chips (FlowPane so they wrap) ─────────────
        FlowPane featuresRow = new FlowPane(12, 10,
            featureChip("Password Auth"),
            featureChip("Thread-Safe Registration"),
            featureChip("Waitlist Queue"),
            featureChip("Prerequisite Validation"),
            featureChip("Circular Dep. Detection"),
            featureChip("Live Concurrency Demo")
        );
        featuresRow.setAlignment(Pos.CENTER);

        // ── System modules section ────────────────────────────
        Label modulesHdr = new Label("System Modules");
        modulesHdr.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;"
        );

        HBox modulesRow = new HBox(16,
            moduleCard("Student",  "Register  ·  Drop  ·  Waitlist  ·  Timetable"),
            moduleCard("Faculty",  "View Enrolled  ·  Course Overview"),
            moduleCard("Admin",    "Manage Courses  ·  Prerequisites  ·  Users"),
            moduleCard("System",   "Validation  ·  Concurrency  ·  Exceptions")
        );
        modulesRow.setAlignment(Pos.CENTER);
        for (javafx.scene.Node n : modulesRow.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }

        VBox modulesSection = new VBox(14, modulesHdr, modulesRow);
        modulesSection.getStyleClass().add("info-card");

        // ── Back button ───────────────────────────────────────
        Button backBtn = new Button("← Back to Login");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setOnAction(e -> MainApp.switchScene(LoginController.getScene()));

        content.getChildren().addAll(
            hero,
            cardsRow,
            featuresRow,
            modulesSection,
            backBtn
        );

        root.setCenter(content);

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(MainApp.getCss());
        return scene;
    }

    // ═══════════════════════════════════════════════════════════
    //  BUILDERS
    // ═══════════════════════════════════════════════════════════

    private static VBox infoCard(String heading, String[][] rows) {
        Label head = new Label(heading);
        head.setStyle(
            "-fx-text-fill: #e94560;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;"
        );

        VBox card = new VBox(10);
        card.getStyleClass().add("info-card");
        card.getChildren().add(head);
        card.getChildren().add(new Separator());

        for (String[] row : rows) {
            HBox line = new HBox(8);
            line.setAlignment(Pos.TOP_LEFT);

            Label keyLbl = new Label(row[0]);
            keyLbl.setStyle(
                "-fx-text-fill: #a8a8b3;" +
                "-fx-font-size: 12px;" +
                "-fx-min-width: 90px;" +
                "-fx-max-width: 90px;"
            );

            Label valLbl = new Label(row[1]);
            valLbl.setStyle(
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 12px;"
            );
            valLbl.setWrapText(true);
            HBox.setHgrow(valLbl, Priority.ALWAYS);

            line.getChildren().addAll(keyLbl, valLbl);
            card.getChildren().add(line);
        }

        return card;
    }

    private static Label featureChip(String text) {
        Label chip = new Label(text);
        chip.setStyle(
            "-fx-background-color: #0f3460;" +
            "-fx-text-fill: #4ecca3;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;"
        );
        return chip;
    }

    private static VBox moduleCard(String title, String details) {
        Label titleLbl = new Label(title);
        titleLbl.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;"
        );

        Label detailLbl = new Label(details);
        detailLbl.setStyle(
            "-fx-text-fill: #a8a8b3;" +
            "-fx-font-size: 11px;"
        );
        detailLbl.setWrapText(true);

        VBox card = new VBox(6, titleLbl, detailLbl);
        card.getStyleClass().add("info-card");
        card.setAlignment(Pos.TOP_LEFT);
        return card;
    }
}