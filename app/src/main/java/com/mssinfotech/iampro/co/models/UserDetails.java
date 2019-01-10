package com.mssinfotech.iampro.co.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails implements Parcelable {
    public static final int ROLE_SUPER_ADMIN = 1;
    public static final int ROLE_ADMIN = 2;
    public static final int ROLE_USER = 3;

    @Expose
    private int id;
    @Expose
    private String username;
    @Expose
    @SerializedName("fname")
    private String firstName;
    @Expose
    @SerializedName("lname")
    private String lastName;
    @Expose
    @SerializedName("pass")
    private String password;
    @Expose
    private String role;
    @Expose
    private String status;
    @Expose
    private String avatar;
    @Expose
    @SerializedName("tag_line")
    private String tagLine;
    @Expose
    @SerializedName("identity_type")
    private String identityType;
    @Expose
    @SerializedName("identity_number")
    private String identityNumber;
    @Expose
    @SerializedName("banner_image")
    private String bannerImage;
    @Expose
    @SerializedName("img_banner_image")
    private String imgBannerImage;
    @Expose
    @SerializedName("video_banner_image")
    private String videoBannerImage;
    @Expose
    @SerializedName("profile_image_gallery")
    private String profileImageGallery;
    @Expose
    @SerializedName("profile_video_gallery")
    private String profileVideoGallery;
    @Expose
    private String mobile;
    @Expose
    private String address;
    @Expose
    @SerializedName("about_me")
    private String aboutMe;
    @Expose
    private String country;
    @Expose
    private String state;
    @Expose
    private String city;
    @Expose
    private String gender;
    @Expose
    private String vcode;
    @Expose
    @SerializedName("fullname")
    private String fullName;
    @Expose
    private String email;
    @Expose
    private String dob;
    @Expose
    private String zip;
    @Expose
    @SerializedName("udate")
    private String update;
    @Expose
    private String category;

    public UserDetails() {
    }

    //<editor-fold desc="parcelable implementation">
    protected UserDetails(Parcel in) {
        id = in.readInt();
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        password = in.readString();
        role = in.readString();
        status = in.readString();
        avatar = in.readString();
        tagLine = in.readString();
        identityType = in.readString();
        identityNumber = in.readString();
        bannerImage = in.readString();
        imgBannerImage = in.readString();
        videoBannerImage = in.readString();
        profileImageGallery = in.readString();
        profileVideoGallery = in.readString();
        mobile = in.readString();
        address = in.readString();
        aboutMe = in.readString();
        country = in.readString();
        state = in.readString();
        city = in.readString();
        gender = in.readString();
        vcode = in.readString();
        fullName = in.readString();
        email = in.readString();
        dob = in.readString();
        zip = in.readString();
        update = in.readString();
        category = in.readString();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(password);
        dest.writeString(role);
        dest.writeString(status);
        dest.writeString(avatar);
        dest.writeString(tagLine);
        dest.writeString(identityType);
        dest.writeString(identityNumber);
        dest.writeString(bannerImage);
        dest.writeString(imgBannerImage);
        dest.writeString(videoBannerImage);
        dest.writeString(profileImageGallery);
        dest.writeString(profileVideoGallery);
        dest.writeString(mobile);
        dest.writeString(address);
        dest.writeString(aboutMe);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(gender);
        dest.writeString(vcode);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(dob);
        dest.writeString(zip);
        dest.writeString(update);
        dest.writeString(category);
    }
    //</editor-fold>

    //<editor-fold desc="getters">
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getIdentityType() {
        return identityType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public String getImgBannerImage() {
        return imgBannerImage;
    }

    public String getVideoBannerImage() {
        return videoBannerImage;
    }

    public String getProfileImageGallery() {
        return profileImageGallery;
    }

    public String getProfileVideoGallery() {
        return profileVideoGallery;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public String getVcode() {
        return vcode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getZip() {
        return zip;
    }

    public String getUpdate() {
        return update;
    }

    public String getCategory() {
        return category;
    }
    //</editor-fold>

    //<editor-fold desc="setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public void setImgBannerImage(String imgBannerImage) {
        this.imgBannerImage = imgBannerImage;
    }

    public void setVideoBannerImage(String videoBannerImage) {
        this.videoBannerImage = videoBannerImage;
    }

    public void setProfileImageGallery(String profileImageGallery) {
        this.profileImageGallery = profileImageGallery;
    }

    public void setProfileVideoGallery(String profileVideoGallery) {
        this.profileVideoGallery = profileVideoGallery;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    //</editor-fold>
}
