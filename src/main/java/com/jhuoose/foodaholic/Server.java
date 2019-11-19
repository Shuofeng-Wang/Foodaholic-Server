package com.jhuoose.foodaholic;

import com.jhuoose.foodaholic.controllers.ActivityController;
import com.jhuoose.foodaholic.controllers.EventController;
import com.jhuoose.foodaholic.controllers.UserController;
import com.jhuoose.foodaholic.models.Activity;
import com.jhuoose.foodaholic.models.Event;
import com.jhuoose.foodaholic.models.User;
import com.jhuoose.foodaholic.repositories.*;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {
    public static void main(String[] args) throws SQLException {
//        var connection = DriverManager.getConnection("jdbc:sqlite:todoose.db");
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection;
            if (System.getenv("DATABASE_URL") == null)
                if (System.getenv("TRAVIS") == null)
                    connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodaholic", "postgres", "postgres");
                else connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/travis_ci_test", "postgres", "");
            else connection = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
            var userRepository = new UserRepository(connection);
            var userController = new UserController(userRepository);
            var eventRepository = new EventRepository(connection);
            var eventController = new EventController(eventRepository);
            var activityRepository = new ActivityRepository(connection);
            var activityController = new ActivityController(activityRepository);
            Javalin.create(config -> { config.addStaticFiles("/public"); })
                    .events(event -> {
                        event.serverStopped(() -> { connection.close(); });
                    })
                    .routes(() -> {
                        path("users", () -> {
                            post(userController::register);
                            path("login", () -> {
                                post(userController::login);
                            });
                        });
                        path("events", () -> {
                            post(eventController::create);
                            get(eventController::getAll);
                            path(":id", () -> {
                                delete(eventController::delete);
                                get(eventController::getOne);
                            });
                        });
                        path("activities", () -> {
                            post(activityController::create);
                            get(activityController::getAll);
                            path(":id", () -> {
                                delete(activityController::delete);
                                get(activityController::getOne);
                            });
                        });
                    })
                    //.exception(EventNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .start(System.getenv("PORT") == null ? 4000 : Integer.parseInt(System.getenv("PORT")));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

    }
}
