package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.Activity;

public class ActivityProfileView {
    private int id;
    private String activityName;
    private String description;
    private int vote;
    private float money;
    private String category;

    public ActivityProfileView(Activity activity) {
        this.id = activity.getId();
        this.activityName = activity.getActivityName();
        this.description = activity.getDescription();
        this.vote = activity.getVote();
        this.money = activity.getMoney();
        this.category = activity.getCategory();
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
}
