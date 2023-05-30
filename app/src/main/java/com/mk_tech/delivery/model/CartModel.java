package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<DataModel> data;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<DataModel> getData() {
        return data;
    }

    public static class CartProduct {
        int id;
        int product_id;
        String image;
        String name;
        String slug;
        String price;
        int quantity;
        int stock;
        String total;
        String currency;
        List<CartProductAddition> additions;
        String description;

        @SerializedName("id")
        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @SerializedName("stock")
        public int getStock() {
            return this.stock;
        }

        @SerializedName("image")
        public String getImage() {
            return this.image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @SerializedName("description")
        public String getDescription() {
            return this.description;
        }


        @SerializedName("slug")
        public String getSlug() {
            return this.slug;
        }


        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
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

        @SerializedName("product_id")
        public int getProduct_id() {
            return this.product_id;
        }


        @SerializedName("additions")
        public List<CartProductAddition> getAdditions() {
            return this.additions;
        }

        public void setAdditions(List<CartProductAddition> additions) {
            this.additions = additions;
        }
    }

    public class DataModel {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("products_count")
        @Expose
        public Integer products_count;

        @SerializedName("total")
        @Expose
        public String total;

        @SerializedName("currency")
        @Expose
        public String currency;

        @SerializedName("products")
        @Expose
        public List<CartProduct> products;


        @SerializedName("slug")
        @Expose
        String slug;

        public String getSlug() {
            return this.slug;
        }

        public String getTotal() {
            return total;
        }

        @SerializedName("discount")
        String discount;
        public String getDiscount() {
            return this.discount;
        }

        public Integer getId() {
            return id;
        }

        public String getCurrency() {
            return currency;
        }

        public List<CartProduct> getProducts() {
            return products;
        }

        public Integer getProducts_count() {
            return products_count;
        }

     }

    public class CartProductAddition {
        int id;
        String name;
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


        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
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

    public class DeliveryTime {
        String day;
        String hour;

        @SerializedName("day")
        public String getDay() {
            return this.day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        @SerializedName("hour")
        public String getHour() {
            return this.hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }
    }

}
