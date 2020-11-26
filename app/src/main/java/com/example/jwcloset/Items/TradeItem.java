package com.example.jwcloset.Items;

import java.io.Serializable;

public class TradeItem implements Serializable {
    String uid;
    String url;
    String date;
    String title;
    String description;
    String category;
    String name;

    public TradeItem(String uid, String url, String date, String title, String description, String category, String name) {
        this.uid = uid;
        this.url = url;
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.name = name;
    }

    public TradeItem(String date, String title, String description, String category, String name) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
