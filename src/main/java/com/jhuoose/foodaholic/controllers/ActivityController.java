package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.repositories.ActivityNotFoundException;
import com.jhuoose.foodaholic.repositories.ActivityRepository;
import com.jhuoose.foodaholic.repositories.ActivityNotFoundException;
import com.jhuoose.foodaholic.repositories.ActivityRepository;
import io.javalin.http.Context;

import java.sql.SQLException;

public class ActivityController {
    private ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
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

    public void edit(Context ctx) throws ActivityNotFoundException, SQLException {
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var activityName = ctx.formParam("activityName");
        if(activityName != null) activity.setActivityName(activityName);
        var description = ctx.formParam("description","");
        if(!description.isEmpty()) activity.setDescription(description);
        var vote = ctx.formParam("vote", Integer.class).getOrNull();
        if(vote != null) activity.setVote(vote);
        var money = ctx.formParam("money", Float.class).getOrNull();
        if(money != null) activity.setMoney(money);
        var category = ctx.formParam("category");
        if(category != null) activity.setCategory(category);
        activityRepository.edit(activity);
        ctx.status(204);
        ctx.json("Edit Successfully!");
    }
}
