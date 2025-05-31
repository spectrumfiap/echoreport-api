package fiap.tds.entities;

import java.time.LocalDateTime;

public class    Reporte {
    private int id;
    private String reporterName;
    private String eventType;
    private String description;
    private String location;
    private String imageUrl;
    private Integer userId; // ID do usuário que fez o reporte, pode ser nulo
    private LocalDateTime createdAt; // Data e hora da criação do reporte
    private String status; // (ex: "novo", "verificado", "falso_positivo")
    private String severity; // (ex: "baixo", "médio", "alto")
    private String adminNotes; // Notas do administrador sobre o reporte, pode ser nulo ou vazio

    // Construtor vazio
    public Reporte() {
    }

    // Construtor
    public Reporte(int id, String reporterName, String eventType, String description, String location, String imageUrl, Integer userId, LocalDateTime createdAt, String status, String severity, String adminNotes) {
        this.id = id;
        this.reporterName = reporterName;
        this.eventType = eventType;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
        this.severity = severity;
        this.adminNotes = adminNotes;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}


