package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.User;

public class UserProfileView {
    public int id;
    public String email;
    public String userName;
    public String photoURL;
    public String phone;

    public UserProfileView(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.photoURL = user.getPhotoURL();
        this.phone = user.getPhone();
    }
}
