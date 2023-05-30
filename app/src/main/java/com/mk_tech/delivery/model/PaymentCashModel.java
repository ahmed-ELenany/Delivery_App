package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentCashModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


}
