package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.Event;

import  java.sql.Connection;
import java.sql.SQLException;

public class EventRepository{
    private  Connection connection;

    public EventRepository(Connection connection) throws SQLException{
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists events (identifier INTEGER PRIMARY KEY AUTOINCREMENT， eventName TEXT, startTime TEXT, endTime TEXT)");
        statement.close();
    }

    public Event getOne(int identifier) throws EventNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT identifier, eventName, startTime, endTime FROM events WHERE identifier = ?");
        statement.setInt(1, identifier);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                return new Event(
                        result.getInt("identifier"),
                        result.getString("eventName"),
                        result.getString("startTime"),
                        result.getString("endTime")
                );
            } else {
                throw new EventNotFoundException();
            }
        } finally {
            statement.close();
            result.close();
        }
    }

    public void create() throws SQLException{
        var statement = connection.createStatement();
        statement.execute("INSERT INTO events (eventName, startTime, endTime) VALUES (\"\", \"\", \"\")");
        statement.close();
    }

    public void delete(Event event) throws SQLException {
        var statement = connection.prepareStatement("DELETE from events WHERE identifier = ?");
        statement.setInt(1, event.getIdentifier());
        try {
            if(statement.executeUpdate() == 0) throw new EventNotFoundException();
        } catch (EventNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }
}
