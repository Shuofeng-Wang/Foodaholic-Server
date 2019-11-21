package com.jhuoose.foodaholic.views;

import com.jhuoose.foodaholic.models.Event;

import java.util.ArrayList;

public class EventFullView {
    private int id;
    private String eventName;
    private String description;
    private String location;
    private String startTime;
    private String endTime;
    private UserProfileView organizer;
    private String theme;
    private ArrayList<UserProfileView> participantList = new ArrayList<>();
    private ArrayList<ActivityProfileView> activityList = new ArrayList<>();

    public EventFullView(Event event,
                         UserProfileView organizer,
                         ArrayList<UserProfileView> participantList,
                         ArrayList<ActivityProfileView> activityList) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.organizer = organizer;
        this.theme = event.getTheme();
        this.participantList = participantList;
        this.activityList = activityList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public UserProfileView getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserProfileView organizer) {
        this.organizer = organizer;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ArrayList<UserProfileView> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ArrayList<UserProfileView> participantList) {
        this.participantList = participantList;
    }

    public ArrayList<ActivityProfileView> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<ActivityProfileView> activityList) {
        this.activityList = activityList;
    }
}
