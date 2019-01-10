package com.mssinfotech.iampro.co.models;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TopSliderImageModel implements Parcelable {
    private int id;
    private String heading;
    @SerializedName("slider_type")
    private String sliderType;
    private String detail;
    private String link;
    private String image;
    @SerializedName("slider_image")
    private String sliderImage;
    private int status;
    private String language;
    private int no;
    private int index;
    private int mindex;

    public TopSliderImageModel() {
    }

    public TopSliderImageModel(int id, String heading, String sliderType,
                               String detail, String link, String image, String sliderImage,
                               int status, String language, int no, int index, int mindex) {
        this.id = id;
        this.heading = heading;
        this.sliderType = sliderType;
        this.detail = detail;
        this.link = link;
        this.image = image;
        this.sliderImage = sliderImage;
        this.status = status;
        this.language = language;
        this.no = no;
        this.index = index;
        this.mindex = mindex;
    }

    //<editor-fold desc="Parcelable Implementation">
    protected TopSliderImageModel(Parcel in) {
        id = in.readInt();
        heading = in.readString();
        sliderType = in.readString();
        detail = in.readString();
        link = in.readString();
        image = in.readString();
        sliderImage = in.readString();
        status = in.readInt();
        language = in.readString();
        no = in.readInt();
        index = in.readInt();
        mindex = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(heading);
        dest.writeString(sliderType);
        dest.writeString(detail);
        dest.writeString(link);
        dest.writeString(image);
        dest.writeString(sliderImage);
        dest.writeInt(status);
        dest.writeString(language);
        dest.writeInt(no);
        dest.writeInt(index);
        dest.writeInt(mindex);
    }

    public static final Creator<TopSliderImageModel> CREATOR = new Creator<TopSliderImageModel>() {
        @Override
        public TopSliderImageModel createFromParcel(Parcel in) {
            return new TopSliderImageModel(in);
        }

        @Override
        public TopSliderImageModel[] newArray(int size) {
            return new TopSliderImageModel[size];
        }
    };
    //</editor-fold>

    //<editor-fold desc="getters">
    public int getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getSliderType() {
        return sliderType;
    }

    public String getDetail() {
        return detail;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public int getStatus() {
        return status;
    }

    public String getLanguage() {
        return language;
    }

    public int getNo() {
        return no;
    }

    public int getIndex() {
        return index;
    }

    public int getMindex() {
        return mindex;
    }
    //</editor-fold>


    //<editor-fold desc="setters">
    public void setId(int id) {
        this.id = id;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setSliderType(String sliderType) {
        this.sliderType = sliderType;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMindex(int mindex) {
        this.mindex = mindex;
    }
    //</editor-fold>
}
