package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

public class SliderModel {

    int id;
    String type;
    String title;
    String description;
    String url;
    String mime;
    String link;

    @SerializedName("id")
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("title")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("url")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("mime")
    public String getMime() {
        return this.mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    @SerializedName("link")
    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
