package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumDetails implements Parcelable {
    private int id;
    private String name;
    private int addedBy;
    private int status;
    private String type;

    public AlbumDetails() {
    }

    public AlbumDetails(int id, String name, int addedBy, int status, String type) {
        this.id = id;
        this.name = name;
        this.addedBy = addedBy;
        this.status = status;
        this.type = type;
    }

    //<editor-fold desc="parcelable Implementation">
    protected AlbumDetails(Parcel in) {
        id = in.readInt();
        name = in.readString();
        addedBy = in.readInt();
        status = in.readInt();
        type = in.readString();
    }

    public static final Creator<AlbumDetails> CREATOR = new Creator<AlbumDetails>() {
        @Override
        public AlbumDetails createFromParcel(Parcel in) {
            return new AlbumDetails(in);
        }

        @Override
        public AlbumDetails[] newArray(int size) {
            return new AlbumDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(addedBy);
        dest.writeInt(status);
        dest.writeString(type);
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
    //</editor-fold>
}
