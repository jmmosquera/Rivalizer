package iezv.jmm.rivalizer.POJO;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private String idGame;
    private String name;
    private String urlPhoto;
    private String description;
    private String rules;
    private int validated;

    public Game () {}

    public Game(String idGame, String name, String urlPhoto, String description, String rules) {
        this.idGame = idGame;
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.description = description;
        this.rules = rules;
        this.validated = 0;
    }

    protected Game(Parcel in) {
        idGame = in.readString();
        name = in.readString();
        urlPhoto = in.readString();
        description = in.readString();
        rules = in.readString();
        validated = in.readInt();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getValidated() {
        return validated;
    }

    public void setValidated(int validated) {
        this.validated = validated;
    }

    @Override
    public String toString() {
        return "Game{" +
                "idGame='" + idGame + '\'' +
                ", name='" + name + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", description='" + description + '\'' +
                ", rules='" + rules + '\'' +
                ", validated=" + validated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idGame);
        dest.writeString(name);
        dest.writeString(urlPhoto);
        dest.writeString(description);
        dest.writeString(rules);
        dest.writeInt(validated);
    }
}
