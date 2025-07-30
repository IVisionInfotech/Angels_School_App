package com.ivision.model;

import io.realm.RealmObject;

public class Image extends RealmObject {

    private String image;

    public Image() {
    }

    public Image(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
