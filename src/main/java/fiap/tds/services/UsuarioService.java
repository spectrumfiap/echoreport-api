package fiap.tds.services;

import fiap.tds.dtos.UsuarioLoginDTO; // DTO para login
import fiap.tds.dtos.UsuarioRegistroDTO; // DTO para registro
import fiap.tds.entities.Usuario;
import fiap.tds.repositories.UsuarioRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.utils.PasswordUtil;

import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject; // Se PasswordUtil for um bean CDI

import java.time.LocalDateTime;
import java.util.Arrays; // Para o DTO de registro
import java.util.List;
// import java.util.UUID; // Não mais necessário se o ID for int autoincrementado

@ApplicationScoped
public class UsuarioService {

    private final UsuarioRepository repository = new UsuarioRepository();
    private final PasswordUtil passwordUtil = new PasswordUtil(); // Instanciação direta

    // Método registrar agora recebe o DTO
    public Usuario registrar(UsuarioRegistroDTO registroDTO) {
        if (registroDTO == null ||
                registroDTO.getNomeCompleto() == null || registroDTO.getNomeCompleto().trim().isEmpty() ||
                registroDTO.getEmail() == null || registroDTO.getEmail().trim().isEmpty() ||
                registroDTO.getPassword() == null || registroDTO.getPassword().isEmpty()) {
            throw new BadRequestException("Nome, email e senha são obrigatórios para registro.");
        }

        if (!isValidEmail(registroDTO.getEmail())) {
            throw new BadRequestException("Formato de email inválido.");
        }
        if (registroDTO.getPassword().length() < 6) { // Exemplo de validação de senha
            throw new BadRequestException("A senha deve ter pelo menos 6 caracteres.");
        }

        if (repository.buscarPorEmail(registroDTO.getEmail().toLowerCase()) != null) {
            throw new BadRequestException("Email já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        // O ID será gerado pelo banco, então não definimos aqui.
        novoUsuario.setNomeCompleto(registroDTO.getNomeCompleto());
        novoUsuario.setEmail(registroDTO.getEmail().toLowerCase());
        novoUsuario.setPasswordHash(passwordUtil.hashPassword(registroDTO.getPassword()));
        novoUsuario.setLocationPreference(registroDTO.getLocationPreference());

        if (registroDTO.getSubscribedAlerts() != null) {
            novoUsuario.setSubscribedAlerts(registroDTO.getSubscribedAlerts().toArray(new String[0]));
        } else {
            novoUsuario.setSubscribedAlerts(new String[0]);
        }

        novoUsuario.setRole("user"); // Papel padrão para novos registros
        novoUsuario.setCreatedAt(LocalDateTime.now());

        repository.registrar(novoUsuario); // O repositório irá preencher o ID no objeto novoUsuario
        novoUsuario.setPasswordHash(null); // Nunca retornar o hash
        return novoUsuario;
    }

    public Usuario login(UsuarioLoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty() ||
                loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new BadRequestException("Email e senha são obrigatórios para login.");
        }
        Usuario usuario = repository.buscarPorEmail(loginDTO.getEmail().toLowerCase());
        if (usuario == null) {
            // Mensagem genérica para não revelar se o email existe ou não
            throw new BadRequestException("Email ou senha inválidos.");
        }

        if (!passwordUtil.checkPassword(loginDTO.getPassword(), usuario.getPasswordHash())) {
            throw new BadRequestException("Email ou senha inválidos.");
        }

        usuario.setPasswordHash(null);
        return usuario;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = repository.listarTodos();
        usuarios.forEach(u -> u.setPasswordHash(null)); // Remove hash da lista
        return usuarios;
    }

    public Usuario buscarPorId(int userId) { // Parâmetro e tipo de retorno ajustados para int
        if (userId <= 0) {
            throw new BadRequestException("ID do usuário deve ser um número positivo.");
        }
        Usuario usuario = repository.buscarPorId(userId);
        if (usuario == null) {
            throw new NotFoundException("Usuário com ID " + userId + " não encontrado.");
        }
        usuario.setPasswordHash(null);
        return usuario;
    }

    public Usuario atualizar(int userId, Usuario usuarioComNovosDados) {
        if (userId <= 0 || usuarioComNovosDados == null) {
            throw new BadRequestException("ID e dados do usuário são obrigatórios para atualização.");
        }
        if (usuarioComNovosDados.getNomeCompleto() == null || usuarioComNovosDados.getNomeCompleto().trim().isEmpty() ||
                usuarioComNovosDados.getEmail() == null || usuarioComNovosDados.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Nome e email são obrigatórios para atualização.");
        }
        if (!isValidEmail(usuarioComNovosDados.getEmail())) {
            throw new BadRequestException("Formato de email inválido para atualização.");
        }

        Usuario existente = repository.buscarPorId(userId);
        if (existente == null) {
            throw new NotFoundException("Usuário com ID " + userId + " não encontrado para atualização.");
        }

        if (!existente.getEmail().equalsIgnoreCase(usuarioComNovosDados.getEmail().toLowerCase()) &&
                repository.buscarPorEmail(usuarioComNovosDados.getEmail().toLowerCase()) != null) {
            throw new BadRequestException("O novo email fornecido já está em uso por outra conta.");
        }

        existente.setNomeCompleto(usuarioComNovosDados.getNomeCompleto());
        existente.setEmail(usuarioComNovosDados.getEmail().toLowerCase());
        existente.setLocationPreference(usuarioComNovosDados.getLocationPreference());
        existente.setSubscribedAlerts(usuarioComNovosDados.getSubscribedAlerts());
        existente.setRole(usuarioComNovosDados.getRole() != null ? usuarioComNovosDados.getRole() : "user");

        repository.atualizar(existente);
        existente.setPasswordHash(null);
        return existente;
    }

    public void deletar(int userId) { // Parâmetro ajustado para int
        if (userId <= 0) {
            throw new BadRequestException("ID do usuário não pode ser nulo ou vazio para exclusão.");
        }
        buscarPorId(userId); // Verifica se existe
        repository.deletar(userId);
    }

    public void mudarSenha(int userId, String senhaAntiga, String senhaNova) {
        if (userId <= 0 || senhaAntiga == null || senhaNova == null || senhaNova.trim().isEmpty()) {
            throw new BadRequestException("ID do usuário, senha antiga e nova senha são obrigatórios.");
        }
        if (senhaNova.length() < 6) {
            throw new BadRequestException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        Usuario usuario = repository.buscarPorId(userId);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        if (!passwordUtil.checkPassword(senhaAntiga, usuario.getPasswordHash())) {
            throw new BadRequestException("Senha antiga incorreta.");
        }
        String novoPasswordHash = passwordUtil.hashPassword(senhaNova);
        repository.atualizarPasswordHash(userId, novoPasswordHash); // Chama o novo método do repositório
    }
}