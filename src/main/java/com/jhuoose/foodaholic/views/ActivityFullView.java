package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.Activity;

import java.util.ArrayList;

public class ActivityFullView {
    private int id;
    private String activityName;
    private String description;
    private int vote;
    private float money;
    private String category;
    private ArrayList<UserProfileView> participantList = new ArrayList<>();

    public ActivityFullView(Activity activity,
                            ArrayList<UserProfileView> participantList) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.description = activity.getDescription();
        this.vote = activity.getVote();
        this.money = activity.getMoney();
        this.category = activity.getCategory();
        this.participantList = participantList;
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

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
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
}
