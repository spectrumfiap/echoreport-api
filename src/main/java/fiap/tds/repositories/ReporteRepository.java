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
    private static final String ID_COLUMN_NAME_DB = "ID";

    /**
     * Registra um novo reporte no banco de dados.
     *
     * @param reporte O objeto Reporte a ser registrado.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void registrar(Reporte reporte) {
        String sql = "INSERT INTO " + TABLE_NAME +
                " (REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, ID_USUARIO, CREATED_AT, STATUS, SEVERITY, ADMIN_NOTES) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String[] columnNamesToReturn = new String[] { ID_COLUMN_NAME_DB };

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
            stmt.setString(8, reporte.getStatus());
            stmt.setString(9, reporte.getSeverity());
            stmt.setString(10, reporte.getAdminNotes());


            int res = stmt.executeUpdate();
            if (res > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reporte.setId(generatedKeys.getInt(1));
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

    /**
     * Busca todos os reportes no banco de dados.
     *
     * @return Uma lista de objetos Reporte.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public List<Reporte> buscarTodos() {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT ID, REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, ID_USUARIO, CREATED_AT, STATUS, SEVERITY, ADMIN_NOTES FROM " +
                TABLE_NAME + " ORDER BY CREATED_AT DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("ID"));
                reporte.setReporterName(rs.getString("REPORTER_NAME"));
                reporte.setEventType(rs.getString("EVENT_TYPE"));
                reporte.setDescription(rs.getString("DESCRIPTION"));
                reporte.setLocation(rs.getString("LOCATION"));
                reporte.setImageUrl(rs.getString("IMAGE_URL"));

                int userIdDb = rs.getInt("ID_USUARIO");
                if (rs.wasNull()) {
                    reporte.setUserId(null);
                } else {
                    reporte.setUserId(userIdDb);
                }

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    reporte.setCreatedAt(ts.toLocalDateTime());
                }
                reporte.setStatus(rs.getString("STATUS"));
                reporte.setSeverity(rs.getString("SEVERITY"));
                reporte.setAdminNotes(rs.getString("ADMIN_NOTES"));
                lista.add(reporte);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar todos os Reportes: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao buscar todos os reportes.", e);
        }
        return lista;
    }

    /**
     * Busca um reporte pelo ID no banco de dados.
     *
     * @param id O ID do reporte a ser buscado.
     * @return O objeto Reporte correspondente ao ID, ou null se não encontrado.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public Reporte buscarPorId(int id) {
        String sql = "SELECT ID, REPORTER_NAME, EVENT_TYPE, DESCRIPTION, LOCATION, IMAGE_URL, ID_USUARIO, CREATED_AT, STATUS, SEVERITY, ADMIN_NOTES FROM " +
                TABLE_NAME + " WHERE ID = ?";
        Reporte reporte = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reporte = new Reporte();
                reporte.setId(rs.getInt("ID"));
                reporte.setReporterName(rs.getString("REPORTER_NAME"));
                reporte.setEventType(rs.getString("EVENT_TYPE"));
                reporte.setDescription(rs.getString("DESCRIPTION"));
                reporte.setLocation(rs.getString("LOCATION"));
                reporte.setImageUrl(rs.getString("IMAGE_URL"));

                int userIdDb = rs.getInt("ID_USUARIO");
                if (rs.wasNull()) {
                    reporte.setUserId(null);
                } else {
                    reporte.setUserId(userIdDb);
                }

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    reporte.setCreatedAt(ts.toLocalDateTime());
                }
                reporte.setStatus(rs.getString("STATUS"));
                reporte.setSeverity(rs.getString("SEVERITY"));
                reporte.setAdminNotes(rs.getString("ADMIN_NOTES"));
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar Reporte por ID: " + id + " Erro: " + e.getMessage(), e);
            throw new RuntimeException("Erro de banco de dados ao buscar reporte por ID.", e);
        }
        return reporte;
    }

    /**
     * Atualiza um reporte no banco de dados.
     *
     * @param reporte O objeto Reporte com os novos dados.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void atualizar(Reporte reporte) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET EVENT_TYPE = ?, DESCRIPTION = ?, LOCATION = ?, IMAGE_URL = ?, STATUS = ?, REPORTER_NAME = ?, SEVERITY = ?, ADMIN_NOTES = ? " +
                "WHERE ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reporte.getEventType());
            stmt.setString(2, reporte.getDescription());
            stmt.setString(3, reporte.getLocation());
            stmt.setString(4, reporte.getImageUrl());
            stmt.setString(5, reporte.getStatus());
            stmt.setString(6, reporte.getReporterName());
            stmt.setString(7, reporte.getSeverity());
            stmt.setString(8, reporte.getAdminNotes());
            stmt.setInt(9, reporte.getId());

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

    /**
     * Deleta um reporte pelo ID no banco de dados.
     *
     * @param id O ID do reporte a ser deletado.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";

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