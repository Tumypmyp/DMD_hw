package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionConfiguration {

    public static Connection getConnection(String database, String user, String password) {
        String url = "jdbc:postgresql://localhost:5432/" + database;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, props);
            System.out.println("Connected to database " + database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static void closeConnection(Connection connection) {
        if (connection == null)
            return;
        try {
            connection.close();
            System.out.println("Connection to database is closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
