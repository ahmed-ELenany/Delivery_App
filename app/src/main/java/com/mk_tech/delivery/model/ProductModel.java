package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel {


    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Product data;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Product getData() {
        return data;
    }


}
