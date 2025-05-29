package fiap.tds.repositories;

import fiap.tds.entities.Abrigo;
import fiap.tds.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbrigoRepository {
    private static final Logger logger = LogManager.getLogger(AbrigoRepository.class);
    private static final String TABLE_NAME = "ER_ABRIGOS";
    private static final String SERVICES_DELIMITER = ";"; // Delimitador para servicesOffered

    /**
     * Registra um novo abrigo no banco de dados.
     * O ID é gerado pelo banco de dados (autoincremento).
     */
    public void registrar(Abrigo abrigo) {
        var sql = "INSERT INTO " + TABLE_NAME +
                " (name, image_url, address, neighborhood, city_state, zip_code, contact_phone, contact_email, capacity_status, services_offered, target_audience, operating_hours, observations, Maps_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, abrigo.getName());
            stmt.setString(2, abrigo.getImageUrl());
            stmt.setString(3, abrigo.getAddress());
            stmt.setString(4, abrigo.getNeighborhood());
            stmt.setString(5, abrigo.getCityState());
            stmt.setString(6, abrigo.getZipCode());
            stmt.setString(7, abrigo.getContactPhone());
            stmt.setString(8, abrigo.getContactEmail());
            stmt.setString(9, abrigo.getCapacityStatus());
            // Converte o array de serviços para uma string delimitada
            if (abrigo.getServicesOffered() != null && abrigo.getServicesOffered().length > 0) {
                stmt.setString(10, String.join(SERVICES_DELIMITER, abrigo.getServicesOffered()));
            } else {
                stmt.setNull(10, Types.VARCHAR);
            }
            stmt.setString(11, abrigo.getTargetAudience());
            stmt.setString(12, abrigo.getOperatingHours());
            stmt.setString(13, abrigo.getObservations());
            stmt.setString(14, abrigo.getGoogleMapsUrl());

            int res = stmt.executeUpdate();
            if (res > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        abrigo.setId(generatedKeys.getInt(1));
                        logger.info("✅ Abrigo registrado com sucesso! ID gerado: " + abrigo.getId());
                    } else {
                        logger.error("❌ Falha ao registrar Abrigo, nenhum ID obtido.");
                        throw new SQLException("Falha ao criar abrigo, nenhum ID obtido.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao registrar Abrigo", e);
        }
    }

    /**
     * Busca todos os abrigos registrados no banco de dados.
     */
    public List<Abrigo> buscarTodos() {
        List<Abrigo> lista = new ArrayList<>();
        var sql = "SELECT id, name, image_url, address, neighborhood, city_state, zip_code, contact_phone, contact_email, capacity_status, services_offered, target_audience, operating_hours, observations, Maps_url FROM " + TABLE_NAME;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Abrigo abrigo = new Abrigo();
                abrigo.setId(rs.getInt("id"));
                abrigo.setName(rs.getString("name"));
                abrigo.setImageUrl(rs.getString("image_url"));
                abrigo.setAddress(rs.getString("address"));
                abrigo.setNeighborhood(rs.getString("neighborhood"));
                abrigo.setCityState(rs.getString("city_state"));
                abrigo.setZipCode(rs.getString("zip_code"));
                abrigo.setContactPhone(rs.getString("contact_phone"));
                abrigo.setContactEmail(rs.getString("contact_email"));
                abrigo.setCapacityStatus(rs.getString("capacity_status"));
                String servicesDb = rs.getString("services_offered");
                if (servicesDb != null && !servicesDb.isEmpty()) {
                    abrigo.setServicesOffered(servicesDb.split(SERVICES_DELIMITER));
                } else {
                    abrigo.setServicesOffered(new String[0]); // Array vazio se não houver serviços
                }
                abrigo.setTargetAudience(rs.getString("target_audience"));
                abrigo.setOperatingHours(rs.getString("operating_hours"));
                abrigo.setObservations(rs.getString("observations"));
                abrigo.setGoogleMapsUrl(rs.getString("Maps_url"));
                lista.add(abrigo);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar todos os Abrigos", e);
        }
        return lista;
    }

    /**
     * Busca um abrigo específico pelo ID (int).
     */
    public Abrigo buscarPorId(int id) {
        var sql = "SELECT id, name, image_url, address, neighborhood, city_state, zip_code, contact_phone, contact_email, capacity_status, services_offered, target_audience, operating_hours, observations, Maps_url FROM " + TABLE_NAME + " WHERE id = ?";
        Abrigo abrigo = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                abrigo = new Abrigo();
                abrigo.setId(rs.getInt("id"));
                abrigo.setName(rs.getString("name"));
                abrigo.setImageUrl(rs.getString("image_url"));
                abrigo.setAddress(rs.getString("address"));
                abrigo.setNeighborhood(rs.getString("neighborhood"));
                abrigo.setCityState(rs.getString("city_state"));
                abrigo.setZipCode(rs.getString("zip_code"));
                abrigo.setContactPhone(rs.getString("contact_phone"));
                abrigo.setContactEmail(rs.getString("contact_email"));
                abrigo.setCapacityStatus(rs.getString("capacity_status"));
                String servicesDb = rs.getString("services_offered");
                if (servicesDb != null && !servicesDb.isEmpty()) {
                    abrigo.setServicesOffered(servicesDb.split(SERVICES_DELIMITER));
                } else {
                    abrigo.setServicesOffered(new String[0]);
                }
                abrigo.setTargetAudience(rs.getString("target_audience"));
                abrigo.setOperatingHours(rs.getString("operating_hours"));
                abrigo.setObservations(rs.getString("observations"));
                abrigo.setGoogleMapsUrl(rs.getString("Maps_url"));
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar Abrigo por ID: " + id, e);
        }
        return abrigo;
    }

    /**
     * Atualiza um abrigo existente no banco de dados.
     */
    public void atualizar(Abrigo abrigo) {
        var sql = "UPDATE " + TABLE_NAME + " SET name = ?, image_url = ?, address = ?, neighborhood = ?, city_state = ?, zip_code = ?, contact_phone = ?, contact_email = ?, capacity_status = ?, services_offered = ?, target_audience = ?, operating_hours = ?, observations = ?, Maps_url = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abrigo.getName());
            stmt.setString(2, abrigo.getImageUrl());
            stmt.setString(3, abrigo.getAddress());
            stmt.setString(4, abrigo.getNeighborhood());
            stmt.setString(5, abrigo.getCityState());
            stmt.setString(6, abrigo.getZipCode());
            stmt.setString(7, abrigo.getContactPhone());
            stmt.setString(8, abrigo.getContactEmail());
            stmt.setString(9, abrigo.getCapacityStatus());
            if (abrigo.getServicesOffered() != null && abrigo.getServicesOffered().length > 0) {
                stmt.setString(10, String.join(SERVICES_DELIMITER, abrigo.getServicesOffered()));
            } else {
                stmt.setNull(10, Types.VARCHAR);
            }
            stmt.setString(11, abrigo.getTargetAudience());
            stmt.setString(12, abrigo.getOperatingHours());
            stmt.setString(13, abrigo.getObservations());
            stmt.setString(14, abrigo.getGoogleMapsUrl());
            stmt.setInt(15, abrigo.getId()); // Condição WHERE

            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Abrigo atualizado com sucesso! ID: " + abrigo.getId());
            } else {
                logger.warn("⚠️ Abrigo com ID " + abrigo.getId() + " não encontrado para atualização.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar Abrigo ID: " + abrigo.getId(), e);
        }
    }

    /**
     * Deleta um abrigo do banco de dados pelo ID (int).
     */
    public void deletar(int id) {
        var sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int res = stmt.executeUpdate();

            if (res > 0) {
                logger.info("✅ Abrigo deletado com sucesso! ID: " + id);
            } else {
                logger.warn("⚠️ Abrigo com ID " + id + " não encontrado para exclusão.");
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao deletar Abrigo ID: " + id, e);
        }
    }
}