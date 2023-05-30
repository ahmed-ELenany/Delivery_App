package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private String data;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public class DataModel {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("user_id")
        @Expose
        public Integer user_id;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("delivery_price")
        @Expose
        public Integer delivery_price;

        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }

        public Integer getDelivery_price() {
            return delivery_price;
        }
    }


}
