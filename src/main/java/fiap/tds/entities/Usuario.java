package fiap.tds.entities;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Usuario {
    private int userId; // ID do usuário como int (PK)
    private String nomeCompleto;
    private String email;
    private String passwordHash;
    private String locationPreference;
    private String[] subscribedAlerts;
    private String role;
    private LocalDateTime createdAt;

    // Construtor vazio
    public Usuario() {
    }

    // Construtor completo
    public Usuario(int userId, String nomeCompleto, String email, String passwordHash, String locationPreference, String[] subscribedAlerts, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.passwordHash = passwordHash;
        this.locationPreference = locationPreference;
        this.subscribedAlerts = subscribedAlerts;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String[] getSubscribedAlerts() {
        return subscribedAlerts;
    }

    public void setSubscribedAlerts(String[] subscribedAlerts) {
        this.subscribedAlerts = subscribedAlerts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}