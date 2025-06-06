package fiap.tds.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia para hashing.");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12)); // O 12 é o log_rounds
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || plainPassword.isEmpty() || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Acontece se o hash não for um hash BCrypt válido
            System.err.println("Erro ao verificar senha (formato de hash inválido?): " + e.getMessage());
            return false;
        }
    }
}