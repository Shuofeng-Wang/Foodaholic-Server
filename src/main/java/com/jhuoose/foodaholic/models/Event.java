package com.jhuoose.foodaholic.models;

import java.util.ArrayList;

public class Event {
    private int id;
    private String eventName;
    private String entryCode;
    private String description;
    private String location;
    private String startTime; //to Date
    private String endTime;
    private int organizerId;
    private String theme;
    private ArrayList<Integer> participantIdList = new ArrayList<>();
    private ArrayList<Integer> activityIdList = new ArrayList<>();

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", organizerId=" + organizerId +
                ", theme='" + theme + '\'' +
                ", participantIdList=" + participantIdList +
                ", activityIdList=" + activityIdList +
                '}';
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

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
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

    public int getOrganizerId() {
        return organizerId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
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

    public ArrayList<Integer> getActivityIdList() {
        return activityIdList;
    }

    public void setActivityIdList(ArrayList<Integer> activityIdList) {
        this.activityIdList = activityIdList;
    }{
        this.participantIdList = participantIdList;
    }

    public void addActivity(int activityId) {
        if (!this.activityIdList.contains(activityId)) this.activityIdList.add(activityId);
    }

    public boolean isActivity(int activityId) {
        return this.activityIdList.contains(activityId);
    }

    public boolean deleteActivity(int activityId) {
        return this.activityIdList.remove(Integer.valueOf(activityId));
    }

}
