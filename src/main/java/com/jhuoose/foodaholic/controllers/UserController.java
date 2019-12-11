package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.Event;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.EventNotFoundException;
import com.jhuoose.foodaholic.repositories.EventRepository;
import com.jhuoose.foodaholic.repositories.UserNotFoundException;
import com.jhuoose.foodaholic.repositories.UserRepository;
import com.jhuoose.foodaholic.views.EventProfileView;
import com.jhuoose.foodaholic.views.UserFullView;
import com.jhuoose.foodaholic.views.UserProfileView;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
    private static UserController ourInstance = new UserController(UserRepository.getInstance());

    public static UserController getInstance() {
        return ourInstance;
    }

    private UserRepository userRepository;

    private UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(Context ctx) throws SQLException {
        if (userRepository.isEmailExist(ctx.formParam("email", ""))) {
            throw new ForbiddenResponse("Email already exists");
        }
        String bcryptHashPassword = BCrypt.withDefaults().hashToString(12, ctx.formParam("password", "").toCharArray());
        var user = new User(ctx.formParam("email", ""), bcryptHashPassword);
        user.setUserName(ctx.formParam("userName", "").isEmpty() ? "Foodaholicer" : ctx.formParam("userName", ""));
        user.setPhotoURL(ctx.formParam("userName", ""));
        user.setPhone(ctx.formParam("userName", ""));
        userRepository.create(user);
        ctx.status(201);
    }

    public void login(Context ctx) throws SQLException, UserNotFoundException {
        if (!userRepository.isEmailExist(ctx.formParam("email", ""))) {
            throw new ForbiddenResponse("Incorrect email or password");
        }
        var user = userRepository.getOne(ctx.formParam("email", ""));
        if (!BCrypt.verifyer().verify(ctx.formParam("password", "").toCharArray(), user.getPassword()).verified) {
            throw new ForbiddenResponse("Incorrect email or password");
        }
        ctx.sessionAttribute("userId", user.getId());
        ctx.status(200);
    }

    public void logout(Context ctx) {
        ctx.sessionAttribute("userId", null);
        ctx.status(200);
    }

    public static Integer currentUserId(Context ctx) {
        var userId = (Integer) ctx.sessionAttribute("userId");
        if (userId == null) throw new ForbiddenResponse();
        return userId;
    }

    public void getCurrentUserView(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        var events = EventRepository.getInstance().getMultiple(user.getParticipatingEventIdList());
        var eventProfiles = new ArrayList<EventProfileView>();
        for (Event event : events) eventProfiles.add(new EventProfileView(event));
        var friends = userRepository.getMultiple(user.getFriendIdList());
        var friendProfiles = new ArrayList<UserProfileView>();
        for (User friend : friends) friendProfiles.add(new UserProfileView(friend));
        ctx.json(new UserFullView(user, friendProfiles, eventProfiles));
    }

    public void deleteCurrentUser(Context ctx) throws SQLException, UserNotFoundException {
        userRepository.delete(currentUserId(ctx));
        ctx.sessionAttribute("userId", null);
        ctx.status(204);
    }

    public void getParticipatingEventList(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        var events = EventRepository.getInstance().getMultiple(user.getParticipatingEventIdList());
        var eventProfiles = new ArrayList<EventProfileView>();
        for (Event event : events) eventProfiles.add(new EventProfileView(event));
        ctx.json(eventProfiles);
    }

    public void joinEvent(Context ctx) throws SQLException, UserNotFoundException, EventNotFoundException {
        var entryCode = ctx.formParam("entryCode", "");
        var event = EventRepository.getInstance().getOne(entryCode);
        var user = userRepository.getOne(currentUserId(ctx));
        user.addParticipatingEvent(event.getId());
        userRepository.update(user);
        event.addParticipant(currentUserId(ctx));
        EventRepository.getInstance().update(event);
        ctx.status(204);
    }

    public void leaveEvent(Context ctx) throws SQLException, UserNotFoundException, EventNotFoundException {
        var eventId = ctx.pathParam("id", Integer.class).get();
        var event = EventRepository.getInstance().getOne(eventId);
        var user = userRepository.getOne(currentUserId(ctx));
        user.deleteParticipatingEvent(eventId);
        userRepository.update(user);
        event.deleteParticipant(currentUserId(ctx));
        EventRepository.getInstance().update(event);
        ctx.status(204);
    }

//    public void getNotificationList();
//
//    public void addNotification();
//
//    public void deleteNotification();

    public void getFriendList(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        var friends = userRepository.getMultiple(user.getFriendIdList());
        var result = new ArrayList<UserProfileView>();
        for (User friend : friends) result.add(new UserProfileView(friend));
        ctx.json(result);
    }

    public void addFriend(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        var friend = userRepository.getOne(ctx.pathParam("id", Integer.class).get());
        user.addFriend(friend.getId());
        friend.addFriend(user.getId());
        userRepository.update(user);
        userRepository.update(friend);
        ctx.status(204);
    }

    public void deleteFriend(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        var friend = userRepository.getOne(ctx.pathParam("id", Integer.class).get());
        user.deleteFriend(friend.getId());
        friend.deleteFriend(user.getId());
        userRepository.update(user);
        userRepository.update(friend);
        ctx.status(204);
    }

    public void getCurrentUserProfile(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        ctx.json(new UserProfileView(user));
    }

    public void updateCurrentUserProfile(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        if (!ctx.formParam("email", "").isEmpty()) {
            if (userRepository.isEmailExist(ctx.formParam("email", ""))) {
                throw new ForbiddenResponse("Email already exists");
            }
            user.setEmail(ctx.formParam("email", ""));
        }
        if (!ctx.formParam("userName", "").isEmpty()) user.setUserName(ctx.formParam("userName", ""));
        if (!ctx.formParam("photoURL", "").isEmpty()) user.setPhotoURL(ctx.formParam("userName", ""));
        if (!ctx.formParam("phone", "").isEmpty()) user.setPhone(ctx.formParam("userName", ""));
        userRepository.update(user);
        ctx.status(204);
    }

    public void getProfileById(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(ctx.pathParam("id", Integer.class).get());
        ctx.json(new UserProfileView(user));
    }

    public void getProfileByEmail(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(ctx.formParam("email", ""));
        ctx.json(new UserProfileView(user));
    }
}