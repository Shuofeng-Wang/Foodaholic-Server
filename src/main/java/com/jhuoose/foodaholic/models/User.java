package com.jhuoose.foodaholic.models;

import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String password;
    private String userName;
    private String photoURL;
    private String phone;
    private ArrayList<Integer> friendIdList = new ArrayList<>();
    private ArrayList<Integer> participatingEventIdList = new ArrayList<>();
    private ArrayList<Integer> notificationIdList = new ArrayList<>();

    public User(int id, String email, String password, String userName, String photoURL, String phone,
                ArrayList<Integer> friendIdList, ArrayList<Integer> participatingEventIdList, ArrayList<Integer> notificationIdList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.photoURL = photoURL;
        this.phone = phone;
        this.friendIdList = friendIdList;
        this.participatingEventIdList = participatingEventIdList;
        this.notificationIdList = notificationIdList;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<Integer> getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(ArrayList<Integer> friendsId) {
        this.friendIdList = friendsId;
    }

    public void addFriend(int friendId) {
        if (!this.friendIdList.contains(friendId)) this.friendIdList.add(friendId);
    }

    public boolean isFriend(int friendId) {
        return this.friendIdList.contains(friendId);
    }

    public boolean deleteFriend(int friendId) {
        return this.friendIdList.remove(Integer.valueOf(friendId));
    }

    public ArrayList<Integer> getParticipatingEventIdList() {
        return participatingEventIdList;
    }

    public void setParticipatingEventIdList(ArrayList<Integer> participatingEvents) {
        this.participatingEventIdList = participatingEvents;
    }

    public void addParticipatingEvent(int eventId) {
        if (!this.participatingEventIdList.contains(eventId)) this.participatingEventIdList.add(eventId);
    }

    public boolean isParticipatingEvent(int eventId) {
        return this.participatingEventIdList.contains(eventId);
    }

    public boolean deleteParticipatingEvent(int eventId) {
        return this.participatingEventIdList.remove(Integer.valueOf(eventId));
    }

    public ArrayList<Integer> getNotificationIdList() {
        return notificationIdList;
    }

    public void setNotificationIdList(ArrayList<Integer> notifications) {
        this.notificationIdList = notifications;
    }

    public void addNotification(int notificationId) {
        if (!this.notificationIdList.contains(notificationId)) this.notificationIdList.add(notificationId);
    }

    public boolean isNotification(int notificationId) {
        return this.notificationIdList.contains(notificationId);
    }

    public boolean deleteNotification(int notificationId) {
        return this.notificationIdList.remove(Integer.valueOf(notificationId));
    }
}
