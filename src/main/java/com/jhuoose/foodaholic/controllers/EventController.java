package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.repositories.EventNotFoundException;
import com.jhuoose.foodaholic.repositories.EventRepository;
import io.javalin.http.Context;


import java.rmi.activation.ActivationSystem;
import java.sql.SQLException;

public class EventController{
    private EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void create(Context ctx) throws SQLException {
        eventRepository.create();
        ctx.status(201);
        ctx.json("Create Successfully!");
    }

    public void delete(Context ctx) throws SQLException, EventNotFoundException {
        eventRepository.delete(eventRepository.getOne(ctx.pathParam("id", Integer.class).get()));
        ctx.json("Delete Successfully!");
        ctx.status(204);
    }

    public void getOne(Context ctx) throws  EventNotFoundException, SQLException{
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        ctx.json(event);
    }

    public void getAll(Context ctx) throws  EventNotFoundException, SQLException{
        ctx.json(eventRepository.getAll());
    }

    public void edit(Context ctx) throws EventNotFoundException, SQLException {
        var event = eventRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var eventName = ctx.formParam("eventName");
        if(eventName != null) event.setEventName(eventName);
        var description = ctx.formParam("description","");
        if(!description.isEmpty()) event.setDescription(description);
        var location = ctx.formParam("location","");
        if(!location.isEmpty()) event.setLocation(location);
        var startTime = ctx.formParam("startTime", "");
        if(!startTime.isEmpty()) event.setStartTime(startTime);
        var endTime = ctx.formParam("endTime", "");
        if(!endTime.isEmpty()) event.setEndTime(endTime);
        var organizerId = ctx.formParam("organizerId", Integer.class).get();
        if(organizerId != null) event.setOrganizerId(organizerId);
        var theme = ctx.formParam("theme");
        if(theme != null) event.setTheme(theme);
        eventRepository.edit(event);
        ctx.status(204);
    }
}