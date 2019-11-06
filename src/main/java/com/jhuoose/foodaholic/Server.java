package com.jhuoose.foodaholic;

import com.jhuoose.foodaholic.controllers.UserController;
import com.jhuoose.foodaholic.repositories.UserRepository;
import io.javalin.Javalin;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {
    public static void main(String[] args) throws SQLException {
        var connection = DriverManager.getConnection("jdbc:sqlite:todoose.db");
        var userRepository = new UserRepository(connection);
        var userController = new UserController(userRepository);
        Javalin.create(config -> { config.addStaticFiles("/public"); })
        .events(event -> {
            event.serverStopped(() -> { connection.close(); });
        })
        .routes(() -> {
            path("items", () -> {
                get(userController::getAll);
                post(userController::create);
                path(":identifier", () -> {
                    delete(userController::delete);
                    put(userController::update);
                });
            });
        })
//        .exception(ItemNotFoundException.class, (e, ctx) -> { ctx.status(404); })
        .start(System.getenv("PORT") == null ? 4000 : Integer.parseInt(System.getenv("PORT")));
    }
}
