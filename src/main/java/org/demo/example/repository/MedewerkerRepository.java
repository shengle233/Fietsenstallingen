
package org.demo.example.repository;

import org.demo.example.database.DatabaseConnection;
import org.demo.example.model.Medewerker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedewerkerRepository {

    public List<Medewerker> findAll()
            throws SQLException {

        List<Medewerker> medewerkers =
                new ArrayList<>();

        String sql = """
                SELECT medewerkerId, naam, email
                FROM medewerker
                ORDER BY medewerkerId
                """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("medewerkerId");
                String naam = rs.getString("naam");
                String email = rs.getString("email");

                Medewerker medewerker =
                        new Medewerker(id, naam, email);

                medewerkers.add(medewerker);
            }
        }

        return medewerkers;
    }

    public boolean addMedewerker(String naam, String email)
            throws SQLException {

        String sql = """
            INSERT INTO medewerker (naam, email)
            VALUES (?, ?)
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, naam);
            stmt.setString(2, email);

            return stmt.executeUpdate() == 1;
        }
    }

    public boolean updateMedewerker(
            int medewerkerId,
            String naam,
            String email
    ) throws SQLException {

        String sql = """
            UPDATE medewerker
            SET naam = ?, email = ?
            WHERE medewerkerId = ?
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, naam);
            stmt.setString(2, email);
            stmt.setInt(3, medewerkerId);

            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deleteMedewerker(int medewerkerId)
            throws SQLException {

        String sql = """
            DELETE FROM medewerker
            WHERE medewerkerId = ?
              AND NOT EXISTS (
                  SELECT 1
                  FROM melding
                  WHERE medewerker_medewerkerId = ?
              )
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, medewerkerId);
            stmt.setInt(2, medewerkerId);

            return stmt.executeUpdate() == 1;
        }
    }


}
