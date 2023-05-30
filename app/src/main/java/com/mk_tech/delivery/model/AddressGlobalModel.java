package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressGlobalModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<DataModel> data;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<DataModel> getData() {
        return data;
    }

    public class DataModel {
        @SerializedName("addressess")
        @Expose
        List<Address> addressess;
        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public List<Address> getAddressess() {
            return addressess;
        }
    }

    public class Address {
        @SerializedName("id")
        @Expose
        public int id;


        @SerializedName("address")
        @Expose
        public String address;

        @SerializedName("city")
        @Expose
        public String city;

        @SerializedName("area")
        @Expose
        public String area;


        @SerializedName("block")
        @Expose
        public String block;

        @SerializedName("street")
        @Expose
        public String street;

        public int getId() {
            return id;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        public String getBlock() {
            return block;
        }

        public String getArea() {
            return area;
        }

        public String getAddress() {
            return address;
        }
    }


}
