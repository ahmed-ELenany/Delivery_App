package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityModel {

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

    public class DataModel {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("name")
        @Expose
        public String name;


        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }

    }


}
