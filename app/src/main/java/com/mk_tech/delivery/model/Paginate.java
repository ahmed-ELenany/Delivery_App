package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

public class Paginate {

    int current_page;
    boolean has_pages;
    String next_page_url;
    int per_page;

    @SerializedName("current_page")
    public int getCurrent_page() {
        return this.current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    @SerializedName("has_pages")
    public boolean getHas_pages() {
        return this.has_pages;
    }

    public void setHas_pages(boolean has_pages) {
        this.has_pages = has_pages;
    }

    @SerializedName("next_page_url")
    public String getNext_page_url() {
        return this.next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    @SerializedName("per_page")
    public int getPer_page() {
        return this.per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }
}
