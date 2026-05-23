package model;

public class Admin extends User {
    private final String adminLevel;

    public Admin(String userId, String name, String email,
                 String password, String adminLevel) {
        super(userId, name, email, password);
        this.adminLevel = adminLevel;
    }

    @Override
    public void login() {
        System.out.printf("%nAdmin %s [Level: %s] logged in.%n", name, adminLevel);
    }

    public String getAdminLevel() { return adminLevel; }
}