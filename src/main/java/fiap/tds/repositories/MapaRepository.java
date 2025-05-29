package fiap.tds.repositories;

import fiap.tds.entities.Mapa;
import fiap.tds.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MapaRepository {
    private static final Logger logger = LogManager.getLogger(MapaRepository.class);

    private static final String TABLE_NAME = "ER_RISK_AREAS";

    /**
     * Registra uma nova área de risco (Mapa) no banco de dados.
     * O ‘ID’ é gerado pelo banco de dados (autoincremento).
     */
    public void registrar(Mapa mapa) {
        var sql = "INSERT INTO " + TABLE_NAME + " (latitude, longitude, radius, risk_level, title, description, reason, last_updated_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Solicita as chaves geradas

            stmt.setDouble(1, mapa.getLatitude());
            stmt.setDouble(2, mapa.getLongitude());
            stmt.setInt(3, mapa.getRadius());
            stmt.setString(4, mapa.getRiskLevel());
            stmt.setString(5, mapa.getTitle());
            stmt.setString(6, mapa.getDescription());
            stmt.setString(7, mapa.getReason());
            // Verifica se lastUpdatedTimestamp é nulo antes de converter
            if (mapa.getLastUpdatedTimestamp() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(mapa.getLastUpdatedTimestamp()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }


            int res = stmt.executeUpdate();
            if (res > 0) {
                // Recupera o ID gerado pelo banco
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mapa.setId(generatedKeys.getInt(1));
                        logger.info("✅ Área de Risco (Mapa) registrada com sucesso! ID gerado: " + mapa.getId());
                    } else {
                        logger.error("❌ Falha ao registrar Área de Risco (Mapa), nenhum ID obtido.");
                        throw new SQLException("Falha ao criar mapa, nenhum ID obtido.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao registrar Área de Risco (Mapa)", e);
        }
    }

    /**
     * Busca todas as áreas de risco (Mapas) registradas no banco de dados.
     */
    public List<Mapa> buscarTodos() {
        List<Mapa> lista = new ArrayList<>();
        var sql = "SELECT id, latitude, longitude, radius, risk_level, title, description, reason, last_updated_timestamp FROM " + TABLE_NAME;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mapa mapa = new Mapa();
                mapa.setId(rs.getInt("id")); // ID como int
                mapa.setLatitude(rs.getDouble("latitude"));
                mapa.setLongitude(rs.getDouble("longitude"));
                mapa.setRadius(rs.getInt("radius"));
                mapa.setRiskLevel(rs.getString("risk_level"));
                mapa.setTitle(rs.getString("title"));
                mapa.setDescription(rs.getString("description"));
                mapa.setReason(rs.getString("reason"));
                Timestamp ts = rs.getTimestamp("last_updated_timestamp");
                if (ts != null) {
                    mapa.setLastUpdatedTimestamp(ts.toLocalDateTime());
                }
                lista.add(mapa);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar todas as Áreas de Risco (Mapas)", e);
        }
        return lista;
    }

    /**
     * Busca uma área de risco (Mapa) específica pelo ID (int).
     */
    public Mapa buscarPorId(int id) { // Parâmetro id como int
        var sql = "SELECT id, latitude, longitude, radius, risk_level, title, description, reason, last_updated_timestamp FROM " + TABLE_NAME + " WHERE id = ?";
        Mapa mapa = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Usando setInt para o ID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                mapa = new Mapa();
                mapa.setId(rs.getInt("id"));
                mapa.setLatitude(rs.getDouble("latitude"));
                mapa.setLongitude(rs.getDouble("longitude"));
                mapa.setRadius(rs.getInt("radius"));
                mapa.setRiskLevel(rs.getString("risk_level"));
                mapa.setTitle(rs.getString("title"));
                mapa.setDescription(rs.getString("description"));
                mapa.setReason(rs.getString("reason"));
                Timestamp ts = rs.getTimestamp("last_updated_timestamp");
                if (ts != null) {
                    mapa.setLastUpdatedTimestamp(ts.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar Área de Risco (Mapa) por ID: " + id, e);
        }
        return mapa;
    }

    /**
     * Atualiza uma área de risco (Mapa) existente no banco de dados.
     * O ID do objeto mapa é usado para a condição WHERE.
     */
    public void atualizar(Mapa mapa) { // O ID já está no objeto mapa
        var sql = "UPDATE " + TABLE_NAME + " SET latitude = ?, longitude = ?, radius = ?, risk_level = ?, title = ?, description = ?, reason = ?, last_updated_timestamp = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, mapa.getLatitude());
            stmt.setDouble(2, mapa.getLongitude());
            stmt.setInt(3, mapa.getRadius());
            stmt.setString(4, mapa.getRiskLevel());
            stmt.setString(5, mapa.getTitle());
            stmt.setString(6, mapa.getDescription());
            stmt.setString(7, mapa.getReason());
            if (mapa.getLastUpdatedTimestamp() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(mapa.getLastUpdatedTimestamp()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }
            stmt.setInt(9, mapa.getId()); // Condição WHERE com ID int

            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Área de Risco (Mapa) atualizada com sucesso! ID: " + mapa.getId());
            } else {
                logger.warn("⚠️ Área de Risco (Mapa) com ID " + mapa.getId() + " não encontrada para atualização.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar Área de Risco (Mapa) ID: " + mapa.getId(), e);
        }
    }

    /**
     * Deleta uma área de risco (Mapa) do banco de dados pelo ID (int).
     */
    public void deletar(int id) { // Parâmetro id como int
        var sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Usando setInt para o ID
            int res = stmt.executeUpdate();

            if (res > 0) {
                logger.info("✅ Área de Risco (Mapa) deletada com sucesso! ID: " + id);
            } else {
                logger.warn("⚠️ Área de Risco (Mapa) com ID " + id + " não encontrada para exclusão.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao deletar Área de Risco (Mapa) ID: " + id, e);
        }
    }
}