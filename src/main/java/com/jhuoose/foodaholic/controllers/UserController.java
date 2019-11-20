package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.UserNotFoundException;
import com.jhuoose.foodaholic.repositories.UserRepository;
import com.jhuoose.foodaholic.views.UserProfileView;
import com.jhuoose.foodaholic.views.UserView;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.SQLException;

public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
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

    public Integer currentUserId(Context ctx) {
        var userId = (Integer) ctx.sessionAttribute("userId");
        if (userId == null) throw new ForbiddenResponse();
        return userId;
    }

    public void getCurrentUser(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        ctx.json(new UserView(user));
    }

    public void deleteCurrentUser(Context ctx) throws SQLException, UserNotFoundException {
        userRepository.delete(currentUserId(ctx));
        ctx.sessionAttribute("userId", null);
        ctx.status(204);
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

    public void joinEvent(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        user.addParticipatingEvent(ctx.pathParam("id", Integer.class).get());
        userRepository.update(user);
        ctx.status(204);
    }

    public void leaveEvent(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(currentUserId(ctx));
        user.deleteParticipatingEvent(ctx.pathParam("id", Integer.class).get());
        userRepository.update(user);
        ctx.status(204);
    }

    public void getProfileById(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(ctx.formParam("id", Integer.class).get());
        ctx.json(new UserProfileView(user));
    }

    public void getProfileByEmail(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(ctx.formParam("email", ""));
        ctx.json(new UserProfileView(user));
    }
}