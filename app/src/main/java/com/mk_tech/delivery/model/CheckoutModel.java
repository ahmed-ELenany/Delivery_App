package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckoutModel {

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

        @SerializedName("carts")
        @Expose
        public List<CartModel.DataModel> carts;

        @SerializedName("grand_total")
        @Expose
        public Grand_total grand_total;


        @SerializedName("shippings")
        @Expose
        public List<Shipping> shippings;

        public Grand_total getGrand_total() {
            return grand_total;
        }

        public List<CartModel.DataModel> getCarts() {
            return carts;
        }

        public List<Shipping> getShippings() {
            return shippings;
        }
    }

    public class Grand_total {
        Double total;
        String currency;

        @SerializedName("total")
        public Double getTotal() {
            return this.total;
        }

        @SerializedName("currency")
        public String getCurrency() {
            return this.currency;
        }

    }

    public class Shipping {
        Double price;
        String company;
        String currency;

        @SerializedName("price")
        public Double getPrice() {
            return this.price;
        }

        @SerializedName("currency")
        public String getCurrency() {
            return this.currency;
        }

        @SerializedName("company")
        public String getCompany() {
            return this.company;
        }

    }
}
