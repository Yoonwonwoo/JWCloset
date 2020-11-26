package com.example.jwcloset.Items;

import java.io.Serializable;

public class MarketItem implements Serializable {
    String image;
    String name;
    String category;
    String uri;

    public MarketItem(String image, String name, String category, String uri) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
