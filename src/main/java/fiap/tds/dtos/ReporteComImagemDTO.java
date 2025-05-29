package fiap.tds.dtos;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload; // Importa o tipo FileUpload do RESTEasy Reactive

public class ReporteComImagemDTO {

    @RestForm("reporterName")
    @PartType(MediaType.TEXT_PLAIN)
    public String reporterName;

    @RestForm("eventType")
    @PartType(MediaType.TEXT_PLAIN)
    public String eventType;

    @RestForm("description")
    @PartType(MediaType.TEXT_PLAIN)
    public String description;

    @RestForm("location")
    @PartType(MediaType.TEXT_PLAIN)
    public String location;

    @RestForm("image") // Nome do campo do arquivo no FormData
    public FileUpload imageFile; // Campo para o arquivo de imagem

    @RestForm("userId")
    @PartType(MediaType.TEXT_PLAIN)
    public Integer userId; // Opcional

    // Construtor vazio
    public ReporteComImagemDTO() {
    }

    // Getters e Setters

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

    public FileUpload getImageFile() {
        return imageFile;
    }

    public void setImageFile(FileUpload imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}