package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VideoDetails implements Parcelable {
    private int id;
    private String name;
    private int category;
    @SerializedName("albem_type")
    private int albemType;
    @SerializedName("albemid")
    private int albumId;
    private int status;
    @SerializedName("image")
    private String videoName;
    @SerializedName("udate")
    private String update;
    @SerializedName("is_featured")
    private int isFeatured;
    @SerializedName("about_us")
    private String aboutUs;
    private String ago;
    @SerializedName("album_detail")
    private AlbumDetails albumDetails;
    @SerializedName("user_detail")
    private UserDetails userDetails;

    public VideoDetails() {
    }

    //<editor-fold desc="parcelable implementation">
    protected VideoDetails(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readInt();
        albemType = in.readInt();
        albumId = in.readInt();
        status = in.readInt();
        videoName = in.readString();
        update = in.readString();
        isFeatured = in.readInt();
        aboutUs = in.readString();
        ago = in.readString();
        albumDetails = in.readParcelable(AlbumDetails.class.getClassLoader());
        userDetails = in.readParcelable(UserDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(category);
        dest.writeInt(albemType);
        dest.writeInt(albumId);
        dest.writeInt(status);
        dest.writeString(videoName);
        dest.writeString(update);
        dest.writeInt(isFeatured);
        dest.writeString(aboutUs);
        dest.writeString(ago);
        dest.writeParcelable(albumDetails, flags);
        dest.writeParcelable(userDetails, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoDetails> CREATOR = new Creator<VideoDetails>() {
        @Override
        public VideoDetails createFromParcel(Parcel in) {
            return new VideoDetails(in);
        }

        @Override
        public VideoDetails[] newArray(int size) {
            return new VideoDetails[size];
        }
    };
    //</editor-fold>

    //<editor-fold desc="getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    public int getAlbemType() {
        return albemType;
    }

    public int getAlbumId() {
        return albumId;
    }

    public int getStatus() {
        return status;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getUpdate() {
        return update;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public String getAgo() {
        return ago;
    }

    public AlbumDetails getAlbumDetails() {
        return albumDetails;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setAlbemType(int albemType) {
        this.albemType = albemType;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public void setAgo(String ago) {
        this.ago = ago;
    }

    public void setAlbumDetails(AlbumDetails albumDetails) {
        this.albumDetails = albumDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    //</editor-fold>
}
