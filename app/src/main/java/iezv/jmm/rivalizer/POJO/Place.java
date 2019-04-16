package iezv.jmm.rivalizer.POJO;

public class Place {
    private int idPlace;
    private String cloudId;
    private String name;
    private String address;
    private String coordinates;
    private String urlPhoto;
    private String review;

    public Place () {}

    public Place(int idPlace, String name, String address, String coordinates, String urlPhoto, String review) {
        this.idPlace = idPlace;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.urlPhoto = urlPhoto;
        this.review = review;
    }

    public int getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }
}
