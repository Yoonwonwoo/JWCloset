package com.example.jwcloset.Items;

import java.io.Serializable;

public class ChatlistItem implements Serializable {

    String uid;
    String name;
    String lastMsg;

    public ChatlistItem(String uid, String name, String lastMsg) {
        this.uid = uid;
        this.name = name;
        this.lastMsg = lastMsg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
