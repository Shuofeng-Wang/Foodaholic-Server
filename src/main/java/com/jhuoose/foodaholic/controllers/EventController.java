package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.repositories.EventNotFoundException;
import com.jhuoose.foodaholic.repositories.EventRepository;
import io.javalin.http.Context;


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
    }

    public void update(Context ctx) throws EventNotFoundException, SQLException {
        var event = eventRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var eventName = ctx.formParam("eventName", "");
        //if(!eventName.isEmpty()) event.setEventName(eventName);
    }

    public void getOne(Context ctx) throws  EventNotFoundException, SQLException{
        var id = ctx.pathParam("id", Integer.class);
        var event = eventRepository.getOne(id.get());
        ctx.json(event);
    }

    public void getAll(Context ctx) throws  EventNotFoundException, SQLException{
        ctx.json(eventRepository.getAll());
    }
}