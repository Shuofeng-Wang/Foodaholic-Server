package com.jhuoose.foodaholic;

import com.jhuoose.foodaholic.controllers.UserController;
import com.jhuoose.foodaholic.repositories.UserRepository;
import io.javalin.Javalin;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {
    public static void main(String[] args) throws SQLException {
//        var connection = DriverManager.getConnection("jdbc:sqlite:todoose.db");
        try {
            Class.forName("org.postgresql.Driver");
            var connection = System.getenv("DATABASE_URL") == null ?
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodaholic",
                            "postgres", "postgres") :
                    DriverManager.getConnection(System.getenv("DATABASE_URL"));
            var userRepository = new UserRepository(connection);
            var userController = new UserController(userRepository);
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
                    })
//                    .exception(ItemNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .start(System.getenv("PORT") == null ? 4000 : Integer.parseInt(System.getenv("PORT")));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

    }
}
