package com.mk_tech.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("day")
    @Expose
    public String day;

    @SerializedName("start")
    @Expose
    public String start;

    @SerializedName("end")
    @Expose
    public String end;

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }
}
