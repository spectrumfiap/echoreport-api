package fiap.tds.repositories;

import fiap.tds.entities.Reporte;
import fiap.tds.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReporteRepository {
    private static final Logger logger = LogManager.getLogger(ReporteRepository.class);
    private static final String TABLE_NAME = "ER_REPORTES";
    private static final String ID_COLUMN_NAME_DB = "ID"; // Nome da coluna ID no banco

    public void registrar(Reporte reporte) {
        // ID não é incluído no INSERT pois é autoincrementado
        String sql = "INSERT INTO " + TABLE_NAME +
                " (REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, USER_ID, CREATED_AT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String[] columnNamesToReturn = new String[] { ID_COLUMN_NAME_DB }; // Especifica a coluna ID

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, columnNamesToReturn)) {

            stmt.setString(1, reporte.getReporterName());
            stmt.setString(2, reporte.getEventType());
            stmt.setString(3, reporte.getDescription());
            stmt.setString(4, reporte.getLocation());
            stmt.setString(5, reporte.getImageUrl());

            if (reporte.getUserId() != null) {
                stmt.setInt(6, reporte.getUserId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setTimestamp(7, Timestamp.valueOf(reporte.getCreatedAt() != null ? reporte.getCreatedAt() : LocalDateTime.now()));

            int res = stmt.executeUpdate();
            if (res > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reporte.setId(generatedKeys.getInt(1)); // Pega pelo índice
                        logger.info("✅ Reporte registrado com sucesso! ID gerado: " + reporte.getId());
                    } else {
                        throw new SQLException("Falha ao criar reporte, nenhum ID obtido.");
                    }
                }
            } else {
                throw new SQLException("Falha ao registrar reporte, nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro de SQL ao registrar Reporte: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao tentar registrar o reporte.", e);
        }
    }

    public List<Reporte> buscarTodos() {
        List<Reporte> lista = new ArrayList<>();
        // Usa ID como nome da coluna
        String sql = "SELECT ID, REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, USER_ID, CREATED_AT FROM " +
                TABLE_NAME + " ORDER BY CREATED_AT DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("ID")); // CORRIGIDO para "ID"
                reporte.setReporterName(rs.getString("REPORTER_NAME"));
                reporte.setEventType(rs.getString("EVENT_TYPE"));
                reporte.setDescription(rs.getString("DESCRIPTION"));
                reporte.setLocation(rs.getString("LOCATION"));
                reporte.setImageUrl(rs.getString("IMAGE_URL"));

                int userIdDb = rs.getInt("USER_ID");
                if (rs.wasNull()) {
                    reporte.setUserId(null);
                } else {
                    reporte.setUserId(userIdDb);
                }

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    reporte.setCreatedAt(ts.toLocalDateTime());
                }
                lista.add(reporte);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar todos os Reportes: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao buscar todos os reportes.", e);
        }
        return lista;
    }

    public Reporte buscarPorId(int id) {
        String sql = "SELECT ID, REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, USER_ID, CREATED_AT FROM " +
                TABLE_NAME + " WHERE ID = ?"; // CORRIGIDO para "ID"
        Reporte reporte = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reporte = new Reporte();
                reporte.setId(rs.getInt("ID")); // CORRIGIDO para "ID"
                reporte.setReporterName(rs.getString("REPORTER_NAME"));
                reporte.setEventType(rs.getString("EVENT_TYPE"));
                reporte.setDescription(rs.getString("DESCRIPTION"));
                reporte.setLocation(rs.getString("LOCATION"));
                reporte.setImageUrl(rs.getString("IMAGE_URL"));

                int userIdDb = rs.getInt("USER_ID");
                if (rs.wasNull()) {
                    reporte.setUserId(null);
                } else {
                    reporte.setUserId(userIdDb);
                }

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    reporte.setCreatedAt(ts.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar Reporte por ID: " + id + " Erro: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao buscar reporte por ID.", e);
        }
        return reporte;
    }

    public void atualizar(Reporte reporte) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET EVENT_TYPE = ?, DESCRIPTION = ?, LOCATION = ?, IMAGE_URL = ? " +
                "WHERE ID = ?"; // CORRIGIDO para "ID"

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reporte.getEventType());
            stmt.setString(2, reporte.getDescription());
            stmt.setString(3, reporte.getLocation());
            stmt.setString(4, reporte.getImageUrl());
            stmt.setInt(5, reporte.getId());

            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Reporte atualizado com sucesso! ID: " + reporte.getId());
            } else {
                logger.warn("⚠️ Reporte com ID " + reporte.getId() + " não encontrado para atualização ou nenhum dado alterado.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar Reporte ID: " + reporte.getId() + " Erro: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao atualizar reporte.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?"; // CORRIGIDO para "ID"

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int res = stmt.executeUpdate();

            if (res > 0) {
                logger.info("✅ Reporte deletado com sucesso! ID: " + id);
            } else {
                logger.warn("⚠️ Reporte com ID " + id + " não encontrado para exclusão.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao deletar Reporte ID: " + id + " Erro: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao deletar reporte.", e);
        }
    }
}