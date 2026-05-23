package model;

public abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String password;

    public User(String userId, String name, String email, String password) {
        this.userId    = userId;
        this.name      = name;
        this.email     = email;
        this.password  = password;
    }

    public abstract void login();

    public boolean authenticate(String input) {
        return this.password.equals(input);
    }

    public String getUserId() { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + userId + "] " + name;
    }
}