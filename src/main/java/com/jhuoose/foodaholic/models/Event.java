package com.jhuoose.foodaholic.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Event {

    private Integer identifier;
    private String eventName;
    private String startTime;
    private String endTime;
    private ArrayList<Activity> activityList = new ArrayList<>();

    public Event(Integer identifier, String eventName, String startTime, String endTime, ArrayList<Activity> activityList) {
        this.identifier = identifier;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityList = activityList;
    }

    public Event(Integer identifier, String eventName, String startTime, String endTime) {
        this.identifier = identifier;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<Activity> activityList) {
        this.activityList = activityList;
    }
}
