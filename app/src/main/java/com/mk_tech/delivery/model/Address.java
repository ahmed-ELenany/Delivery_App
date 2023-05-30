package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("city_id")
    @Expose
    public int city_id;

    @SerializedName("area_id")
    @Expose
    public int area_id;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("area")
    @Expose
    public String area;

    @SerializedName("name")
    @Expose
    public String name;


    @SerializedName("mobile")
    @Expose
    public String phone;


    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("block")
    @Expose
    public String block;

    @SerializedName("street")
    @Expose
    public String street;

    @SerializedName("building")
    @Expose
    public String building;


    @SerializedName("apartment_no")
    @Expose
    public String apartment_no;

    @SerializedName("avenue")
    @Expose
    public String avenue;


    @SerializedName("latitude")
    @Expose
    public String latitude;


    @SerializedName("longitude")
    @Expose
    public String longitude;


    public int isGlobal;

    public int getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(int isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getApartment_no() {
        return apartment_no;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAvenue() {
        return avenue;
    }

    public void setAvenue(String avenue) {
        this.avenue = avenue;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea_id() {
        return area_id;
    }

    public int getCity_id() {
        return city_id;
    }
}
