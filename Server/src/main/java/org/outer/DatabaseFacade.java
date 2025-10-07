package org.outer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFacade {

    public static final String url = "jdbc:postgresql://localhost:5433/studs";
    public static final String user = "s465527";
    public static final String password = "gYobdNKJPaDxgOiE";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery("SELECT version();");
            if (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка подключения к бд");
        }
    }
}
