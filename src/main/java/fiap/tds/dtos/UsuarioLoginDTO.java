package fiap.tds.dtos;

public class UsuarioLoginDTO {
    public String email;
    public String password;

    // Construtor vazio
    public UsuarioLoginDTO() {
    }

    // Construtor

    public UsuarioLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters e Setters
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
}