package com.jhuoose.foodaholic;

import com.jhuoose.foodaholic.controllers.ActivityController;
import com.jhuoose.foodaholic.controllers.EventController;
import com.jhuoose.foodaholic.controllers.UserController;
import com.jhuoose.foodaholic.repositories.*;
import com.jhuoose.foodaholic.utils.EmailUtil;
import com.sendgrid.SendGrid;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Server {
    private static Connection connection;
    private static SendGrid sendGrid;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            if (System.getenv("DATABASE_URL") == null)
                if (System.getenv("TRAVIS") == null)
                    connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodaholic", "postgres", "postgres");
                else connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/travis_ci_test", "postgres", "");
            else connection = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
            if (System.getenv("SENDGRID_API_KEY") == null)
                sendGrid = new SendGrid("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");
            else sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    public static SendGrid getSendGrid() {
        return sendGrid;
    }

    public static void main(String[] args) throws SQLException {
        try {
            var userController = UserController.getInstance();
            var eventController = EventController.getInstance();
            var activityController = ActivityController.getInstance();
            var notificationRepository = NotificationRepository.getInstance();
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
                            path("logout", () -> {
                                post(userController::logout);
                            });
                            path("current", () -> {
                                get(userController::getCurrentUserView);
                                delete(userController::deleteCurrentUser);
                                path("events", () -> {
                                    get(userController::getParticipatingEventList);
                                    post(userController::joinEvent);
                                    path(":id", () -> {
                                        delete(userController::leaveEvent);
                                    });
                                });
                                path("notifications", () -> {
                                    get(userController::getNotificationList);
                                    path(":id", () -> {
                                        delete(userController::removeNotification);
                                    });
                                });
                                path("friends", () -> {
                                    get(userController::getFriendList);
                                    path(":id", () -> {
                                        post(userController::addFriend);
                                        delete(userController::deleteFriend);
                                    });
                                });
                                path("profile", () -> {
                                    get(userController::getCurrentUserProfile);
                                    put(userController::updateCurrentUserProfile);
                                });
                            });
                            path(":id", () -> {
                                get(userController::getProfileById);
                            });
                            path("search", () -> {
                                path("byEmail", () -> {
                                   get(userController::getProfileByEmail);
                                });
                            });
                        });
                        path("events", () -> {
                            post(eventController::create);
                            path(":id", () -> {
                                delete(eventController::delete);
                                get(eventController::getEventView);
                                put(eventController::update);
                                path("entryCode", () -> {
                                    get(eventController::getEntryCode);
                                    post(eventController::sendEntryCodeToOne);
                                });
                                path("organizer", () -> {
                                    get(eventController::getOrganizer);
                                });
                                path("activities", () -> {
                                    get(eventController::getActivityList);
                                    post(eventController::createActivity);
                                    path(":activityId", () -> {
                                        delete(eventController::deleteActivity);
                                    });
                                });
                                path("split", () -> {
                                    post(eventController::splitBill);
                                });
                            });
                        });
                        path("activities", () -> {
                            path(":id", () -> {
                                get(activityController::getActivityView);
                                put(activityController::update);
                                path("vote", () -> {
                                    put(activityController::vote);
                                });
                                path("boo", () -> {
                                    put(activityController::boo);
                                });
                                path("participate", () -> {
                                    post(activityController::join);
                                    delete(activityController::leave);
                                });
                            });
                        });
                    })
                    .exception(UserNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .exception(EventNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .exception(ActivityNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .exception(NotificationNotFoundException.class, (e, ctx) -> { ctx.status(404); })
                    .start(System.getenv("PORT") == null ? 4000 : Integer.parseInt(System.getenv("PORT")));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

    }
}
