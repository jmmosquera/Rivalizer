package iezv.jmm.rivalizer.POJO;

import java.util.ArrayList;

public class Rival {

    private int id;
    private String cloud_id;
    private String name;
    private String urlPhoto;
    private String fPlace;
    private int idGames;
    private ArrayList<String> games;

    public Rival(){}
    public Rival(int id, String cloud_id, String name, String urlPhoto, String fPlace, ArrayList<String> games){
        this.id = id;
        this.cloud_id = cloud_id;
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.fPlace = fPlace;
        this.games = games;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCloud_id() {
        return cloud_id;
    }

    public void setCloud_id(String cloud_id) {
        this.cloud_id = cloud_id;
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

    public String getfPlace() {
        return fPlace;
    }

    public void setfPlace(String fPlace) {
        this.fPlace = fPlace;
    }

    public ArrayList<String> getGames() {
        return games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }

    public int getIdGames() {
        return idGames;
    }

    public void setIdGames(int idGames) {
        this.idGames = idGames;
    }
}
