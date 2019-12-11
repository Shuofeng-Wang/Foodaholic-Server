package com.jhuoose.foodaholic.models;

public class Notification {
    private int id;
    private int sendUserId;
    private int recieveUserId;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(int sendUserId) {
        this.sendUserId = sendUserId;
    }

    public int getRecieveUserId() {
        return recieveUserId;
    }

    public void setRecieveUserId(int recieveUserId) {
        this.recieveUserId = recieveUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
