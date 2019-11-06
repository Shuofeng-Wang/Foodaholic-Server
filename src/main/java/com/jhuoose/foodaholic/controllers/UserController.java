package com.jhuoose.foodaholic.controllers;

import com.jhuoose.foodaholic.repositories.UserNotFoundException;
import com.jhuoose.foodaholic.repositories.UserRepository;
import io.javalin.http.Context;

import java.sql.SQLException;

public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getAll(Context ctx) throws SQLException {
        ctx.json(userRepository.getAll());
    }

    public void create(Context ctx) throws SQLException {
        userRepository.create();
        ctx.status(201);
    }

    public void delete(Context ctx) throws SQLException, UserNotFoundException {
        userRepository.delete(userRepository.getOne(ctx.pathParam("identifier", Integer.class).get()));
        ctx.status(204);
    }

    public void update(Context ctx) throws SQLException, UserNotFoundException {
        var item = userRepository.getOne(ctx.pathParam("identifier", Integer.class).get());
        item.setDescription(ctx.formParam("description", ""));
        userRepository.update(item);
        ctx.status(204);
    }
}
