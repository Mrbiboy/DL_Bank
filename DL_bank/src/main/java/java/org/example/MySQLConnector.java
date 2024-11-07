package java.org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {

    static private String url = "jdbc:mysql://localhost:3306/Dl_bank";
    static private String user = "root";
    static private String password = "PHW#84#jeor";

    public  Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
