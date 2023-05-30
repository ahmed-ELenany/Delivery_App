package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompaniesModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DataCompanies data;
    @SerializedName("paginate")
    @Expose
    private Paginate paginate;

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public DataCompanies getData() {
        return data;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        paginate = paginate;
    }

    public class DataCompanies {
        @SerializedName("sliders")
        @Expose
        private List<SliderModel> sliders;

        @SerializedName("companies")
        @Expose
        private List<Company> companies;

        public List<Company> getCompanies() {
            return companies;
        }

        public List<SliderModel> getSliders() {
            return sliders;
        }
    }
}
