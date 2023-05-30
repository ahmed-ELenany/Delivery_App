package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

   /* @SerializedName("data")
    @Expose
    private  AddressListModel.AddressModel  data;*/

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    /*public AddressListModel.AddressModel getData() {
        return data;
    }*/
}
