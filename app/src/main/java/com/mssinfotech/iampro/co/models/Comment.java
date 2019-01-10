package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {

  @Expose
  private int id;
  @Expose
  @SerializedName("pid")
  private int productId;
  @Expose
  private String comment;
  @Expose
  private String date;
  @Expose
  @SerializedName("added_by")
  private int addedById;
  @Expose
  private int status;
  @Expose
  private String detail;
  @Expose
  @SerializedName("fullname")
  private String fullName;
  @Expose
  private String email;
  @Expose
  @SerializedName("user_img")
  private String userImage;
  @Expose
  @SerializedName("rdate")
  private String ago;

  public Comment() {
  }


  //<editor-fold desc="Parcelable Implementation">
  protected Comment(Parcel in) {
    id = in.readInt();
    productId = in.readInt();
    comment = in.readString();
    date = in.readString();
    addedById = in.readInt();
    status = in.readInt();
    detail = in.readString();
    fullName = in.readString();
    email = in.readString();
    userImage = in.readString();
    ago = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(productId);
    dest.writeString(comment);
    dest.writeString(date);
    dest.writeInt(addedById);
    dest.writeInt(status);
    dest.writeString(detail);
    dest.writeString(fullName);
    dest.writeString(email);
    dest.writeString(userImage);
    dest.writeString(ago);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Comment> CREATOR = new Creator<Comment>() {
    @Override
    public Comment createFromParcel(Parcel in) {
      return new Comment(in);
    }

    @Override
    public Comment[] newArray(int size) {
      return new Comment[size];
    }
  };
  //</editor-fold>

  //<editor-fold desc="Getters">
  public int getId() {
    return id;
  }

  public int getProductId() {
    return productId;
  }

  public String getComment() {
    return comment;
  }

  public String getDate() {
    return date;
  }

  public int getAddedById() {
    return addedById;
  }

  public int getStatus() {
    return status;
  }

  public String getDetail() {
    return detail;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getUserImage() {
    return userImage;
  }

  public String getAgo() {
    return ago;
  }
  //</editor-fold>

  //<editor-fold desc="Setters">
  public void setId(int id) {
    this.id = id;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setAddedById(int addedById) {
    this.addedById = addedById;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setUserImage(String userImage) {
    this.userImage = userImage;
  }

  public void setAgo(String ago) {
    this.ago = ago;
  }
  //</editor-fold>
}
