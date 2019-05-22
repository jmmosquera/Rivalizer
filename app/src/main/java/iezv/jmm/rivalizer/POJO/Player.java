package iezv.jmm.rivalizer.POJO;

public class Player {

    private String idPlayer;
    private String name;
    private String urlPhoto;
    private String zipCode;

    public Player(String idPlayer, String name, String urlPhoto, String zipCode){
        this.idPlayer = idPlayer;
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.zipCode = zipCode;

    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
