
package org.demo.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/fietsenstallingen_beheer";

    public static Connection getConnection()
            throws SQLException {

        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (user == null || password == null) {
            throw new SQLException(
                    "Stel DB_USER en DB_PASSWORD in."
            );
        }

        return DriverManager.getConnection(
                URL, user, password
        );
    }
}
