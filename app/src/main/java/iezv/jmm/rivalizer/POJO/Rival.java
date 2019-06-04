package iezv.jmm.rivalizer.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Rival implements Parcelable {

    private int id;
    private String cloud_id;
    private String name;
    private String urlPhoto;
    private String fPlace;
    private int idGames;
    private ArrayList<Game> games;

    public Rival(){}

    public Rival(int id, String cloud_id, String name, String urlPhoto, String fPlace, ArrayList<Game> games){
        this.id = id;
        this.cloud_id = cloud_id;
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.fPlace = fPlace;
        this.games = games;
    }


    protected Rival(Parcel in) {
        id = in.readInt();
        cloud_id = in.readString();
        name = in.readString();
        urlPhoto = in.readString();
        fPlace = in.readString();
        idGames = in.readInt();
        games = in.createTypedArrayList(Game.CREATOR);
    }

    public static final Creator<Rival> CREATOR = new Creator<Rival>() {
        @Override
        public Rival createFromParcel(Parcel in) {
            return new Rival(in);
        }

        @Override
        public Rival[] newArray(int size) {
            return new Rival[size];
        }
    };

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

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public int getIdGames() {
        return idGames;
    }

    public void setIdGames(int idGames) {
        this.idGames = idGames;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cloud_id);
        dest.writeString(name);
        dest.writeString(urlPhoto);
        dest.writeString(fPlace);
        dest.writeInt(idGames);
        dest.writeTypedList(games);
    }
}
