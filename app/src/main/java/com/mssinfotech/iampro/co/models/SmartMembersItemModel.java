package com.mssinfotech.iampro.co.models;

public class SmartMembersItemModel {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String avatarUrl;

    public SmartMembersItemModel() {
    }

    public SmartMembersItemModel(String fullName, String phoneNumber, String email, String avatarUrl) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
