package com.jhuoose.foodaholic.models;

import java.util.ArrayList;

public class Activity {
    private int id;
    private String activityName;
    private String description;
    private int vote;
    private float money;
    private String category;
    private ArrayList<Integer> participantIdList = new ArrayList<>();

    public Activity() {
    }

    public Activity(int id, String activityName, String description, int vote, float money, String category, ArrayList<Integer> participantIdList) {
        this.id = id;
        this.activityName = activityName;
        this.description = description;
        this.vote = vote;
        this.money = money;
        this.category = category;
        this.participantIdList = participantIdList;
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

    public ArrayList<Integer> getParticipantIdList() {
        return participantIdList;
    }

    public void setParticipantIdList(ArrayList<Integer> participantIdList) {
        this.participantIdList = participantIdList;
    }

    public void addParticipant(int participantId) {
        if (!this.participantIdList.contains(participantId)) this.participantIdList.add(participantId);
    }

    public boolean isParticipant(int participantId) {
        return this.participantIdList.contains(participantId);
    }

    public boolean deleteParticipant(int participantId) {
        return this.participantIdList.remove(Integer.valueOf(participantId));
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

    public void upVote() {
        this.vote = this.vote + 1;
    }

    public void downVote() {
        this.vote = this.vote - 1;
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
