package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails implements Parcelable {
  @Expose private int id;
  @Expose private String name;
  @Expose private String category;

  @Expose
  @SerializedName("selling_cost")
  private int sellingCost;

  @Expose
  @SerializedName("purchese_cost")
  private int purchaseCost;

  @Expose
  @SerializedName("added_by")
  private int addedBy;

  @Expose private String detail;

  @Expose
  @SerializedName("other_image")
  private String otherImage;

  @Expose private String image;
  @Expose private String visit;

  @Expose
  @SerializedName("udate")
  private String update;

  @Expose private int status;
  @Expose private int shipping;
  @Expose private String language;

  @Expose
  @SerializedName("brand_name")
  private String brandName;

  @Expose private String city;

  @Expose
  @SerializedName("user_name")
  private String userName;

  @SerializedName("user_detail")
  private UserDetails userDetails;

  @Expose private String url;

  @Expose
  @SerializedName("product_type")
  private String productType;

  public ProductDetails() {}

  // <editor-fold desc="parcelable implementation">
  protected ProductDetails(Parcel in) {
    id = in.readInt();
    name = in.readString();
    category = in.readString();
    sellingCost = in.readInt();
    purchaseCost = in.readInt();
    addedBy = in.readInt();
    detail = in.readString();
    otherImage = in.readString();
    image = in.readString();
    visit = in.readString();
    update = in.readString();
    status = in.readInt();
    shipping = in.readInt();
    language = in.readString();
    brandName = in.readString();
    city = in.readString();
    userName = in.readString();
    userDetails = in.readParcelable(UserDetails.class.getClassLoader());
    url = in.readString();
    productType = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(category);
    dest.writeInt(sellingCost);
    dest.writeInt(purchaseCost);
    dest.writeInt(addedBy);
    dest.writeString(detail);
    dest.writeString(otherImage);
    dest.writeString(image);
    dest.writeString(visit);
    dest.writeString(update);
    dest.writeInt(status);
    dest.writeInt(shipping);
    dest.writeString(language);
    dest.writeString(brandName);
    dest.writeString(city);
    dest.writeString(userName);
    dest.writeParcelable(userDetails, flags);
    dest.writeString(url);
    dest.writeString(productType);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductDetails> CREATOR =
      new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
          return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
          return new ProductDetails[size];
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

  public int getSellingCost() {
    return sellingCost;
  }

  public int getPurchaseCost() {
    return purchaseCost;
  }

  public int getAddedBy() {
    return addedBy;
  }

  public String getDetail() {
    return detail;
  }

  public String getOtherImage() {
    return otherImage;
  }

  public String getImage() {
    return image;
  }

  public String getVisit() {
    return visit;
  }

  public String getUpdate() {
    return update;
  }

  public int getStatus() {
    return status;
  }

  public int getShipping() {
    return shipping;
  }

  public String getLanguage() {
    return language;
  }

  public String getBrandName() {
    return brandName;
  }

  public String getCity() {
    return city;
  }

  public String getUserName() {
    return userName;
  }

  public UserDetails getUserDetails() {
    return userDetails;
  }

  public String getUrl() {
    return url;
  }

  public String getProductType() {
    return productType;
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

  public void setSellingCost(int sellingCost) {
    this.sellingCost = sellingCost;
  }

  public void setPurchaseCost(int purchaseCost) {
    this.purchaseCost = purchaseCost;
  }

  public void setAddedBy(int addedBy) {
    this.addedBy = addedBy;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public void setOtherImage(String otherImage) {
    this.otherImage = otherImage;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setVisit(String visit) {
    this.visit = visit;
  }

  public void setUpdate(String update) {
    this.update = update;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setShipping(int shipping) {
    this.shipping = shipping;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUserDetails(UserDetails userDetails) {
    this.userDetails = userDetails;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }
  // </editor-fold>
}
