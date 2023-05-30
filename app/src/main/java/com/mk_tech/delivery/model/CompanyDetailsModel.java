package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyDetailsModel {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DataCompanyDetails data;
    @SerializedName("paginate")
    @Expose
    private Paginate paginate;

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public DataCompanyDetails getData() {
        return data;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        paginate = paginate;
    }

    public class DataCompanyDetails {
        @SerializedName("company")
        @Expose
        private Company company;


        @SerializedName("brand")
        @Expose
        private BrandDetails brand;

        @SerializedName("sliders")
        @Expose
        private List<SliderModel> sliders;

        @SerializedName("products")
        @Expose
        private List<Product> products;

        public Company getCompany() {
            return company;
        }

        public BrandDetails getBrand() {
            return brand;
        }

        public List<Product> getProducts() {
            return products;
        }

        public List<SliderModel> getSliders() {
            return sliders;
        }
    }

    public class BrandDetails {

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("image")
        @Expose
        private Banner.Image image;

        public String getName() {
            return name;
        }

        public Banner.Image getImage() {
            return image;
        }
    }
}
