package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyPackagesModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;


    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("company")
        @Expose
        private Company company;

        @SerializedName("packages")
        @Expose
        private List<Package> packages;

        public Company getCompany() {
            return company;
        }

        public List<Package> getPackages() {
            return packages;
        }
    }


}
