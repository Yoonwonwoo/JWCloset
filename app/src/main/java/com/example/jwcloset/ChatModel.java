package com.example.jwcloset;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {

public Map<String, String> users = new HashMap<>();

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    public Map<String, Comment> getComments() {
        return comments;
    }

    public void setComments(Map<String, Comment> comments) {
        this.comments = comments;
    }

    public Map<String, Comment> comments = new HashMap<>();
public Map<String, String> msg = new HashMap<>();

public static class Comment{
    String uid;
    public String message;
}
}


