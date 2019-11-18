package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.repositories.EventRepository;

import javax.naming.Context;
import java.sql.SQLException;

public class EventController{
    private EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void create(Context ctx) throws SQLException{
        eventRepository.create();
        ctx.status(201);
    }


}