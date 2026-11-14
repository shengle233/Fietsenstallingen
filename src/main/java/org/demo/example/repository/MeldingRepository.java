package org.demo.example.repository;

import org.demo.example.database.DatabaseConnection;
import org.demo.example.model.MeldingDetail;
import org.demo.example.model.MeldingOverzicht;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MeldingRepository {


    public List<MeldingOverzicht> findAllOverzicht()
            throws SQLException {

        List<MeldingOverzicht> meldingen = new ArrayList<>();


        String sql = """
        SELECT
            m.meldingId,
            COALESCE(
                f.naam_fietsenstalling,
                'Onbekende fietsenstalling'
            ) AS fietsenstalling,
            m.probleemtype,
            m.status,
            COALESCE(w.naam, 'Niet toegewezen') AS medewerker,
            m.datum

        FROM melding m

        LEFT JOIN fietsenstalling_dataset f
            ON m.fietsenstallingId = f.objectid

        LEFT JOIN medewerker w
            ON m.medewerker_medewerkerId = w.medewerkerId

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


    public int createMelding(
            int fietsenstallingId,
            Integer medewerkerId,
            String omschrijving,
            String probleemtype,
            List<String> taken
    ) throws SQLException {

        // Controleer de invoer
        if (fietsenstallingId <= 0) {
            throw new IllegalArgumentException(
                    "Selecteer een fietsenstalling."
            );
        }

        if (omschrijving == null || omschrijving.isBlank()) {
            throw new IllegalArgumentException(
                    "Vul een omschrijving in."
            );
        }

        if (probleemtype == null || probleemtype.isBlank()) {
            throw new IllegalArgumentException(
                    "Selecteer een probleemtype."
            );
        }

        if (taken == null || taken.isEmpty()
                || taken.stream().anyMatch(
                taak -> taak == null || taak.isBlank()
        )) {
            throw new IllegalArgumentException(
                    "Voeg minimaal één geldige taak toe."
            );
        }

        String meldingSql = """
            INSERT INTO melding
                (fietsenstallingId,
                 medewerker_medewerkerId,
                 datum,
                 omschrijving,
                 probleemtype,
                 status)
            VALUES (?, ?, NOW(), ?, ?, 'OPEN')
            """;

        String taakSql = """
        INSERT INTO taak
            (melding_meldingId, omschrijving, voltooid, volgorde)
        VALUES (?, ?, 0, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                int meldingId;

                // 1. Melding opslaan
                try (PreparedStatement stmt =
                             conn.prepareStatement(
                                     meldingSql,
                                     Statement.RETURN_GENERATED_KEYS
                             )) {

                    stmt.setInt(1, fietsenstallingId);

                    if (medewerkerId == null) {
                        stmt.setNull(2, Types.INTEGER);
                    } else {
                        stmt.setInt(2, medewerkerId);
                    }

                    stmt.setString(3, omschrijving.trim());
                    stmt.setString(4, probleemtype);

                    stmt.executeUpdate();

                    // 2. Nieuwe meldingId ophalen
                    try (ResultSet keys = stmt.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "Geen meldingId ontvangen."
                            );
                        }

                        meldingId = keys.getInt(1);
                    }
                }

                // 3. Taken opslaan
                try (PreparedStatement stmt =
                             conn.prepareStatement(taakSql)) {

                    int volgorde = 1;

                    for (String taak : taken) {

                        stmt.setInt(1, meldingId);
                        stmt.setString(2, taak.trim());
                        stmt.setInt(3, volgorde++);

                        stmt.executeUpdate();
                    }
                }

                // 4. Alles is gelukt
                conn.commit();

                return meldingId;

            } catch (SQLException | RuntimeException e) {

                // 5. Bij een fout alles terugdraaien
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }


    public MeldingDetail findDetailById(int meldingId)
            throws SQLException {

        String meldingSql = """
            SELECT
                m.meldingId,
                COALESCE(
                    f.naam_fietsenstalling,
                    'Onbekende fietsenstalling'
                ) AS fietsenstalling,
                m.probleemtype,
                m.status,
                COALESCE(
                    w.naam, 'Niet toegewezen'
                ) AS medewerker,
                m.datum,
                m.omschrijving,
                m.medewerker_medewerkerId AS medewerkerId
            FROM melding m
            LEFT JOIN fietsenstalling_dataset f
                ON m.fietsenstallingId = f.objectid
            LEFT JOIN medewerker w
                ON m.medewerker_medewerkerId = w.medewerkerId
            WHERE m.meldingId = ?
            """;

        String taakSql = """
        SELECT taakId, omschrijving, voltooid
        FROM taak
        WHERE melding_meldingId = ?
        ORDER BY volgorde ASC, taakId ASC
        """;

        try (Connection conn =
                     DatabaseConnection.getConnection()) {

            try (PreparedStatement stmt =
                         conn.prepareStatement(meldingSql)) {

                stmt.setInt(1, meldingId);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) {
                        return null;
                    }

                    int id = rs.getInt("meldingId");
                    String fietsenstalling =
                            rs.getString("fietsenstalling");
                    String probleemtype =
                            rs.getString("probleemtype");
                    String status =
                            rs.getString("status");
                    String medewerker =
                            rs.getString("medewerker");
                    LocalDateTime datum =
                            rs.getTimestamp("datum")
                                    .toLocalDateTime();
                    String omschrijving =
                            rs.getString("omschrijving");
                    Integer medewerkerId =
                            (Integer) rs.getObject("medewerkerId");

                    List<MeldingDetail.TaakDetail> taken =
                            new ArrayList<>();

                    try (PreparedStatement taakStmt =
                                 conn.prepareStatement(taakSql)) {

                        taakStmt.setInt(1, meldingId);

                        try (ResultSet taakRs =
                                     taakStmt.executeQuery()) {

                            while (taakRs.next()) {
                                taken.add(
                                        new MeldingDetail.TaakDetail(
                                                taakRs.getInt("taakId"),
                                                taakRs.getString("omschrijving"),
                                                taakRs.getBoolean("voltooid")
                                        )
                                );
                            }
                        }
                    }

                    return new MeldingDetail(
                            id,
                            fietsenstalling,
                            probleemtype,
                            status,
                            medewerkerId,
                            medewerker,
                            datum,
                            omschrijving,
                            taken
                    );

                }
            }
        }
    }

    public boolean updateMedewerker(
            int meldingId,
            int medewerkerId
    ) throws SQLException {

        String sql = """
            UPDATE melding
            SET medewerker_medewerkerId = ?
            WHERE meldingId = ?
              AND status <> 'AFGEHANDELD'
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, medewerkerId);
            stmt.setInt(2, meldingId);

            return stmt.executeUpdate() == 1;
        }
    }


    public boolean updateTaak(
            int meldingId,
            int taakId,
            boolean voltooid
    ) throws SQLException {

        String meldingSql = """
            SELECT status, medewerker_medewerkerId
            FROM melding
            WHERE meldingId = ?
            FOR UPDATE
            """;

        String taakSql = """
            UPDATE taak
            SET voltooid = ?
            WHERE taakId = ?
              AND melding_meldingId = ?
            """;

        String statusSql = """
            UPDATE melding
            SET status = 'IN_BEHANDELING'
            WHERE meldingId = ?
              AND status = 'OPEN'
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                String status;
                Integer medewerkerId;

                try (PreparedStatement stmt =
                             conn.prepareStatement(meldingSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        status = rs.getString("status");
                        medewerkerId = (Integer) rs.getObject(
                                "medewerker_medewerkerId"
                        );
                    }
                }

                if (medewerkerId == null
                        || (!status.equals("OPEN")
                        && !status.equals("IN_BEHANDELING"))) {

                    conn.rollback();
                    return false;
                }

                int updated;

                try (PreparedStatement stmt =
                             conn.prepareStatement(taakSql)) {

                    stmt.setBoolean(1, voltooid);
                    stmt.setInt(2, taakId);
                    stmt.setInt(3, meldingId);

                    updated = stmt.executeUpdate();
                }

                if (updated != 1) {
                    conn.rollback();
                    return false;
                }

                if (status.equals("OPEN") && voltooid) {

                    try (PreparedStatement stmt =
                                 conn.prepareStatement(statusSql)) {

                        stmt.setInt(1, meldingId);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }


    public boolean updateStatus(
            int meldingId,
            String nieuweStatus
    ) throws SQLException {

        String meldingSql = """
            SELECT status, medewerker_medewerkerId
            FROM melding
            WHERE meldingId = ?
            FOR UPDATE
            """;

        String takenSql = """
            SELECT
                COUNT(*) AS totaal,
                COALESCE(
                    SUM(CASE WHEN voltooid = 0 THEN 1 ELSE 0 END),
                    0
                ) AS openTaken
            FROM taak
            WHERE melding_meldingId = ?
            """;

        String updateSql = """
            UPDATE melding
            SET status = ?
            WHERE meldingId = ?
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                String huidigeStatus;
                Integer medewerkerId;

                try (PreparedStatement stmt =
                             conn.prepareStatement(meldingSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        huidigeStatus = rs.getString("status");
                        medewerkerId = (Integer) rs.getObject(
                                "medewerker_medewerkerId"
                        );
                    }
                }

                boolean toegestaan = switch (nieuweStatus) {

                    case "ONDERBROKEN" ->
                            huidigeStatus.equals("IN_BEHANDELING");

                    case "IN_BEHANDELING" ->
                            huidigeStatus.equals("ONDERBROKEN");

                    case "AFGEHANDELD" ->
                            huidigeStatus.equals("IN_BEHANDELING");

                    default -> false;
                };

                if (!toegestaan || medewerkerId == null) {
                    conn.rollback();
                    return false;
                }

                if (nieuweStatus.equals("AFGEHANDELD")) {

                    try (PreparedStatement stmt =
                                 conn.prepareStatement(takenSql)) {

                        stmt.setInt(1, meldingId);

                        try (ResultSet rs = stmt.executeQuery()) {

                            rs.next();

                            int totaal = rs.getInt("totaal");
                            int openTaken = rs.getInt("openTaken");

                            if (totaal == 0 || openTaken > 0) {
                                conn.rollback();
                                return false;
                            }
                        }
                    }
                }

                try (PreparedStatement stmt =
                             conn.prepareStatement(updateSql)) {

                    stmt.setString(1, nieuweStatus);
                    stmt.setInt(2, meldingId);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }


    public boolean addTaak(
            int meldingId,
            String omschrijving
    ) throws SQLException {

        if (omschrijving == null || omschrijving.isBlank()) {
            return false;
        }

        String meldingSql = """
            SELECT status
            FROM melding
            WHERE meldingId = ?
            FOR UPDATE
            """;

        String taakSql = """
        INSERT INTO taak
            (melding_meldingId, omschrijving, voltooid, volgorde)
        SELECT ?, ?, 0,
               COALESCE(MAX(volgorde), 0) + 1
        FROM taak
        WHERE melding_meldingId = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                String status;

                try (PreparedStatement stmt =
                             conn.prepareStatement(meldingSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        status = rs.getString("status");
                    }
                }

                if (!"OPEN".equals(status)
                        && !"IN_BEHANDELING".equals(status)) {

                    conn.rollback();
                    return false;
                }

                try (PreparedStatement stmt =
                             conn.prepareStatement(taakSql)) {

                    stmt.setInt(1, meldingId);
                    stmt.setString(2, omschrijving.trim());
                    stmt.setInt(3, meldingId);

                    if (stmt.executeUpdate() != 1) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }


    public boolean deleteMelding(int meldingId)
            throws SQLException {

        String checkSql = """
            SELECT status
            FROM melding
            WHERE meldingId = ?
            FOR UPDATE
            """;

        String deleteTakenSql = """
            DELETE FROM taak
            WHERE melding_meldingId = ?
            """;

        String deleteMeldingSql = """
            DELETE FROM melding
            WHERE meldingId = ?
              AND status = 'OPEN'
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                String status;

                try (PreparedStatement stmt =
                             conn.prepareStatement(checkSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {

                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        status = rs.getString("status");
                    }
                }

                if (!"OPEN".equals(status)) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement stmt =
                             conn.prepareStatement(deleteTakenSql)) {

                    stmt.setInt(1, meldingId);
                    stmt.executeUpdate();
                }

                int deleted;

                try (PreparedStatement stmt =
                             conn.prepareStatement(deleteMeldingSql)) {

                    stmt.setInt(1, meldingId);
                    deleted = stmt.executeUpdate();
                }

                if (deleted != 1) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }



    public Map<String, Integer> countByStatus()
            throws SQLException {

        Map<String, Integer> aantallen = new HashMap<>();

        aantallen.put("OPEN", 0);
        aantallen.put("IN_BEHANDELING", 0);
        aantallen.put("ONDERBROKEN", 0);
        aantallen.put("AFGEHANDELD", 0);

        String sql = """
            SELECT status, COUNT(*) AS aantal
            FROM melding
            GROUP BY status
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                aantallen.put(
                        rs.getString("status"),
                        rs.getInt("aantal")
                );
            }
        }

        return aantallen;
    }


    public List<MeldingOverzicht> findRecent(int limit)
            throws SQLException {

        List<MeldingOverzicht> meldingen = new ArrayList<>();

        String sql = """
            SELECT
                m.meldingId,
                COALESCE(
                    f.naam_fietsenstalling,
                    'Onbekende fietsenstalling'
                ) AS fietsenstalling,
                m.probleemtype,
                m.status,
                COALESCE(
                    w.naam, 'Niet toegewezen'
                ) AS medewerker,
                m.datum
            FROM melding m

            LEFT JOIN fietsenstalling_dataset f
                ON m.fietsenstallingId = f.objectid

            LEFT JOIN medewerker w
                ON m.medewerker_medewerkerId = w.medewerkerId

            ORDER BY m.datum DESC, m.meldingId DESC
            LIMIT ?
            """;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    meldingen.add(new MeldingOverzicht(
                            rs.getInt("meldingId"),
                            rs.getString("fietsenstalling"),
                            rs.getString("probleemtype"),
                            rs.getString("status"),
                            rs.getString("medewerker"),
                            rs.getTimestamp("datum").toLocalDateTime()
                    ));
                }
            }
        }

        return meldingen;
    }


    public boolean moveTaak(
            int meldingId,
            int taakId,
            int richting
    ) throws SQLException {

        if (richting != -1 && richting != 1) {
            return false;
        }

        String meldingSql = """
            SELECT status
            FROM melding
            WHERE meldingId = ?
            FOR UPDATE
            """;

        String selectTakenSql = """
            SELECT taakId
            FROM taak
            WHERE melding_meldingId = ?
            ORDER BY volgorde, taakId
            FOR UPDATE
            """;

        String updateSql = """
            UPDATE taak
            SET volgorde = ?
            WHERE taakId = ?
              AND melding_meldingId = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                String status;

                try (PreparedStatement stmt =
                             conn.prepareStatement(meldingSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        status = rs.getString("status");
                    }
                }

                if (!"OPEN".equals(status)
                        && !"IN_BEHANDELING".equals(status)) {
                    conn.rollback();
                    return false;
                }

                List<Integer> taakIds = new ArrayList<>();

                try (PreparedStatement stmt =
                             conn.prepareStatement(selectTakenSql)) {

                    stmt.setInt(1, meldingId);

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            taakIds.add(rs.getInt("taakId"));
                        }
                    }
                }

                int huidigeIndex = taakIds.indexOf(taakId);
                int nieuweIndex = huidigeIndex + richting;

                if (huidigeIndex < 0
                        || nieuweIndex < 0
                        || nieuweIndex >= taakIds.size()) {

                    conn.rollback();
                    return false;
                }

                Collections.swap(
                        taakIds,
                        huidigeIndex,
                        nieuweIndex
                );

                try (PreparedStatement stmt =
                             conn.prepareStatement(updateSql)) {

                    for (int i = 0; i < taakIds.size(); i++) {

                        stmt.setInt(1, i + 1);
                        stmt.setInt(2, taakIds.get(i));
                        stmt.setInt(3, meldingId);

                        stmt.addBatch();
                    }

                    stmt.executeBatch();
                }

                conn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }






}
