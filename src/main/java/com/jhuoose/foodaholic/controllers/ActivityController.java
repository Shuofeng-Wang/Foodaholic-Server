package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.*;
import com.jhuoose.foodaholic.repositories.ActivityNotFoundException;
import com.jhuoose.foodaholic.repositories.ActivityRepository;
import com.jhuoose.foodaholic.views.ActivityFullView;
import com.jhuoose.foodaholic.views.EventProfileView;
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

    public void getActivityView(Context ctx) throws ActivityNotFoundException, EventNotFoundException, UserNotFoundException, SQLException{
        var id = ctx.pathParam("id", Integer.class).get();
        var activity = activityRepository.getOne(id);
        var event = EventRepository.getInstance().getOne(activity.getEventId());
        var eventProfile = new EventProfileView(event);
        var voteList = UserRepository.getInstance().getMultiple(activity.getVoteIdList());
        var voteProfiles = new ArrayList<UserProfileView>();
        for (User vote : voteList) voteProfiles.add(new UserProfileView(vote));
        var booList = UserRepository.getInstance().getMultiple(activity.getBooIdList());
        var booProfiles = new ArrayList<UserProfileView>();
        for (User boo : booList) booProfiles.add(new UserProfileView(boo));
        var participants = UserRepository.getInstance().getMultiple(activity.getParticipantIdList());
        var participantsProfiles = new ArrayList<UserProfileView>();
        for (User participant : participants) participantsProfiles.add(new UserProfileView(participant));
        var payer = UserRepository.getInstance().getOne(activity.getPayerId());
        var payerProfile = new UserProfileView(payer);
        ctx.json(new ActivityFullView(activity, eventProfile, voteProfiles, booProfiles, participantsProfiles, payerProfile));
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
        int currentUserId = UserController.currentUserId(ctx);
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        if (activity.isBooId(currentUserId)) {
            activity.deleteBooId(currentUserId);
            activity.upVote();
        }
        if (!activity.isVoteId(currentUserId)) {
            activity.addVoteId(currentUserId);
            activity.upVote();
        }
        activityRepository.update(activity);
        ctx.status(204);
    }

    public void boo(Context ctx) throws ActivityNotFoundException, SQLException {
        int currentUserId = UserController.currentUserId(ctx);
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        if (activity.isVoteId(currentUserId)) {
            activity.deleteVoteId(currentUserId);
            activity.downVote();
        }
        if (!activity.isBooId(currentUserId)) {
            activity.addBooId(currentUserId);
            activity.downVote();
        }
        activityRepository.update(activity);
        ctx.status(204);
    }

    public void join(Context ctx) throws ActivityNotFoundException, EventNotFoundException, SQLException {
        int currentUserId = UserController.currentUserId(ctx);
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        var event = EventRepository.getInstance().getOne(activity.getEventId());
        if (event.isParticipant(currentUserId))
            activity.addParticipant(currentUserId);
        activityRepository.update(activity);
        ctx.status(204);
    }

    public void leave(Context ctx) throws ActivityNotFoundException, SQLException {
        int currentUserId = UserController.currentUserId(ctx);
        var activity = activityRepository.getOne(ctx.pathParam("id", Integer.class).get());
        activity.deleteParticipant(currentUserId);
        activityRepository.update(activity);
        ctx.status(204);
    }
}
