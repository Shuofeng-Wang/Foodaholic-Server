package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.models.Event;
import com.jhuoose.foodaholic.models.Notification;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.*;
import com.jhuoose.foodaholic.utils.EmailUtil;
import com.jhuoose.foodaholic.views.ActivityProfileView;
import com.jhuoose.foodaholic.views.EventFullView;
import com.jhuoose.foodaholic.views.UserProfileView;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;


import java.io.IOException;
import java.rmi.activation.ActivationSystem;
import java.sql.SQLException;
import java.util.*;

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

    public void getOrganizer(Context ctx) throws SQLException, EventNotFoundException, UserNotFoundException {
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        var organizer = UserRepository.getInstance().getOne(event.getOrganizerId());
        var organizerProfile = new UserProfileView(organizer);
        ctx.json(organizerProfile);
    }

    public void getEntryCode(Context ctx) throws SQLException, EventNotFoundException {
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        if (event.getOrganizerId() != UserController.currentUserId(ctx))
            throw new ForbiddenResponse();
        ctx.result(event.getEntryCode());
    }

    public void sendEntryCodeToOne(Context ctx) throws SQLException, UserNotFoundException, EventNotFoundException, IOException {
        int currentUserId = UserController.currentUserId(ctx);
        var currentUser = UserRepository.getInstance().getOne(currentUserId);
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        var email = ctx.formParam("guestEmail", "");
        String subject = "Entry code to event " + event.getEventName();
        String content = "Dear Friend, \n\n" +
                "You are invited to the event " + event.getEventName() +
                " by " + currentUser.getUserName() + "<" + currentUser.getEmail() + ">. " +
                "The entry code to this event is: " + event.getEntryCode() + ". " +
                "You can now join the event in the Foodaholic app. \n\n" +
                "Sincerely, \n\nFoodaholic Team";
        EmailUtil.getInstance().sendEmailToOne(subject, email, content);
        ctx.status(201);
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
        int currentUserId = UserController.currentUserId(ctx);
        var eventId = ctx.pathParam("id", Integer.class).get();
        var event = eventRepository.getOne(eventId);
        var activity = new Activity();
        activity.setActivityName(ctx.formParam("activityName","").isEmpty() ? "New Activity" : ctx.formParam("activityName",""));
        activity.setDescription(ctx.formParam("description",""));
        activity.setVote(0);
        activity.setMoney(ctx.formParam("money", Float.class).getOrNull() == null ? 0 : ctx.formParam("money", Float.class).get());
        activity.setCategory(ctx.formParam("category", ""));
        activity.setEventId(eventId);
        activity.addParticipant(currentUserId);
        activity.setPayerId(ctx.formParam("payerId", Integer.class).getOrNull() == null ? currentUserId : ctx.pathParam("payerId", Integer.class).get());
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

    public void splitBill(Context ctx) throws SQLException, EventNotFoundException, UserNotFoundException {
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        var activities = ActivityRepository.getInstance().getMultiple(event.getActivityIdList());
        var notification = new Notification();
        notification.setTime(new Date(System.currentTimeMillis()));
        notification.setTitle("Bill of event " + event.getEventName());
        notification.setCategory("Bill");
        var content = "The event " + event.getEventName() + " is over. " +
                "Participants shall pay to each other as below: \n";
        Map<Integer, Double> spending = new HashMap<>();
        for (Activity activity: activities) {
            var people = activity.getParticipantIdList().size();
            if (!activity.getParticipantIdList().isEmpty()) {
                spending.put(activity.getPayerId(),
                        spending.getOrDefault(activity.getPayerId(), 0.0) + activity.getMoney());
            }
            for (Integer participantId: activity.getParticipantIdList()) {
                spending.put(participantId,
                        spending.getOrDefault(participantId, 0.0) - activity.getMoney() / people);
            }
        }
        Iterator<Map.Entry<Integer, Double>> iterator0 = spending.entrySet().iterator();
        Iterator<Map.Entry<Integer, Double>> iterator1 = spending.entrySet().iterator();
        Map.Entry<Integer, Double> entry0, entry1;
        if (iterator1.hasNext()) entry1 = iterator1.next(); else throw new ForbiddenResponse();
        while (iterator0.hasNext()) {
            entry0 = iterator0.next();
            if (entry1.getKey().equals(entry0.getKey()) && iterator1.hasNext()) entry1 = iterator1.next();
            if (entry0.getValue() == 0 || !iterator0.hasNext()) continue;
            while (entry0.getValue() * entry1.getValue() >= 0 && iterator1.hasNext()) entry1 = iterator1.next();
            var user0 = UserRepository.getInstance().getOne(entry0.getKey());
            var user0string = user0.getUserName() + "<" + user0.getEmail() + ">";
            var user1 = UserRepository.getInstance().getOne(entry1.getKey());
            var user1string = user1.getUserName() + "<" + user1.getEmail() + ">";
            var value = Math.round(100.0 * Math.abs(entry0.getValue())) / 100.0;
            if (entry0.getValue() > 0 && value != 0) content = content + user1string + " shall give " + user0string + " $" + value + ".\n";
            if (entry0.getValue() < 0 && value != 0) content = content + user0string + " shall give " + user1string + " $" + (-value) + ".\n";
            entry1.setValue(entry0.getValue() + entry1.getValue());
            entry0.setValue(0.0);
        }
        notification.setContent(content);
        int notificationId = NotificationRepository.getInstance().create(notification);
        for (Integer userId: spending.keySet()) {
            var user = UserRepository.getInstance().getOne(userId);
            user.addNotification(notificationId);
            UserRepository.getInstance().update(user);
        }
        ctx.status(201);
    }
}