package fiap.tds.entities;

import java.time.LocalDateTime;

public class Reporte {
    private int id;
    private String reporterName;
    private String eventType;
    private String description;
    private String location;
    private String imageUrl; // URL ou caminho para a imagem, após o ‘upload’
    private Integer userId; // ID do usuário que fez o reporte, pode ser nulo
    private LocalDateTime createdAt; // Data e hora da criação do reporte

    // Construtor vazio
    public Reporte() {
    }

    // Construtor
    public Reporte(int id, String reporterName, String eventType, String description, String location, String imageUrl, Integer userId, LocalDateTime createdAt) {
        this.id = id;
        this.reporterName = reporterName;
        this.eventType = eventType;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


