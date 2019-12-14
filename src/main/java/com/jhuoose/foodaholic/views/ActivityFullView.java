package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.Activity;

import java.util.ArrayList;

public class ActivityFullView {
    private int id;
    private String activityName;
    private String description;
    private int vote;
    private double money;
    private String category;
    private EventProfileView event;
    private ArrayList<UserProfileView> voteList = new ArrayList<>();
    private ArrayList<UserProfileView> booList = new ArrayList<>();
    private ArrayList<UserProfileView> participantList = new ArrayList<>();
    private UserProfileView payer;

    public ActivityFullView() {
    }

    public ActivityFullView(Activity activity,
                            EventProfileView event,
                            ArrayList<UserProfileView> voteList,
                            ArrayList<UserProfileView> booList,
                            ArrayList<UserProfileView> participantList,
                            UserProfileView payer) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.description = activity.getDescription();
        this.vote = activity.getVote();
        this.money = activity.getMoney();
        this.category = activity.getCategory();
        this.event = event;
        this.voteList = voteList;
        this.booList = booList;
        this.participantList = participantList;
        this.payer = payer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<UserProfileView> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ArrayList<UserProfileView> participantList) {
        this.participantList = participantList;
    }

    public EventProfileView getEvent() {
        return event;
    }

    public void setEvent(EventProfileView event) {
        this.event = event;
    }

    public ArrayList<UserProfileView> getVoteList() {
        return voteList;
    }

    public void setVoteList(ArrayList<UserProfileView> voteList) {
        this.voteList = voteList;
    }

    public ArrayList<UserProfileView> getBooList() {
        return booList;
    }

    public void setBooList(ArrayList<UserProfileView> booList) {
        this.booList = booList;
    }

    public UserProfileView getPayer() {
        return payer;
    }

    public void setPayer(UserProfileView payer) {
        this.payer = payer;
    }
}
