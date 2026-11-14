
package org.demo.example.repository;

import org.demo.example.database.DatabaseConnection;
import org.demo.example.model.Fietsenstalling;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FietsenstallingRepository {

    public List<Fietsenstalling> findAll()
            throws SQLException {

        List<Fietsenstalling> fietsenstallingen =
                new ArrayList<>();

        String sql = """
        SELECT
            objectid AS fietsenstallingId,
            naam_fietsenstalling AS naam,
            adres_omschrijving AS adres
        FROM fietsenstalling_dataset
        ORDER BY naam_fietsenstalling
        """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Fietsenstalling fietsenstalling =
                        new Fietsenstalling(
                                rs.getInt("fietsenstallingId"),
                                rs.getString("naam"),
                                rs.getString("adres")
                        );

                fietsenstallingen.add(fietsenstalling);
            }
        }

        return fietsenstallingen;
    }
}
