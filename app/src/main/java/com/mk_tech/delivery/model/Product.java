package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    int id;
    String name;
    String description;
    String full_description;
    String old_price;
    String new_price;
    String image;
    String company;
    String end_at;
    int stock;
    List<Banner.Image> images;
    double discount_percentage;
    boolean is_favoriet;

    @SerializedName("id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("stock")
    public int getStock() {
        return this.stock;
    }

    @SerializedName("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("end_at")
    public String getEndAt() {
        return this.end_at;
    }


    @SerializedName("full_description")
    public String getFullDescription() {
        return this.full_description;
    }

    public void setFullDescription(String full_description) {
        this.full_description = full_description;
    }

    @SerializedName("old_price")
    public String getOld_price() {
        return this.old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    @SerializedName("new_price")
    public String getNew_price() {
        return this.new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    @SerializedName("image")
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("company")
    public String getCompany() {
        return this.company;
    }


    @SerializedName("images")
    public List<Banner.Image> getImages() {
        return this.images;
    }

    public void setImages(List<Banner.Image> images) {
        this.images = images;
    }

    @SerializedName("discount_percentage")
    public double getDiscount_percentage() {
        return this.discount_percentage;
    }

    public void setDiscount_percentage(double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    @SerializedName("is_favoriet")
    public boolean getIs_favoriet() {
        return this.is_favoriet;
    }

    public void setIs_favoriet(boolean is_favoriet) {
        this.is_favoriet = is_favoriet;
    }
}
