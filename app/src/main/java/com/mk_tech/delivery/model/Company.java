package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

public class Company {

    int id;
    String name;
    String profile;
    int products_count;

    @SerializedName("id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @SerializedName("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @SerializedName("profile")
    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    @SerializedName("products_count")
    public int getProducts_count() {
        return this.products_count;
    }

    public void setProducts_count(int products_count) {
        this.products_count = products_count;
    }
}
