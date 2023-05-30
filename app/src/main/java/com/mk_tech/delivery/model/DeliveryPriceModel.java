package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryPriceModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DataModel data;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public DataModel getData() {
        return data;
    }

    public class DataModel {


        @SerializedName("totalShipping")
        @Expose
        public String totalShipping;

        public String getTotalShipping() {
            return totalShipping;
        }
    }


}
