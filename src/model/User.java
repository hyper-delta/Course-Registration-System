package model;

public abstract class User {
    protected String userId;
    protected String name;
    protected String email;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name   = name;
        this.email  = email;
    }

    public abstract void login();

    public String getUserId() { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + userId + "] " + name;
    }
}