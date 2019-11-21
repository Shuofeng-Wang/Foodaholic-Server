package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.Server;
import com.jhuoose.foodaholic.models.Event;
import io.javalin.core.validation.Validator;

import java.sql.Array;
import  java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventRepository{
    private static EventRepository ourInstance;

    static {
        try {
            ourInstance = new EventRepository(Server.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static EventRepository getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private EventRepository(Connection connection) throws SQLException{
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists events " +
                "(id SERIAL PRIMARY KEY, " +
                "eventName TEXT, " +
                "description TEXT, " +
                "location TEXT, " +
                "startTime TEXT, " +
                "endTime TEXT, " +
                "organizerId INTEGER, " +
                "theme TEXT, " +
                "participantIdArray integer[], " +
                "activityIdArray integer[])");
        statement.close();
    }

    public List<Event> getAll() throws EventNotFoundException, SQLException {
        var events = new ArrayList<Event>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM events");
        try {
            while(result.next()) {
                events.add(
                    new Event(
                            result.getInt("id"),
                            result.getString("eventName"),
                            result.getString("description"),
                            result.getString("location"),
                            result.getString("startTime"),
                            result.getString("endTime"),
                            result.getInt("organizerId"),
                            result.getString("theme"),
                            new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray())),
                            new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("activityIdArray")).getArray()))
                    )
                );
            }
            return events;
        } finally {
            statement.close();
            result.close();
        }
    }

    public Event getOne(int id) throws EventNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT * FROM events WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                //Integer[] array = (Integer[])(result.getArray("participantIdArray")).getArray();//.getClass().getSimpleName());
                //for(Integer it :array){
                //    System.out.println(it);
                //}
                return new Event(
                        result.getInt("id"),
                        result.getString("eventName"),
                        result.getString("description"),
                        result.getString("location"),
                        result.getString("startTime"),
                        result.getString("endTime"),
                        result.getInt("organizerId"),
                        result.getString("theme"),
                        new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray())),
                        new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("activityIdArray")).getArray()))
                );
            } else {
                throw new EventNotFoundException();
            }
        } finally {
            statement.close();
            result.close();
        }
    }

    public List<Event> getMultiple(List<Integer> idList) throws SQLException {
        var events = new ArrayList<Event>();
        var statement = connection.prepareStatement(
                "SELECT * FROM events WHERE id IN (SELECT(UNNEST(?::integer[])))");
        statement.setArray(1, connection.createArrayOf("integer", idList.toArray()));
        var result = statement.executeQuery();
        try {
            while(result.next()) {
                events.add(
                    new Event(
                            result.getInt("id"),
                            result.getString("eventName"),
                            result.getString("description"),
                            result.getString("location"),
                            result.getString("startTime"),
                            result.getString("endTime"),
                            result.getInt("organizerId"),
                            result.getString("theme"),
                            new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray())),
                            new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("activityIdArray")).getArray()))
                    )
                );
            }
            return events;
        } finally {
            statement.close();
            result.close();
        }
    }

    public int create(Event event) throws SQLException{
        var statement = connection.prepareStatement(
                "INSERT INTO events (" +
                        "eventName, description, location, startTime, endTime, " +
                        "organizerId, theme, participantIdArray, activityIdArray) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, '{}') " +
                        "RETURNING id");
        statement.setString(1, event.getEventName());
        statement.setString(2, event.getDescription());
        statement.setString(3, event.getLocation());
        statement.setString(4, event.getStartTime());
        statement.setString(5, event.getEndTime());
        statement.setInt(6, event.getOrganizerId());
        statement.setString(7, event.getStartTime());
        statement.setArray(8, connection.createArrayOf("integer", event.getParticipantIdList().toArray()));
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

    public void delete(Event event) throws SQLException, EventNotFoundException {
        delete(event.getId());
    }

    public void delete(int id) throws SQLException, EventNotFoundException {
        var statement = connection.prepareStatement("DELETE from events WHERE id = ?");
        statement.setInt(1, id);
        try {
            if(statement.executeUpdate() == 0) throw new EventNotFoundException();
        } finally{
            statement.close();
        }
    }

    public void update(Event event) throws SQLException, EventNotFoundException {
        var statement = connection.prepareStatement("UPDATE events SET eventName = ?, description = ?, location = ?, startTime = ?, endTime = ?, organizerId = ?, theme = ?, participantIdArray = ?, ActivityIdArray = ? WHERE id = ?");
        statement.setString(1, event.getEventName());
        statement.setString(2, event.getDescription());
        statement.setString(3, event.getLocation());
        statement.setString(4,event.getStartTime());
        statement.setString(5, event.getEndTime());
        statement.setInt(6,event.getOrganizerId());
        statement.setString(7,event.getTheme());
        statement.setArray(8, connection.createArrayOf("integer",event.getParticipantIdList().toArray()));
        statement.setArray(9, connection.createArrayOf("integer",event.getActivityIdList().toArray()));
        statement.setInt(10, event.getId());
        try {
            if(statement.executeUpdate() == 0) throw new EventNotFoundException();
        }
        finally {
            statement.close();
        }
    }
}
