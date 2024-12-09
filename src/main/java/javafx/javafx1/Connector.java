package javafx.javafx1;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector
{
    public static Connection getConnection()
    {
        Connection connect = null;
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/duongduaeducation",
                    "root",
                    "");

            System.out.println("Database connected.");
            return connect;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
