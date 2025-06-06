package fiap.tds.repositories;

import fiap.tds.entities.Alerta;
import fiap.tds.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaRepository {
    private static final Logger logger = LogManager.getLogger(AlertaRepository.class);
    // Nome da tabela no banco de dados
    private static final String TABLE_NAME = "ER_ALERTAS";

    /**
     * Registra um novo alerta no banco de dados.
     * O ID é gerado pelo banco de dados (autoincremento).
     */
    public void registrar(Alerta alerta) {
        var sql = "INSERT INTO " + TABLE_NAME +
                " (title, severity, source, description, published_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, alerta.getTitle());
            stmt.setString(2, alerta.getSeverity());
            stmt.setString(3, alerta.getSource());
            stmt.setString(4, alerta.getDescription());
            if (alerta.getPublishedAt() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(alerta.getPublishedAt()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // Default para agora se nulo
            }

            int res = stmt.executeUpdate();
            if (res > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        alerta.setId(generatedKeys.getInt(1));
                        logger.info("✅ Alerta registrado com sucesso! ID gerado: " + alerta.getId());
                    } else {
                        logger.error("❌ Falha ao registrar Alerta, nenhum ID obtido.");
                        throw new SQLException("Falha ao criar alerta, nenhum ID obtido.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao registrar Alerta", e);
        }
    }

    /**
     * Busca todos os alertas registrados no banco de dados.
     */
    public List<Alerta> buscarTodos() {
        List<Alerta> lista = new ArrayList<>();
        var sql = "SELECT id, title, severity, source, description, published_at FROM " + TABLE_NAME + " ORDER BY published_at DESC"; // Ordena pelos mais recentes

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Alerta alerta = new Alerta();
                alerta.setId(rs.getInt("id"));
                alerta.setTitle(rs.getString("title"));
                alerta.setSeverity(rs.getString("severity"));
                alerta.setSource(rs.getString("source"));
                alerta.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("published_at");
                if (ts != null) {
                    alerta.setPublishedAt(ts.toLocalDateTime());
                }
                lista.add(alerta);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar todos os Alertas", e);
        }
        return lista;
    }

    /**
     * Busca um alerta específico pelo ID (int).
     */
    public Alerta buscarPorId(int id) {
        var sql = "SELECT id, title, severity, source, description, published_at FROM " + TABLE_NAME + " WHERE id = ?";
        Alerta alerta = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alerta = new Alerta();
                alerta.setId(rs.getInt("id"));
                alerta.setTitle(rs.getString("title"));
                alerta.setSeverity(rs.getString("severity"));
                alerta.setSource(rs.getString("source"));
                alerta.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("published_at");
                if (ts != null) {
                    alerta.setPublishedAt(ts.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar Alerta por ID: " + id, e);
        }
        return alerta;
    }

    /**
     * Atualiza um alerta existente no banco de dados.
     */
    public void atualizar(Alerta alerta) {
        var sql = "UPDATE " + TABLE_NAME +
                " SET title = ?, severity = ?, source = ?, description = ?, published_at = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alerta.getTitle());
            stmt.setString(2, alerta.getSeverity());
            stmt.setString(3, alerta.getSource());
            stmt.setString(4, alerta.getDescription());
            if (alerta.getPublishedAt() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(alerta.getPublishedAt()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            stmt.setInt(6, alerta.getId()); // Condição WHERE

            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Alerta atualizado com sucesso! ID: " + alerta.getId());
            } else {
                logger.warn("⚠️ Alerta com ID " + alerta.getId() + " não encontrado para atualização.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar Alerta ID: " + alerta.getId(), e);
        }
    }

    /**
     * Deleta um alerta do banco de dados pelo ID (int).
     */
    public void deletar(int id) {
        var sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int res = stmt.executeUpdate();

            if (res > 0) {
                logger.info("✅ Alerta deletado com sucesso! ID: " + id);
            } else {
                logger.warn("⚠️ Alerta com ID " + id + " não encontrado para exclusão.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao deletar Alerta ID: " + id, e);
        }
    }
}