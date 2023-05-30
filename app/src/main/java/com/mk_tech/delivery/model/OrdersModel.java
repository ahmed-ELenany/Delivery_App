package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<DataModel> data;

    @SerializedName("paginate")
    @Expose
    private Paginate paginate;

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        paginate = paginate;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<DataModel> getData() {
        return data;
    }

    public class DataModel {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("delivery")
        @Expose
        public String delivery;

        @SerializedName("discount")
        @Expose
        public String discount;


        @SerializedName("total")
        @Expose
        public String total;


        @SerializedName("payment_method")
        @Expose
        public String payment_method;

        @SerializedName("status")
        @Expose
        public String status;

        @SerializedName("currency")
        @Expose
        public String currency;


        public Integer getId() {
            return id;
        }

        public String getCurrency() {
            return currency;
        }


        public String getTotal() {
            return total;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public String getStatus() {
            return status;
        }

        public String getDiscount() {
            return discount;
        }

        public String getDelivery() {
            return delivery;
        }
    }


}
