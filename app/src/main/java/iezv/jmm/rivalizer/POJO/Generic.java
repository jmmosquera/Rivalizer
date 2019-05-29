package iezv.jmm.rivalizer.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Generic implements Parcelable {
    private String idGeneric;
    private String nameGeneric;
    private String photoGeneric;

    public Generic () {}

    public Generic(String idGeneric, String nameGeneric, String photoGeneric){
        this.idGeneric = idGeneric;
        this.nameGeneric = nameGeneric;
        this.photoGeneric = photoGeneric;
    }

    protected Generic(Parcel in) {
        idGeneric = in.readString();
        nameGeneric = in.readString();
        photoGeneric = in.readString();
    }

    public static final Creator<Generic> CREATOR = new Creator<Generic>() {
        @Override
        public Generic createFromParcel(Parcel in) {
            return new Generic(in);
        }

        @Override
        public Generic[] newArray(int size) {
            return new Generic[size];
        }
    };

    public String getIdGeneric() {
        return idGeneric;
    }

    public void setIdGeneric(String idGeneric) {
        this.idGeneric = idGeneric;
    }

    public String getNameGeneric() {
        return nameGeneric;
    }

    public void setNameGeneric(String nameGeneric) {
        this.nameGeneric = nameGeneric;
    }

    public String getPhotoGeneric() {
        return photoGeneric;
    }

    public void setPhotoGeneric(String photoGeneric) {
        this.photoGeneric = photoGeneric;
    }

    @Override
    public String toString() {
        return "Generic{" +
                "idGeneric='" + idGeneric + '\'' +
                ", nameGeneric='" + nameGeneric + '\'' +
                ", photoGeneric='" + photoGeneric + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idGeneric);
        dest.writeString(nameGeneric);
        dest.writeString(photoGeneric);
    }
}
