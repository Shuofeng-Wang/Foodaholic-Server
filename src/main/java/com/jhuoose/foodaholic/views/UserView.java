package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserView {
    public int id;
    public String email;
    public String userName;
    public String photoURL;
    public String phone;
    public List<Integer> friendIdList = new ArrayList<>();
    public List<Integer> participatingEventIdList = new ArrayList<>();
    public List<Integer> notificationIdList = new ArrayList<>();

    public UserView(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.photoURL = user.getPhotoURL();
        this.phone = user.getPhone();
        this.friendIdList = user.getFriendIdList();
        this.participatingEventIdList = user.getParticipatingEventIdList();
        this.notificationIdList = user.getNotificationIdList();
    }
}
