package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDetails implements Parcelable {
  @Expose private int id;
  @Expose private String name = "";
  @Expose private String category;

  @Expose
  @SerializedName("albem_type")
  private int albumType;

  @Expose
  @SerializedName("albemid")
  private int albumId;

  @Expose private int status;

  @Expose
  @SerializedName("image")
  private String imageName;

  @Expose
  @SerializedName("udate")
  private String update;

  @Expose
  @SerializedName("is_featured")
  private int isFeatured;

  @Expose
  @SerializedName("about_us")
  private String aboutUs;

  @Expose private String ago;

  @Expose
  @SerializedName("album_detail")
  private AlbumDetails albumDetails;

  @SerializedName("user_detail")
  private UserDetails userDetail;

  @Expose
  @SerializedName("v_image")
  private String videoThumbnail;

  public ImageDetails() {}

  // <editor-fold desc="parcelable implementation">
  protected ImageDetails(Parcel in) {
    id = in.readInt();
    name = in.readString();
    category = in.readString();
    albumType = in.readInt();
    albumId = in.readInt();
    status = in.readInt();
    imageName = in.readString();
    update = in.readString();
    isFeatured = in.readInt();
    aboutUs = in.readString();
    ago = in.readString();
    albumDetails = in.readParcelable(AlbumDetails.class.getClassLoader());
    userDetail = in.readParcelable(UserDetails.class.getClassLoader());
    videoThumbnail = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(category);
    dest.writeInt(albumType);
    dest.writeInt(albumId);
    dest.writeInt(status);
    dest.writeString(imageName);
    dest.writeString(update);
    dest.writeInt(isFeatured);
    dest.writeString(aboutUs);
    dest.writeString(ago);
    dest.writeParcelable(albumDetails, flags);
    dest.writeParcelable(userDetail, flags);
    dest.writeString(videoThumbnail);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ImageDetails> CREATOR =
      new Creator<ImageDetails>() {
        @Override
        public ImageDetails createFromParcel(Parcel in) {
          return new ImageDetails(in);
        }

        @Override
        public ImageDetails[] newArray(int size) {
          return new ImageDetails[size];
        }
      };
  // </editor-fold>

  // <editor-fold desc="getters">
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public int getAlbumType() {
    return albumType;
  }

  public int getAlbumId() {
    return albumId;
  }

  public int getStatus() {
    return status;
  }

  public String getImageName() {
    return imageName;
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

  public UserDetails getUserDetail() {
    return userDetail;
  }

  public String getVideoThumbnail() {
    return videoThumbnail;
  }
  // </editor-fold>

  // <editor-fold desc="setters">
  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setAlbumType(int albumType) {
    this.albumType = albumType;
  }

  public void setAlbumId(int albumId) {
    this.albumId = albumId;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
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

  public void setUserDetail(UserDetails userDetail) {
    this.userDetail = userDetail;
  }

  public void setVideoThumbnail(String videoThumbnail) {
    this.videoThumbnail = videoThumbnail;
  }
  // </editor-fold>
}
