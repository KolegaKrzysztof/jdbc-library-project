package pl.edu.wszib.libraryjavaproject.database;

import pl.edu.wszib.libraryjavaproject.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private static final UserDAO instance = new UserDAO();
    final Connector connector = Connector.getInstance();

    private UserDAO() {
    }

    public Optional<User> findByLogin(String login){
        try {
            String sql = "SELECT * FROM tuser WHERE login = ?";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        User.Role.valueOf(rs.getString("role"))));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tuser";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        User.Role.valueOf(rs.getString("role"))));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return users;
    }

    public void userAdd(String login, String password, String name, String surname){
        try {
            String sql = "INSERT INTO tuser (login, password, name, surname, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, surname);
            ps.setString(5, User.Role.USER.toString());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    public static UserDAO getInstance() {
        return instance;
    }
}
