package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.UserNotFoundException;
import com.jhuoose.foodaholic.repositories.UserRepository;
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
        String bcryptHashPassword = BCrypt.withDefaults().hashToString(12, ctx.formParam("password", "").toCharArray());
        userRepository.create(new User(ctx.formParam("email", ""), bcryptHashPassword));
        ctx.status(201);
    }

    public void login(Context ctx) throws SQLException, UserNotFoundException {
        var user = userRepository.getOne(ctx.formParam("email", ""));
        if (!BCrypt.verifyer().verify(ctx.formParam("password", "").toCharArray(), user.getPassword()).verified) {
            throw new ForbiddenResponse();
        }
        ctx.sessionAttribute("user", user);
        ctx.status(200);
    }

    public User currentUser(Context ctx) {
        var user = (User) ctx.sessionAttribute("user");
        if (user == null) throw new ForbiddenResponse();
        return user;
    }
}