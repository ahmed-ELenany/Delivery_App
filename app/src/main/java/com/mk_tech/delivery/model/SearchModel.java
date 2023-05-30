package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchModel<T> {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<T> data;
    @SerializedName("paginate")
    @Expose
    private Paginate paginate;

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        paginate = paginate;
    }

}
