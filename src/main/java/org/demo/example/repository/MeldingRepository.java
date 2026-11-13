package org.demo.example.repository;

import org.demo.example.database.DatabaseConnection;
import org.demo.example.model.MeldingOverzicht;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeldingRepository {


    public List<MeldingOverzicht> findAllOverzicht()
            throws SQLException {

        List<MeldingOverzicht> meldingen = new ArrayList<>();

        String sql = """
            SELECT
                m.meldingId,
                COALESCE(
                    f.naam, 'Onbekende fietsenstalling'
                ) AS fietsenstalling,
                m.probleemtype,
                m.status,
                COALESCE(
                    w.naam, 'Niet toegewezen'
                ) AS medewerker,
                m.datum
            FROM melding m
            LEFT JOIN fietsenstalling f
                ON m.fietsenstallingId =
                   f.fietsenstallingId
            LEFT JOIN medewerker w
                ON m.medewerker_medewerkerId =
                   w.medewerkerId
            ORDER BY m.datum DESC, m.meldingId DESC
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                MeldingOverzicht melding =
                        new MeldingOverzicht(
                                rs.getInt("meldingId"),
                                rs.getString("fietsenstalling"),
                                rs.getString("probleemtype"),
                                rs.getString("status"),
                                rs.getString("medewerker"),
                                rs.getTimestamp("datum")
                                        .toLocalDateTime()
                        );

                meldingen.add(melding);
            }
        }

        return meldingen;
    }

}
