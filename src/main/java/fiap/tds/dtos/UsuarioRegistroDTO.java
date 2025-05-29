package fiap.tds.dtos;

import java.util.List;

public class UsuarioRegistroDTO {
    public String nomeCompleto;
    public String email;
    public String password; // Senha em texto plano vinda do formulário
    public String locationPreference;
    public List<String> subscribedAlerts; // Lista de Strings para os tipos de alerta

    // Construtor vazio
    public UsuarioRegistroDTO() {
    }

    // Construtor
    public UsuarioRegistroDTO(String nomeCompleto, String email, String password, String locationPreference, List<String> subscribedAlerts) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.password = password;
        this.locationPreference = locationPreference;
        this.subscribedAlerts = subscribedAlerts;
    }

    // Getters e Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public List<String> getSubscribedAlerts() {
        return subscribedAlerts;
    }

    public void setSubscribedAlerts(List<String> subscribedAlerts) {
        this.subscribedAlerts = subscribedAlerts;
    }
}