package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentUser implements Parcelable {

  @Expose private int id;
  @Expose private String username;
  @Expose private String email;

  @Expose
  @SerializedName("aType")
  private String type;

  @Expose
  @SerializedName("fname")
  private String firstName;

  @Expose
  @SerializedName("lname")
  private String lastName;

  @Expose private String mobile;
  @Expose private String address;
  @Expose private String avatar;

  @Expose
  @SerializedName("banner_image")
  private String bannerImage;

  @Expose
  @SerializedName("profile_image_gallery")
  private String profileImageGallery;

  @Expose
  @SerializedName("img_banner_image")
  private String imgBannerImage;

  @Expose
  @SerializedName("profile_video_gallery")
  private String profileVideoGallery;

  @Expose
  @SerializedName("video_banner_image")
  private String videoBannerImage;

  @Expose
  @SerializedName("about_me")
  private String aboutMe;

  @Expose private String category;

  @Expose
  @SerializedName("tag_line")
  private String tagLine;


  public CurrentUser() {
  }

  //<editor-fold desc="Parcelable Implementation">
  protected CurrentUser(Parcel in) {
    id = in.readInt();
    username = in.readString();
    email = in.readString();
    type = in.readString();
    firstName = in.readString();
    lastName = in.readString();
    mobile = in.readString();
    address = in.readString();
    avatar = in.readString();
    bannerImage = in.readString();
    profileImageGallery = in.readString();
    imgBannerImage = in.readString();
    profileVideoGallery = in.readString();
    videoBannerImage = in.readString();
    aboutMe = in.readString();
    category = in.readString();
    tagLine = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(username);
    dest.writeString(email);
    dest.writeString(type);
    dest.writeString(firstName);
    dest.writeString(lastName);
    dest.writeString(mobile);
    dest.writeString(address);
    dest.writeString(avatar);
    dest.writeString(bannerImage);
    dest.writeString(profileImageGallery);
    dest.writeString(imgBannerImage);
    dest.writeString(profileVideoGallery);
    dest.writeString(videoBannerImage);
    dest.writeString(aboutMe);
    dest.writeString(category);
    dest.writeString(tagLine);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CurrentUser> CREATOR = new Creator<CurrentUser>() {
    @Override
    public CurrentUser createFromParcel(Parcel in) {
      return new CurrentUser(in);
    }

    @Override
    public CurrentUser[] newArray(int size) {
      return new CurrentUser[size];
    }
  };
  //</editor-fold>

  //<editor-fold desc="Getters">
  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getType() {
    return type;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getMobile() {
    return mobile;
  }

  public String getAddress() {
    return address;
  }

  public String getAvatar() {
    return avatar;
  }

  public String getBannerImage() {
    return bannerImage;
  }

  public String getProfileImageGallery() {
    return profileImageGallery;
  }

  public String getImgBannerImage() {
    return imgBannerImage;
  }

  public String getProfileVideoGallery() {
    return profileVideoGallery;
  }

  public String getVideoBannerImage() {
    return videoBannerImage;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public String getCategory() {
    return category;
  }

  public String getTagLine() {
    return tagLine;
  }
  //</editor-fold>


  //<editor-fold desc="Setters">
  public void setId(int id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }

  public void setProfileImageGallery(String profileImageGallery) {
    this.profileImageGallery = profileImageGallery;
  }

  public void setImgBannerImage(String imgBannerImage) {
    this.imgBannerImage = imgBannerImage;
  }

  public void setProfileVideoGallery(String profileVideoGallery) {
    this.profileVideoGallery = profileVideoGallery;
  }

  public void setVideoBannerImage(String videoBannerImage) {
    this.videoBannerImage = videoBannerImage;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setTagLine(String tagLine) {
    this.tagLine = tagLine;
  }
  //</editor-fold>
}
