package fiap.tds.entities;

import java.time.LocalDateTime;

public class Alerta {
    private int id;
    private String title;
    private String severity; // 'Alto', 'Médio', 'Baixo'
    private String source; // Fonte do alerta (Defesa Civil, Comunidade, etc.)
    private String description;
    private LocalDateTime publishedAt;

    // Construtor vazio
    public Alerta() {
    }

    // Construtor
    public Alerta(int id, String title, String severity, String source, String description, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.severity = severity;
        this.source = source;
        this.description = description;
        this.publishedAt = publishedAt;
    }

    // Getters e Setters para todos os campos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

}