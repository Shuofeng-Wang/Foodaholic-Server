package com.jhuoose.foodaholic.models;

import java.util.ArrayList;

public class Activity {
    private int id;
    private String activityName;
    private String description;
    private int vote;
    private double money;
    private String category;
    private int eventId;
    private ArrayList<Integer> voteIdList = new ArrayList<>();
    private ArrayList<Integer> booIdList = new ArrayList<>();
    private ArrayList<Integer> participantIdList = new ArrayList<>();
    private int payerId;

    public Activity() {
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

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public ArrayList<Integer> getVoteIdList() {
        return voteIdList;
    }

    public void setVoteIdList(ArrayList<Integer> voteIdList) {
        this.voteIdList = voteIdList;
    }

    public void addVoteId(int voteId) {
        if (!this.voteIdList.contains(voteId)) this.voteIdList.add(voteId);
    }

    public boolean isVoteId(int voteId) {
        return this.voteIdList.contains(voteId);
    }

    public boolean deleteVoteId(int voteId) {
        return this.voteIdList.remove(Integer.valueOf(voteId));
    }

    public ArrayList<Integer> getBooIdList() {
        return booIdList;
    }

    public void setBooIdList(ArrayList<Integer> booIdList) {
        this.booIdList = booIdList;
    }

    public void addBooId(int booId) {
        if (!this.booIdList.contains(booId)) this.booIdList.add(booId);
    }

    public boolean isBooId(int booId) {
        return this.booIdList.contains(booId);
    }

    public boolean deleteBooId(int booId) {
        return this.booIdList.remove(Integer.valueOf(booId));
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }
}
