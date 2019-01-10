package com.mssinfotech.iampro.co.models;

import java.util.List;

public class LatestProductsItemModel {

    private String name;
    private String category;
    private String sellingCost;
    private String purchaseCost;
    private String detail;
    private List<String> otherImages;
    private String imageUrl;

    public LatestProductsItemModel() {
    }

    public LatestProductsItemModel(
            String name,
            String category,
            String sellingCost,
            String purchaseCost,
            String detail,
            List<String> otherImages,
            String imageUrl
    ) {
        this.name = name;
        this.category = category;
        this.sellingCost = sellingCost;
        this.purchaseCost = purchaseCost;
        this.detail = detail;
        this.otherImages = otherImages;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSellingCost() {
        return sellingCost;
    }

    public void setSellingCost(String sellingCost) {
        this.sellingCost = sellingCost;
    }

    public String getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(String purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<String> otherImages) {
        this.otherImages = otherImages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
