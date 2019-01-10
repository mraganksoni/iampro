package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ProvideAndDemandItemModel implements Parcelable {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    @SerializedName("product_type")
    private String productType;
    @Expose
    private String category;
    @Expose
    @SerializedName("selling_cost")
    private float sellingCost;
    @Expose
    @SerializedName("purchese_cost")
    private float purchaseCost;
    @Expose
    @SerializedName("other_image")
    private String otherImages;
    @Expose
    @SerializedName("added_by")
    private int      addedBy;
    @Expose
    private String detail;
    @Expose
    private String      visit;
    @Expose
    private String      update;
    @Expose
    @SerializedName("image")
    private String      imageName;
    @Expose
    private int      status;
    @Expose
    private String      language;
    @Expose
    @SerializedName("brand_name")
    private String      brandName;
    @Expose
    private String      city;
    // TODO: 03-06-2018 // Add Comments field, but no data provided by backend manager
    private UserDetails userDetail;
    @Expose
    @SerializedName("user_name")
    private String      userName;
    @Expose
    private String url;
    @Expose
    @SerializedName("user_url")
    private String userUrl;

    public ProvideAndDemandItemModel() {
    }

    //<editor-fold desc="parcelable implementation">
    protected ProvideAndDemandItemModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        productType = in.readString();
        category = in.readString();
        sellingCost = in.readFloat();
        purchaseCost = in.readFloat();
        otherImages = in.readString();
        addedBy = in.readInt();
        detail = in.readString();
        visit = in.readString();
        update = in.readString();
        imageName = in.readString();
        status = in.readInt();
        language = in.readString();
        brandName = in.readString();
        city = in.readString();
        userDetail = in.readParcelable(UserDetails.class.getClassLoader());
        userName = in.readString();
        url = in.readString();
        userUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(productType);
        dest.writeString(category);
        dest.writeFloat(sellingCost);
        dest.writeFloat(purchaseCost);
        dest.writeString(otherImages);
        dest.writeInt(addedBy);
        dest.writeString(detail);
        dest.writeString(visit);
        dest.writeString(update);
        dest.writeString(imageName);
        dest.writeInt(status);
        dest.writeString(language);
        dest.writeString(brandName);
        dest.writeString(city);
        dest.writeParcelable(userDetail, flags);
        dest.writeString(userName);
        dest.writeString(url);
        dest.writeString(userUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProvideAndDemandItemModel> CREATOR = new Creator<ProvideAndDemandItemModel>() {
        @Override
        public ProvideAndDemandItemModel createFromParcel(Parcel in) {
            return new ProvideAndDemandItemModel(in);
        }

        @Override
        public ProvideAndDemandItemModel[] newArray(int size) {
            return new ProvideAndDemandItemModel[size];
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

    public String getProductType() {
        return productType;
    }

    public String getCategory() {
        return category;
    }

    public float getSellingCost() {
        return sellingCost;
    }

    public float getPurchaseCost() {
        return purchaseCost;
    }

    public String getOtherImages() {
        return otherImages;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public String getDetail() {
        return detail;
    }

    public String getVisit() {
        return visit;
    }

    public String getUpdate() {
        return update;
    }

    public String getImageName() {
        return imageName;
    }

    public int getStatus() {
        return status;
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

    public UserDetails getUserDetail() {
        return userDetail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUrl() {
        return url;
    }

    public String getUserUrl() {
        return userUrl;
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSellingCost(float sellingCost) {
        this.sellingCost = sellingCost;
    }

    public void setPurchaseCost(float purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public void setOtherImages(String otherImages) {
        this.otherImages = otherImages;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setUserDetail(UserDetails userDetail) {
        this.userDetail = userDetail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
    //</editor-fold>
}
