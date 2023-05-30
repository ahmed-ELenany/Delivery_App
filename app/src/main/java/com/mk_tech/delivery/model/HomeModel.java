package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class HomeModel {
    boolean success;
    String message;
    Data data;

    @SerializedName("success")
    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @SerializedName("message")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("data")
    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        ArrayList<SliderModel> sliders;
        ArrayList<Product> products_best_sellers;

        public ArrayList<SliderModel> getSliders() {
            return sliders;
        }

        public ArrayList<Product> getProducts_best_sellers() {
            return products_best_sellers;
        }
    }


}


