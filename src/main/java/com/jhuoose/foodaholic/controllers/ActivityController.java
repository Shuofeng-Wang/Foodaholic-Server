package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.repositories.ActivityNotFoundException;
import com.jhuoose.foodaholic.repositories.ActivityRepository;
import com.jhuoose.foodaholic.repositories.EventNotFoundException;
import com.jhuoose.foodaholic.repositories.EventRepository;
import io.javalin.http.Context;

import java.sql.SQLException;

public class ActivityController {
    private ActivityRepository activityRepository;

    public Controller(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void create(Context ctx) throws SQLException {
        activityRepository.create();
        ctx.status(201);
        ctx.json("Create Successfully!");
    }

    public void delete(Context ctx) throws SQLException, ActivityNotFoundException {
        activityRepository.delete(activityRepository.getOne(ctx.pathParam("id", Integer.class).get()));
        ctx.json("Delete Successfully!");
    }

    public void update(Context ctx) throws ActivityNotFoundException, SQLException {
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var activityName = ctx.formParam("activityName", "");
        //if(!activityName.isEmpty()) activity.setActivityName(activityName);
    }

    public void getOne(Context ctx) throws  ActivityNotFoundException, SQLException{
        var id = ctx.pathParam("id", Integer.class);
        var activity = activityRepository.getOne(id.get());
        ctx.json(activity);
    }

    public void getAll(Context ctx) throws ActivityNotFoundException, SQLException{
        ctx.json(activityRepository.getAll());
    }
}
