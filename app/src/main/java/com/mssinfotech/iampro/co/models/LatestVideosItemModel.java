package com.mssinfotech.iampro.co.models;

public class LatestVideosItemModel {
    private String videoUrl;
    private String userDisplayName;
    private String userAvatarUrl;

    public LatestVideosItemModel() {
    }

    public LatestVideosItemModel(String videoUrl, String userDisplayName, String userAvatarUrl) {
        this.videoUrl = videoUrl;
        this.userDisplayName = userDisplayName;
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
}
