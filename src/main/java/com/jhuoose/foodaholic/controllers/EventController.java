package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.models.Event;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.*;
import com.jhuoose.foodaholic.views.ActivityProfileView;
import com.jhuoose.foodaholic.views.EventFullView;
import com.jhuoose.foodaholic.views.UserProfileView;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;


import java.rmi.activation.ActivationSystem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class EventController{
    private static EventController ourInstance = new EventController(EventRepository.getInstance());

    public static EventController getInstance() {
        return ourInstance;
    }

    private EventRepository eventRepository;

    private EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void create(Context ctx) throws SQLException, UserNotFoundException {
        int currentUserId = UserController.currentUserId(ctx);
        var event = new Event();
        event.setEventName(ctx.formParam("eventName", "").isEmpty() ? "New Event" : ctx.formParam("eventName", ""));
        event.setEntryCode(UUID.randomUUID().toString().replaceAll("-", ""));
        event.setDescription(ctx.formParam("description", ""));
        event.setLocation(ctx.formParam("location", ""));
        event.setStartTime(ctx.formParam("startTime", ""));
        event.setEndTime(ctx.formParam("endTime", ""));
        event.setOrganizerId(currentUserId);
        event.setTheme(ctx.formParam("theme", ""));
        event.addParticipant(currentUserId);
        var eventId = eventRepository.create(event);
        var user = UserRepository.getInstance().getOne(currentUserId);
        user.addParticipatingEvent(eventId);
        UserRepository.getInstance().update(user);
        ctx.status(201);
    }

    public void delete(Context ctx) throws SQLException, EventNotFoundException {
        eventRepository.delete(eventRepository.getOne(ctx.pathParam("id", Integer.class).get()));
        ctx.status(204);
    }

    public void getEventView(Context ctx) throws SQLException, EventNotFoundException, UserNotFoundException {
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        var organizer = UserRepository.getInstance().getOne(event.getOrganizerId());
        var organizerProfile = new UserProfileView(organizer);
        var participants = UserRepository.getInstance().getMultiple(event.getParticipantIdList());
        var participantsProfiles = new ArrayList<UserProfileView>();
        for (User participant : participants) participantsProfiles.add(new UserProfileView(participant));
        var activities = ActivityRepository.getInstance().getMultiple(event.getActivityIdList());
        var activityProfiles = new ArrayList<ActivityProfileView>();
        for (Activity activity : activities) activityProfiles.add(new ActivityProfileView(activity));
        ctx.json(new EventFullView(event, organizerProfile, participantsProfiles, activityProfiles));
    }

    public void getEntryCode(Context ctx) throws SQLException, EventNotFoundException {
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        if (event.getOrganizerId() != UserController.currentUserId(ctx))
            throw new ForbiddenResponse();
        ctx.result(event.getEntryCode());
    }

    public void getAll(Context ctx) throws SQLException, EventNotFoundException {
        ctx.json(eventRepository.getAll());
    }

    public void update(Context ctx) throws SQLException, EventNotFoundException {
        var event = eventRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var eventName = ctx.formParam("eventName","");
        if(!eventName.isEmpty()) event.setEventName(eventName);
        var description = ctx.formParam("description","");
        if(!description.isEmpty()) event.setDescription(description);
        var location = ctx.formParam("location","");
        if(!location.isEmpty()) event.setLocation(location);
        var startTime = ctx.formParam("startTime", "");
        if(!startTime.isEmpty()) event.setStartTime(startTime);
        var endTime = ctx.formParam("endTime", "");
        if(!endTime.isEmpty()) event.setEndTime(endTime);
        var organizerId = ctx.formParam("organizerId", Integer.class).getOrNull();
        if(organizerId != null) event.setOrganizerId(organizerId);
        var theme = ctx.formParam("theme", "");
        if(!theme.isEmpty()) event.setTheme(theme);
        eventRepository.update(event);
        ctx.status(204);
    }

    public void getActivityList(Context ctx) throws SQLException, EventNotFoundException {
        var id = ctx.pathParam("id", Integer.class).get();
        var event = eventRepository.getOne(id);
        var activities = ActivityRepository.getInstance().getMultiple(event.getActivityIdList());
        var activityProfiles = new ArrayList<ActivityProfileView>();
        for (Activity activity : activities) activityProfiles.add(new ActivityProfileView(activity));
        ctx.json(activityProfiles);
    }

    public void createActivity(Context ctx) throws SQLException, EventNotFoundException {
        var eventId = ctx.pathParam("id", Integer.class).get();
        var event = eventRepository.getOne(eventId);
        var activity = new Activity();
        activity.setActivityName(ctx.formParam("activityName","").isEmpty() ? "New Activity" : ctx.formParam("activityName",""));
        activity.setDescription(ctx.formParam("description",""));
        activity.setVote(0);
        activity.setMoney(ctx.formParam("money", Float.class).getOrNull() == null ? 0 : ctx.formParam("money", Float.class).getOrNull());
        activity.setCategory(ctx.formParam("category", ""));
        var activityId = ActivityRepository.getInstance().create(activity);
        event.addActivity(activityId);
        eventRepository.update(event);
        ctx.status(201);
    }

    public void deleteActivity(Context ctx) throws SQLException, EventNotFoundException {
        var eventId = ctx.pathParam("id", Integer.class).get();
        var activityId = ctx.pathParam("activityId", Integer.class).get();
        var event = eventRepository.getOne(eventId);
        event.deleteActivity(activityId);
        eventRepository.update(event);
        ActivityRepository.getInstance().delete(activityId);
        ctx.status(204);
    }
}