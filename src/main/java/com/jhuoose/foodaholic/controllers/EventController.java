package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.repositories.EventRepository;

public class EventController{
    private EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}