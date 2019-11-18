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
    }

    public void delete(Context ctx) throws SQLException, EventNotFoundException {
        eventRepository.delete(eventRepository.getOne(ctx.pathParam("identifier", Integer.class).get()));

    }
}