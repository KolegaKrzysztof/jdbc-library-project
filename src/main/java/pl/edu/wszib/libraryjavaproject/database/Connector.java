package pl.edu.wszib.libraryjavaproject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static final Connector instance = new Connector();

    private final Connection connection;
    private Connector(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/libraryjavaproject",
                    "root",
                    "");
        } catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
    }

//    public void close(){
//        try {
//            connection.close();
//        } catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//    }

    public Connection getConnection() {
        return connection;
    }

    public static Connector getInstance() {
        return instance;
    }
}
