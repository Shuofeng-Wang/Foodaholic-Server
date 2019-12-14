package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.Server;
import com.jhuoose.foodaholic.models.Notification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationRepository {
    private static NotificationRepository ourInstance;

    static {
        try {
            ourInstance = new NotificationRepository(Server.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static NotificationRepository getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private NotificationRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists notifications (" +
                "id SERIAL PRIMARY KEY, " +
                "time TIMESTAMP WITH TIME ZONE, " +
                "category TEXT, " +
                "content TEXT)");
        statement.close();
    }

    public List<Notification> getAll() throws SQLException {
        var notifications = new ArrayList<Notification>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM notifications");
        try {
            while(result.next()) {
                var notification = new Notification();
                notification.setId(result.getInt("id"));
                notification.setTime(result.getTimestamp("time"));
                notification.setCategory(result.getString("category"));
                notification.setContent(result.getString("content"));
                notifications.add(notification);
            }
            return notifications;
        } finally {
            statement.close();
            result.close();
        }
    }

    public Notification getOne(int id) throws NotificationNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT * FROM notifications WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                var notification = new Notification();
                notification.setId(result.getInt("id"));
                notification.setTime(result.getTimestamp("time"));
                notification.setCategory(result.getString("category"));
                notification.setContent(result.getString("content"));
                return notification;
            } else {
                throw new NotificationNotFoundException();
            }
        } finally {
            statement.close();
            result.close();
        }
    }

    public List<Notification> getMultiple(List<Integer> idList) throws SQLException {
        var notifications = new ArrayList<Notification>();
        var statement = connection.prepareStatement(
                "SELECT * FROM notifications WHERE id IN (SELECT(UNNEST(?::integer[])))");
        statement.setArray(1, connection.createArrayOf("integer", idList.toArray()));
        var result = statement.executeQuery();
        try {
            while(result.next()) {
                var notification = new Notification();
                notification.setId(result.getInt("id"));
                notification.setTime(result.getTimestamp("time"));
                notification.setCategory(result.getString("category"));
                notification.setContent(result.getString("content"));
                notifications.add(notification);
            }
            return notifications;
        } finally {
            statement.close();
            result.close();
        }
    }

    public int create(Notification notification) throws SQLException{
        var statement = connection.prepareStatement("INSERT INTO notifications (" +
                "time, category, content) " +
                "VALUES (?, ?, ?) " +
                "RETURNING id");
        statement.setTimestamp(1, (Timestamp) notification.getTime());
        statement.setString(2, notification.getCategory());
        statement.setString(3, notification.getContent());
        var result = statement.executeQuery();
        try {
            result.next();
            return result.getInt("id");
        }
        finally {
            statement.close();
            result.close();
        }
    }

    public void delete(Notification notification) throws SQLException {
        delete(notification.getId());
    }

    public void delete(int id) throws SQLException {
        var statement = connection.prepareStatement("DELETE from notifications WHERE id = ?");
        statement.setInt(1, id);
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }

    public void update(Notification notification) throws SQLException, NotificationNotFoundException {
        var statement = connection.prepareStatement("UPDATE notifications SET time = ?, category = ?, content = ? WHERE id = ?");
        statement.setTimestamp(1, (Timestamp) notification.getTime());
        statement.setString(2, notification.getCategory());
        statement.setString(3, notification.getContent());
        statement.setInt(4, notification.getId());
        try {
            if(statement.executeUpdate() == 0) throw new NotificationNotFoundException();
        }
        finally {
            statement.close();
        }
    }
}
