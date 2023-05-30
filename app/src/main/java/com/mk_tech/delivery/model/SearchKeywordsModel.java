package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchKeywordsModel {

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

        @SerializedName("id")
        @Expose
        public int id;


        @SerializedName("name_ar")
        @Expose
        public String name_ar;

        @SerializedName("name_en")
        @Expose
        public String name_en;

        @SerializedName("name")
        @Expose
        public String name;

        public String getName() {
            return name;
        }

        public String getName_en() {
            return name_en;
        }

        public String getName_ar() {
            return name_ar;
        }

        public int getId() {
            return id;
        }
    }


}
