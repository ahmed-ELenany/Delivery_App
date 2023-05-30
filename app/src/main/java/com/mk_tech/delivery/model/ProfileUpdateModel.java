package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateModel {


    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private RegisterModel.UserModel data;


    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }


    public RegisterModel.UserModel getData() {
        return data;
    }


}
