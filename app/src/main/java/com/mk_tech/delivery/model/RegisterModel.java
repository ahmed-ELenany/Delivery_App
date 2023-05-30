package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterModel {


    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DataModel data;


    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }


    public DataModel getData() {
        return data;
    }

    public class DataModel {

        @SerializedName("token")
        @Expose
        public String token;

        @SerializedName("user")
        @Expose
        public UserModel userModel;

        public String getToken() {
            return token;
        }

        public UserModel getUserModel() {
            return userModel;
        }

    }

    public class UserModel {
        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("name")
        @Expose
        public String name;


        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("mobile")
        @Expose
        public String mobile;

        @SerializedName("profile")
        @Expose
        public String profile;

        @SerializedName("loyality_points")
        @Expose
        public int loyality_points;

        public String getProfile() {
            return profile;
        }

        public int getId() {
            return id;
        }

        public int getLoyality_points() {
            return loyality_points;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }


    }


}
