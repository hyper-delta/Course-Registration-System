package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import model.*;

public class LoginController {

    public static Scene getScene() {

        // ── Background ───────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.getStyleClass().add("root");

        // ── Card ─────────────────────────────────────────────────────────────
        VBox card = new VBox(18);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(430);
        card.setAlignment(Pos.TOP_CENTER);

        // ── Branding ─────────────────────────────────────────────────────────
        Label logo     = new Label("🎓 CRS");
        logo.getStyleClass().add("title-label");

        Label subtitle = new Label("Course Registration System");
        subtitle.getStyleClass().add("subtitle-label");

        Label mca      = new Label("MCA Java Project");
        mca.getStyleClass().add("label-muted");

        VBox brand = new VBox(4, logo, subtitle, mca);
        brand.setAlignment(Pos.CENTER);
        VBox.setMargin(brand, new Insets(0, 0, 8, 0));

        // ── Role selector ─────────────────────────────────────────────────────
        Label roleLabel = new Label("LOGIN AS");
        roleLabel.getStyleClass().add("section-label");

        ToggleGroup roleGroup  = new ToggleGroup();
        ToggleButton studentBtn = new ToggleButton("  Student");
        ToggleButton facultyBtn = new ToggleButton("  Faculty");
        ToggleButton adminBtn   = new ToggleButton("  Admin");

        for (ToggleButton btn : new ToggleButton[]{studentBtn, facultyBtn, adminBtn}) {
            btn.setToggleGroup(roleGroup);
            btn.getStyleClass().add("role-btn");
            btn.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(btn, Priority.ALWAYS);
        }
        studentBtn.getStyleClass().add("role-btn-left");
        facultyBtn.getStyleClass().add("role-btn-mid");
        adminBtn.getStyleClass().add("role-btn-right");
        studentBtn.setSelected(true);

        HBox roleBox = new HBox(studentBtn, facultyBtn, adminBtn);

        // ── Input fields ──────────────────────────────────────────────────────
        TextField     idField  = new TextField();
        PasswordField pwdField = new PasswordField();

        for (Control f : new Control[]{idField, pwdField}) {
            f.getStyleClass().add("input-field");
            f.setMaxWidth(Double.MAX_VALUE);
        }
        idField.setPromptText("User ID  (e.g. S001, F001, A001)");
        pwdField.setPromptText("Password");

        // ── Feedback ──────────────────────────────────────────────────────────
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("label-error");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);

        // ── Login button ──────────────────────────────────────────────────────
        Button loginBtn = new Button("LOGIN");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(loginBtn, new Insets(6, 0, 0, 0));

        // ── Hint ──────────────────────────────────────────────────────────────
        Label hint = new Label(
            "Demo credentials — Admin: A001 / admin123\n" +
            "Students: S001–S005 / pass123 · Faculty: F001, F002 / pass123"
        );
        hint.getStyleClass().add("label-muted");
        hint.setTextAlignment(TextAlignment.CENTER);
        hint.setWrapText(true);
        hint.setStyle("-fx-font-size: 11px;");
        VBox.setMargin(hint, new Insets(10, 0, 0, 0));

        // ── Login logic ───────────────────────────────────────────────────────
        Runnable doLogin = () -> {
            String id  = idField.getText().trim();
            String pwd = pwdField.getText().trim();

            errorLabel.setVisible(false);

            if (id.isEmpty() || pwd.isEmpty()) {
                showError(errorLabel, "Please enter both ID and password.");
                return;
            }

            Toggle selected = roleGroup.getSelectedToggle();

            if (selected == studentBtn) {
                Student s = MainApp.store.findStudentById(id).orElse(null);
                if (s == null || !s.authenticate(pwd)) {
                    showError(errorLabel, "Invalid Student ID or password."); return;
                }
                MainApp.switchScene(StudentController.getScene(s));

            } else if (selected == facultyBtn) {
                Faculty f = MainApp.store.findFacultyById(id).orElse(null);
                if (f == null || !f.authenticate(pwd)) {
                    showError(errorLabel, "Invalid Faculty ID or password."); return;
                }
                MainApp.switchScene(FacultyController.getScene(f));

            } else if (selected == adminBtn) {
                Admin a = MainApp.store.findAdminById(id).orElse(null);
                if (a == null || !a.authenticate(pwd)) {
                    showError(errorLabel, "Invalid Admin ID or password."); return;
                }
                MainApp.switchScene(AdminController.getScene(a));
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        pwdField.setOnAction(e -> doLogin.run()); // Enter key on password field

        // Clear error when user starts typing
        idField.textProperty().addListener((o, old, n)  -> errorLabel.setVisible(false));
        pwdField.textProperty().addListener((o, old, n) -> errorLabel.setVisible(false));

        // ── Assemble card ─────────────────────────────────────────────────────
        card.getChildren().addAll(
            brand,
            new Separator(),
            roleLabel, roleBox,
            idField, pwdField,
            loginBtn,
            errorLabel,
            hint
        );
        Button concurrencyBtn = new Button("⚡  View Concurrency Test");
        concurrencyBtn.getStyleClass().add("btn-secondary");
        concurrencyBtn.setOnAction(e ->
                MainApp.switchScene(ConcurrencyController.getScene()));

        Button aboutBtn = new Button("ℹ️  About This Project");
        aboutBtn.getStyleClass().add("btn-secondary");
        aboutBtn.setOnAction(e ->
                 MainApp.switchScene(AboutController.getScene()));

        HBox bottomBtns = new HBox(12, concurrencyBtn, aboutBtn);
        bottomBtns.setAlignment(Pos.CENTER);

        VBox pageWrapper = new VBox(16, card, bottomBtns);
        pageWrapper.setAlignment(Pos.CENTER);

        root.getChildren().add(pageWrapper);

        
        StackPane.setAlignment(card, Pos.CENTER);

        // ── Scene ─────────────────────────────────────────────────────────────
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(MainApp.getCss());
        return scene;
    }

    private static void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
    }
}