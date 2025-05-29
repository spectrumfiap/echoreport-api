package fiap.tds.entities;
import java.util.Arrays;

public class Abrigo {
    private int id;
    private String name;
    private String imageUrl;
    private String address;
    private String neighborhood;
    private String cityState;
    private String zipCode;
    private String contactPhone;
    private String contactEmail;
    private String capacityStatus;
    private String[] servicesOffered; // Array de Strings
    private String targetAudience;
    private String operatingHours;
    private String observations;
    private String googleMapsUrl;

    // Construtor vazio
    public Abrigo() {
    }

    // Construtor completo
    public Abrigo(int id, String name, String imageUrl, String address, String neighborhood,
                  String cityState, String zipCode, String contactPhone, String contactEmail,
                  String capacityStatus, String[] servicesOffered, String targetAudience,
                  String operatingHours, String observations, String googleMapsUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.neighborhood = neighborhood;
        this.cityState = cityState;
        this.zipCode = zipCode;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.capacityStatus = capacityStatus;
        this.servicesOffered = servicesOffered;
        this.targetAudience = targetAudience;
        this.operatingHours = operatingHours;
        this.observations = observations;
        this.googleMapsUrl = googleMapsUrl;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCapacityStatus() {
        return capacityStatus;
    }

    public void setCapacityStatus(String capacityStatus) {
        this.capacityStatus = capacityStatus;
    }

    public String[] getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(String[] servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }

}