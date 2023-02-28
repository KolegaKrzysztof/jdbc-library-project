package pl.edu.wszib.libraryjavaproject.model;

public class User {
    private int id;
    private String login;
    private String password;
    private String name;
    private String surname;
    Role role;

    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(int id, String login, String password, String name, String surname, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[id: '")
                .append(this.id)
                .append("', login: '")
                .append(this.login)
                .append("', name: '")
                .append(this.name)
                .append("', surname: '")
                .append(surname)
                .append("', role: '")
                .append(this.role)
                .append("']");
        return sb.toString();
    }

    public enum Role {
        USER,
        ADMIN
    }
}
