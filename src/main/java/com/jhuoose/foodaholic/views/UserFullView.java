package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.Notification;
import com.jhuoose.foodaholic.models.User;

import java.util.ArrayList;

public class UserFullView {
    private int id;
    private String email;
    private String userName;
    private String photoURL;
    private String phone;
    private ArrayList<UserProfileView> friendList = new ArrayList<>();
    private ArrayList<EventProfileView> participatingEventList = new ArrayList<>();
    private ArrayList<Notification> notificationList = new ArrayList<>();

    public UserFullView(User user,
                        ArrayList<UserProfileView> friendList,
                        ArrayList<EventProfileView> participatingEventList,
                        ArrayList<Notification> notificationList) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.photoURL = user.getPhotoURL();
        this.phone = user.getPhone();
        this.friendList = friendList;
        this.participatingEventList = participatingEventList;
        this.notificationList = notificationList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<UserProfileView> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<UserProfileView> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<EventProfileView> getParticipatingEventList() {
        return participatingEventList;
    }

    public void setParticipatingEventList(ArrayList<EventProfileView> participatingEventList) {
        this.participatingEventList = participatingEventList;
    }

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }
}
