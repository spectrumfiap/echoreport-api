package fiap.tds.repositories;

import fiap.tds.entities.Usuario;
import fiap.tds.infrastructure.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
        private static final Logger logger = LogManager.getLogger(UsuarioRepository.class);
        private static final String TABLE_NAME = "ER_USUARIOS";
        private static final String SUBSCRIBED_ALERTS_DELIMITER = ",";

        /**
         * Registra um novo usuário no banco de dados.
         *
         * @param usuario O objeto Usuario a ser registrado.
         * @throws RuntimeException Se ocorrer um erro de banco de dados.
         */
        public void registrar(Usuario usuario) {
            String sql = "INSERT INTO " + TABLE_NAME +
                    " (NOME_COMPLETO, EMAIL, PASSWORD_HASH, LOCATION_PREFERENCE, SUBSCRIBED_ALERTS, ROLE, CREATED_AT) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";


            String[] columnNamesToReturn = new String[] { "ID_USUARIO" }; // Nome da PK na tabela ER_USUARIOS

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, columnNamesToReturn)) { // Ou Statement.RETURN_GENERATED_KEYS se columnNamesToReturn não funcionar

                stmt.setString(1, usuario.getNomeCompleto());
                stmt.setString(2, usuario.getEmail().toLowerCase());
                stmt.setString(3, usuario.getPasswordHash());
                stmt.setString(4, usuario.getLocationPreference());

                if (usuario.getSubscribedAlerts() != null && usuario.getSubscribedAlerts().length > 0) {
                    stmt.setString(5, String.join(SUBSCRIBED_ALERTS_DELIMITER, usuario.getSubscribedAlerts()));
                } else {
                    stmt.setNull(5, Types.VARCHAR);
                }
                stmt.setString(6, usuario.getRole() != null ? usuario.getRole() : "user");
                stmt.setTimestamp(7, Timestamp.valueOf(usuario.getCreatedAt() != null ? usuario.getCreatedAt() : LocalDateTime.now()));

                int res = stmt.executeUpdate();
                if (res > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            usuario.setUserId(generatedKeys.getInt(1));
                            logger.info("✅ Usuário registrado com sucesso! ID: " + usuario.getUserId());
                        } else {
                            logger.error("❌ Falha ao registrar usuário, nenhum ID gerado obtido após INSERT.");
                            throw new SQLException("Falha ao criar usuário no BD, nenhum ID obtido.");
                        }
                    }
                } else {
                    logger.error("❌ Nenhuma linha afetada ao registrar Usuário no BD.");
                    throw new SQLException("Falha ao registrar usuário, nenhuma linha afetada.");
                }
            } catch (SQLException e) {
                logger.error("❌ Erro de SQL ao registrar usuário: " + e.getMessage(), e);
                throw new RuntimeException("Erro de banco de dados ao registrar usuário.", e);
            }
        }

    /**
        * Busca um usuário pelo email.
        *
        * @param email O email do usuário a ser buscado.
        * @return O objeto Usuario correspondente ao email, ou null se não encontrado.
        * @throws RuntimeException Se ocorrer um erro de banco de dados.
        */

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT ID_USUARIO, NOME_COMPLETO, EMAIL, PASSWORD_HASH, LOCATION_PREFERENCE, SUBSCRIBED_ALERTS, ROLE, CREATED_AT FROM " + TABLE_NAME + " WHERE EMAIL = ?";
        Usuario usuario = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUserId(rs.getInt("ID_USUARIO"));
                    usuario.setNomeCompleto(rs.getString("NOME_COMPLETO"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    usuario.setPasswordHash(rs.getString("PASSWORD_HASH"));
                    usuario.setLocationPreference(rs.getString("LOCATION_PREFERENCE"));
                    String alertsDb = rs.getString("SUBSCRIBED_ALERTS");
                    if (alertsDb != null && !alertsDb.isEmpty()) {
                        usuario.setSubscribedAlerts(alertsDb.split(SUBSCRIBED_ALERTS_DELIMITER));
                    } else {
                        usuario.setSubscribedAlerts(new String[0]);
                    }
                    usuario.setRole(rs.getString("ROLE"));
                    Timestamp ts = rs.getTimestamp("CREATED_AT");
                    if (ts != null) {
                        usuario.setCreatedAt(ts.toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar usuário por email: " + email, e);
            throw new RuntimeException("Erro de banco de dados ao buscar usuário.", e);
        }
        return usuario;
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param userId O ID do usuário a ser buscado.
     * @return O objeto Usuario correspondente ao ID, ou null se não encontrado.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public Usuario buscarPorId(int userId) { // Parâmetro e tipo de retorno ajustados para int
        String sql = "SELECT ID_USUARIO, NOME_COMPLETO, EMAIL, PASSWORD_HASH, LOCATION_PREFERENCE, SUBSCRIBED_ALERTS, ROLE, CREATED_AT FROM " + TABLE_NAME + " WHERE ID_USUARIO = ?";
        Usuario usuario = null;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUserId(rs.getInt("ID_USUARIO"));
                    usuario.setNomeCompleto(rs.getString("NOME_COMPLETO"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    usuario.setPasswordHash(rs.getString("PASSWORD_HASH"));
                    usuario.setLocationPreference(rs.getString("LOCATION_PREFERENCE"));
                    String alertsDb = rs.getString("SUBSCRIBED_ALERTS");
                    if (alertsDb != null && !alertsDb.isEmpty()) {
                        usuario.setSubscribedAlerts(alertsDb.split(SUBSCRIBED_ALERTS_DELIMITER));
                    } else {
                        usuario.setSubscribedAlerts(new String[0]);
                    }
                    usuario.setRole(rs.getString("ROLE"));
                    Timestamp ts = rs.getTimestamp("CREATED_AT");
                    if (ts != null) {
                        usuario.setCreatedAt(ts.toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao buscar usuário por ID: " + userId, e);
            throw new RuntimeException("Erro de banco de dados ao buscar usuário por ID.", e);
        }
        return usuario;
    }

    /**
     * Lista todos os usuários cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Usuario representando todos os usuários.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, NOME_COMPLETO, EMAIL, LOCATION_PREFERENCE, SUBSCRIBED_ALERTS, ROLE, CREATED_AT FROM " + TABLE_NAME + " ORDER BY NOME_COMPLETO"; // Removido PASSWORD_HASH da listagem geral
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUserId(rs.getInt("ID_USUARIO"));
                usuario.setNomeCompleto(rs.getString("NOME_COMPLETO"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setLocationPreference(rs.getString("LOCATION_PREFERENCE"));
                String alertsDb = rs.getString("SUBSCRIBED_ALERTS");
                if (alertsDb != null && !alertsDb.isEmpty()) {
                    usuario.setSubscribedAlerts(alertsDb.split(SUBSCRIBED_ALERTS_DELIMITER));
                } else {
                    usuario.setSubscribedAlerts(new String[0]);
                }
                usuario.setRole(rs.getString("ROLE"));
                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    usuario.setCreatedAt(ts.toLocalDateTime());
                }
                lista.add(usuario);
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao listar todos os usuários", e);
            throw new RuntimeException("Erro de banco de dados ao listar usuários.", e);
        }
        return lista;
    }

    /**
     * Atualiza os dados de um usuário no banco de dados.
     *
     * @param usuario O objeto Usuario com os dados atualizados.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE " + TABLE_NAME + " SET NOME_COMPLETO = ?, EMAIL = ?, LOCATION_PREFERENCE = ?, SUBSCRIBED_ALERTS = ?, ROLE = ? WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEmail().toLowerCase());
            stmt.setString(3, usuario.getLocationPreference());
            if (usuario.getSubscribedAlerts() != null && usuario.getSubscribedAlerts().length > 0) {
                stmt.setString(4, String.join(SUBSCRIBED_ALERTS_DELIMITER, usuario.getSubscribedAlerts()));
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.setString(5, usuario.getRole());
            stmt.setInt(6, usuario.getUserId());

            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Usuário atualizado com sucesso! ID: " + usuario.getUserId());
            } else {
                logger.warn("⚠️ Usuário com ID " + usuario.getUserId() + " não encontrado para atualização.");
                // Considerar lançar NotFoundException se res == 0
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar usuário ID: " + usuario.getUserId(), e);
            throw new RuntimeException("Erro de banco de dados ao atualizar usuário.", e);
        }
    }


    /**
     * Atualiza o hash da senha de um usuário no banco de dados.
     *
     * @param userId O ID do usuário cujo hash de senha será atualizado.
     * @param novoPasswordHash O novo hash da senha a ser definido.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void atualizarPasswordHash(int userId, String novoPasswordHash) {
        String sql = "UPDATE " + TABLE_NAME + " SET PASSWORD_HASH = ? WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoPasswordHash);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            logger.info("✅ Hash de senha atualizado para o usuário ID: " + userId);
        } catch (SQLException e) {
            logger.error("❌ Erro ao atualizar hash de senha do usuário ID: " + userId, e);
            throw new RuntimeException("Erro de banco de dados ao atualizar senha.", e);
        }
    }

    /**
     * Deleta um usuário pelo ID no banco de dados.
     *
     * @param userId O ID do usuário a ser deletado.
     * @throws RuntimeException Se ocorrer um erro de banco de dados.
     */
    public void deletar(int userId) { // Parâmetro e tipo de retorno ajustados para int
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int res = stmt.executeUpdate();
            if (res > 0) {
                logger.info("✅ Usuário deletado com sucesso! ID: " + userId);
            } else {
                logger.warn("⚠️ Usuário com ID " + userId + " não encontrado para exclusão.");
                // Considerar lançar NotFoundException se res == 0
            }
        } catch (SQLException e) {
            logger.error("❌ Erro ao deletar usuário ID: " + userId, e);
            throw new RuntimeException("Erro de banco de dados ao deletar usuário.", e);
        }
    }
}