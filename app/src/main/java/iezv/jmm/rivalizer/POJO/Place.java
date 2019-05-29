package iezv.jmm.rivalizer.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private String idPlace;
    private String name;
    private String address;
    private String coordinates;
    private String urlPhoto;
    private String review;
    private int validated;

    public Place () {}

    public Place(String idPlace, String name, String address, String urlPhoto, String review) {
        this.idPlace = idPlace;
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.review = review;
        this.validated = 1;
    }

    protected Place(Parcel in) {
        idPlace = in.readString();
        name = in.readString();
        address = in.readString();
        coordinates = in.readString();
        urlPhoto = in.readString();
        review = in.readString();
        validated = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPlace);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(coordinates);
        dest.writeString(urlPhoto);
        dest.writeString(review);
        dest.writeInt(validated);
    }
}
