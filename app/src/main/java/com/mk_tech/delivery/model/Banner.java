package com.mk_tech.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Banner {

    int id;
    String model;
    int model_id;
    ArrayList<Image> images;

    @SerializedName("id")
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("model_id")
    public int getModel_id() {
        return this.model_id;
    }
    public void setModel_id(int model_id) {
        this.id = model_id;
    }

    @SerializedName("model")
    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @SerializedName("images")
    public ArrayList<Image> getImages() {
        return this.images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public class Image {
        String url;
        String mime;

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
    }
}
