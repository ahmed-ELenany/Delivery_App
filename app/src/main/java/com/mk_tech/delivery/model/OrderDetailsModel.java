package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsModel {

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

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("delivery")
        @Expose
        public Double delivery;

        @SerializedName("discount")
        @Expose
        public Double discount;


        @SerializedName("total")
        @Expose
        public Double total;


        @SerializedName("payment_method")
        @Expose
        public String payment_method;

        @SerializedName("status")
        @Expose
        public String status;

        @SerializedName("currency")
        @Expose
        public String currency;


        @SerializedName("companies")
        @Expose
        public List<Companies> companies;

        public String getStatus() {
            return status;
        }

        public Double getDelivery() {
            return delivery;
        }

        public Integer getId() {
            return id;
        }

        public List<Companies> getCompanies() {
            return companies;
        }

        public String getCurrency() {
            return currency;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public Double getTotal() {
            return total;
        }

        public Double getDiscount() {
            return discount;
        }
    }

    public class Companies {
        @SerializedName("products")
        @Expose
        public List<OrderSummaryProduct> products;

        public List<OrderSummaryProduct> getProducts() {
            return products;
        }
    }

    public class OrderSummaryProduct {
        int id;
        int product_id;
        String product_image;
        String product_name;
        String price;
        int quantity;
        String total;
        String currency;


        @SerializedName("id")
        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @SerializedName("product_image")
        public String getImage() {
            return this.product_image;
        }

        public void setImage(String image) {
            this.product_image = image;
        }

        @SerializedName("product_name")
        public String getName() {
            return this.product_name;
        }

        public void setName(String name) {
            this.product_name = name;
        }

        @SerializedName("price")
        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @SerializedName("quantity")
        public int getQuantity() {
            return this.quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @SerializedName("total")
        public String getTotal() {
            return this.total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        @SerializedName("currency")
        public String getCurrency() {
            return this.currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }


}
