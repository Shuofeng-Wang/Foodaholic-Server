package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.*;
import com.jhuoose.foodaholic.repositories.ActivityNotFoundException;
import com.jhuoose.foodaholic.repositories.ActivityRepository;
import com.jhuoose.foodaholic.views.ActivityFullView;
import com.jhuoose.foodaholic.views.UserProfileView;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityController {
    private static ActivityController ourInstance = new ActivityController(ActivityRepository.getInstance());

    public static ActivityController getInstance() {
        return ourInstance;
    }

    private ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void getActivityView(Context ctx) throws ActivityNotFoundException, SQLException{
        var id = ctx.pathParam("id", Integer.class).get();
        var activity = activityRepository.getOne(id);
        var participants = UserRepository.getInstance().getMultiple(activity.getParticipantIdList());
        var participantsProfiles = new ArrayList<UserProfileView>();
        for (User participant : participants) participantsProfiles.add(new UserProfileView(participant));
        ctx.json(new ActivityFullView(activity, participantsProfiles));
    }

    public void getAll(Context ctx) throws ActivityNotFoundException, SQLException{
        ctx.json(activityRepository.getAll());
    }

    public void update(Context ctx) throws ActivityNotFoundException, SQLException {
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var activityName = ctx.formParam("activityName","");
        if(!activityName.isEmpty()) activity.setActivityName(activityName);
        var description = ctx.formParam("description","");
        if(!description.isEmpty()) activity.setDescription(description);
        var vote = ctx.formParam("vote", Integer.class).getOrNull();
        if(vote != null) activity.setVote(vote);
        var money = ctx.formParam("money", Float.class).getOrNull();
        if(money != null) activity.setMoney(money);
        var category = ctx.formParam("category", "");
        if(!category.isEmpty()) activity.setCategory(category);
        activityRepository.update(activity);
        ctx.status(204);
    }

    public void vote(Context ctx) throws ActivityNotFoundException, SQLException {
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        activity.upVote();
        activityRepository.update(activity);
        ctx.status(204);
    }

    public void boo(Context ctx) throws ActivityNotFoundException, SQLException {
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        activity.downVote();
        activityRepository.update(activity);
        ctx.status(204);
    }
}
