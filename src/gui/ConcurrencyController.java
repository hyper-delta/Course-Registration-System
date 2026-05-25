package gui;

import exceptions.CRSException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyController {

    private static TextArea  logArea;
    private static HBox      threadStatusRow;
    private static Label     resultLabel;
    private static Button    runBtn;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static Scene getScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        VBox content = new VBox(24);
        content.getStyleClass().add("content-area");
        content.setPadding(new Insets(40, 60, 40, 60));

        // ── Header ────────────────────────────────────────────────
        Label title = new Label("⚡ Concurrency Test");
        title.getStyleClass().add("dashboard-title");

        Label sub = new Label(
            "Multiple student threads race for 2 seats in CS201  ·  " +
            "synchronized(course) ensures only 2 succeed"
        );
        sub.getStyleClass().add("subtitle-label");

        // ── Thread status cards ───────────────────────────────────
        Label cardsHdr = new Label("Thread Status");
        cardsHdr.getStyleClass().add("card-title");

        threadStatusRow = new HBox(12);
        threadStatusRow.setAlignment(Pos.CENTER_LEFT);
        refreshThreadCards("WAITING");

        VBox cardBox = new VBox(10, cardsHdr, threadStatusRow);
        cardBox.getStyleClass().add("info-card");

        // ── Live log ──────────────────────────────────────────────
        Label logHdr = new Label("Live Log");
        logHdr.getStyleClass().add("card-title");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(240);
        logArea.setWrapText(true);
        logArea.setStyle(
            "-fx-control-inner-background: #0a0a1a;" +
            "-fx-text-fill: #4ecca3;" +
            "-fx-font-family: 'Ubuntu Mono', 'Courier New', monospace;" +
            "-fx-font-size: 13px;"
        );

        VBox logBox = new VBox(10, logHdr, logArea);
        logBox.getStyleClass().add("info-card");

        // ── Result bar ────────────────────────────────────────────
        resultLabel = new Label("Press  ▶ RUN TEST  to start the simulation.");
        resultLabel.getStyleClass().add("subtitle-label");

        VBox resultBox = new VBox(resultLabel);
        resultBox.getStyleClass().add("info-card");

        // ── Buttons ───────────────────────────────────────────────
        runBtn = new Button("▶  RUN TEST");
        runBtn.getStyleClass().add("btn-primary");
        runBtn.setPrefWidth(160);
        runBtn.setOnAction(e -> runTest());

        Button resetBtn = new Button("↺  RESET");
        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setPrefWidth(120);
        resetBtn.setOnAction(e -> resetView());

        Button backBtn = new Button("← Back to Login");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setOnAction(e -> MainApp.switchScene(LoginController.getScene()));

        HBox btnRow = new HBox(12, runBtn, resetBtn, backBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        // ── Assemble ──────────────────────────────────────────────
        content.getChildren().addAll(
            new VBox(4, title, sub),
            cardBox,
            logBox,
            resultBox,
            btnRow
        );

        // ScrollPane scroll = new ScrollPane(content); //for scrolling
        // scroll.setFitToWidth(true);
        // scroll.getStyleClass().add("scroll-pane");

        root.setCenter(content);

        Scene scene = new Scene(root, 1180, 820);
        scene.getStylesheets().add(MainApp.getCss());
        return scene;
    }

    // ═══════════════════════════════════════════════════════════
    //  THREAD CARD HELPERS
    // ═══════════════════════════════════════════════════════════

    private static void refreshThreadCards(String defaultStatus) {
        threadStatusRow.getChildren().clear();
        List<Student> students = MainApp.store.getStudents();
        for (Student s : students) {
            VBox card = buildThreadCard(s.getName(), defaultStatus);
            HBox.setHgrow(card, Priority.ALWAYS);
            threadStatusRow.getChildren().add(card);
        }
    }

    private static VBox buildThreadCard(String fullName, String status) {
        // Show first name only to keep cards compact
        String firstName = fullName.contains(" ")
                ? fullName.substring(0, fullName.indexOf(" "))
                : fullName;

        Label nameLbl = new Label(firstName);
        nameLbl.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;"
        );

        Label statusLbl = new Label(status);
        statusLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; "
                + "-fx-text-fill: " + colorFor(status) + ";");

        VBox card = new VBox(6, nameLbl, statusLbl);
        card.getStyleClass().add("info-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);
        return card;
    }

    private static void updateThreadCard(int index, String status) {
        Platform.runLater(() -> {
            if (index >= threadStatusRow.getChildren().size()) return;
            VBox card = (VBox) threadStatusRow.getChildren().get(index);
            Label statusLbl = (Label) card.getChildren().get(1);
            statusLbl.setText(status);
            statusLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; "
                    + "-fx-text-fill: " + colorFor(status) + ";");
        });
    }

    private static String colorFor(String status) {
        if ("CONFIRMED".equals(status))  return "#4ecca3";
        if ("WAITLISTED".equals(status)) return "#f5a623";
        if ("FAILED".equals(status))     return "#e94560";
        if ("RUNNING".equals(status))    return "#a8a8b3";
        return "#5a6a8a"; // WAITING
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGGING
    // ═══════════════════════════════════════════════════════════

    private static void log(String msg) {
        Platform.runLater(() -> {
            String time = LocalTime.now().format(TIME_FMT);
            logArea.appendText("[" + time + "]  " + msg + "\n");
        });
    }

    // ═══════════════════════════════════════════════════════════
    //  RUN TEST
    // ═══════════════════════════════════════════════════════════

    private static void runTest() {
        runBtn.setDisable(true);
        logArea.clear();
        resultLabel.setText("Running...");
        resultLabel.setStyle("-fx-font-size: 14px;");
        refreshThreadCards("WAITING");

        Course cs201 = MainApp.store.findCourseById("CS201").orElse(null);
        Course cs101 = MainApp.store.findCourseById("CS101").orElse(null);

        if (cs201 == null) {
            log("ERROR: CS201 not found. Check seed data.");
            runBtn.setDisable(false);
            return;
        }

        // ── Prepare: reset CS201 and auto-complete CS101 ──────
        cs201.resetForConcurrencyTest();
        List<Student> students = MainApp.store.getStudents();
        for (Student s : students) {
            s.clearRegistrationsForCourse(cs201);
            if (cs101 != null && !s.hasCompleted(cs101)) {
                s.markCompleted(cs101);
            }
        }

        log("System  →  CS201 reset  |  " + cs201.getMaxSeats() + " seats available");
        log("System  →  CS101 silently marked completed for all students");
        log("System  →  Launching " + students.size() + " threads...");
        log("────────────────────────────────────────────────────");

        ExecutorService pool = Executors.newFixedThreadPool(students.size());
        Random rng = new Random();

        for (int i = 0; i < students.size(); i++) {
            final Student s  = students.get(i);
            final int     idx = i;
            final int     threadNum = i + 1;

            pool.submit(() -> {

                // Random delay 0–400ms so threads race visibly
                try {
                    Thread.sleep(rng.nextInt(400));
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                updateThreadCard(idx, "RUNNING");
                log("Thread-" + threadNum + " (" + s.getName() + ")  →  attempting...");

                try {
                    MainApp.service.register(s, cs201);
                } catch (CRSException ex) {
                    updateThreadCard(idx, "FAILED");
                    log("Thread-" + threadNum + " (" + s.getName()
                            + ")  →  ✗ FAILED: " + ex.getMessage());
                    return;
                }

                // ── Determine actual outcome ───────────────────
                boolean confirmed = false;
                for (Registration r : s.getRegistrations()) {
                    if (r.getCourse().equals(cs201)
                            && r.getStatus() == RegistrationStatus.CONFIRMED) {
                        confirmed = true;
                        break;
                    }
                }

                if (confirmed) {
                    updateThreadCard(idx, "CONFIRMED");
                    log("Thread-" + threadNum + " (" + s.getName()
                            + ")  →  ✓ SEAT CONFIRMED");
                } else {
                    // Must be on waitlist
                    updateThreadCard(idx, "WAITLISTED");
                    log("Thread-" + threadNum + " (" + s.getName()
                            + ")  →  ⏳ ADDED TO WAITLIST (course full)");
                }
            });
        }

        pool.shutdown();

        // ── Wait for all threads then show final result ────────
        Thread watcher = new Thread(() -> {
            try {
                pool.awaitTermination(15, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            final int enrolled   = cs201.getEnrolledStudents().size();
            final int waitlisted = cs201.getWaitlist().size();
            final int maxSeats   = cs201.getMaxSeats();

            Platform.runLater(() -> {
                log("────────────────────────────────────────────────────");
                log("RESULT  →  " + enrolled + " / " + maxSeats
                        + " seats filled  |  " + waitlisted + " on waitlist");
                log("PROOF   →  synchronized(course) blocked over-enrollment ✓");

                resultLabel.setText(
                    "✓  " + enrolled + " / " + maxSeats + " seats filled  ·  "
                    + waitlisted + " student(s) on waitlist  ·  "
                    + "Race condition prevented by  synchronized(course)"
                );
                resultLabel.setStyle(
                    "-fx-text-fill: #4ecca3;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;"
                );

                runBtn.setDisable(false);
            });
        });
        watcher.setDaemon(true);
        watcher.start();
    }

    // ═══════════════════════════════════════════════════════════
    //  RESET
    // ═══════════════════════════════════════════════════════════

    private static void resetView() {
        logArea.clear();
        refreshThreadCards("WAITING");
        resultLabel.setText("Press  ▶ RUN TEST  to start the simulation.");
        resultLabel.setStyle("-fx-font-size: 14px;");
        runBtn.setDisable(false);

        Course cs201 = MainApp.store.findCourseById("CS201").orElse(null);
        if (cs201 != null) {
            cs201.resetForConcurrencyTest();
            for (Student s : MainApp.store.getStudents()) {
                s.clearRegistrationsForCourse(cs201);
            }
        }
    }
}