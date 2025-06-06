package fiap.tds.entities;

import java.time.LocalDateTime;

public class Mapa {
    private int id;
    private double latitude;
    private double longitude;
    private int radius;
    private String riskLevel;
    private String title;
    private String description;
    private String reason;
    private LocalDateTime lastUpdatedTimestamp;

    // Construtor vazio
    public Mapa() {
    }

    // Construtor completo
    public Mapa(int id, double latitude, double longitude, int radius, String riskLevel, String title, String description, String reason, LocalDateTime lastUpdatedTimestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.riskLevel = riskLevel;
        this.title = title;
        this.description = description;
        this.reason = reason;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(LocalDateTime lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
}