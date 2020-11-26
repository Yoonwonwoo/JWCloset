package com.example.jwcloset.Items;

import java.io.Serializable;
import java.util.List;

public class PostItem implements Serializable {
    String Author;
    String Title;
    String Time;
    String Category;
    String Description;
    String uid;
    List Images;

    public PostItem(String author, String title, String time, String category, String description) {
        Author = author;
        Title = title;
        Time = time;
        Category = category;
        Description = description;
    }

    public PostItem(String author, String title, String time, String category, String description, List images, String uid) {
        Author = author;
        Title = title;
        Time = time;
        Category = category;
        Description = description;
        Images = images;
        this.uid = uid;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List getImages() {
        return Images;
    }

    public void setImages(List images) {
        Images = images;
    }
}

