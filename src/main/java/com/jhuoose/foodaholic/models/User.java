package com.jhuoose.foodaholic.models;

import java.util.List;

public class User {
    private int id;
    private String email;
    private String password;
    private String name; //change to userName
    private String photoURL;
    private String phone;
    private List<Integer> friendsId;
    private List<Integer> participatingEventsId;
    private List<Integer> notificationsId;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
