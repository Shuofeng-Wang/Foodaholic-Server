package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.Event;
import io.javalin.core.validation.Validator;

import java.sql.Array;
import  java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventRepository{
    private  Connection connection;

    public EventRepository(Connection connection) throws SQLException{
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists events (id SERIAL PRIMARY KEY , eventName TEXT, description TEXT, location TEXT, startTime TEXT, endTime TEXT, organizerId INTEGER, theme TEXT, participantIdArray integer[], activityIdArray integer[])");
        statement.close();
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
                        Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray()),
                        Arrays.asList((Integer[])(result.getArray("activityIdArray")).getArray())
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
        //var statement = connection.createStatement();
        //statement.execute("INSERT INTO events (eventName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testEventName', 'testDesc', 'test','test','test',100,'test','{10000, 10000, 10000, 10000}','{20000, 25000, 25000, 25000}');");
        //statement.close();
        var statement = connection.prepareStatement("INSERT INTO events (eventName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testEventName', 'testDesc', 'test','test','test',100,'test',?,'{20000, 25000, 25000, 25000}');");
        var array = new Integer[]{1, 2, 3, 4};
        Array sqlArray = connection.createArrayOf("integer", array);
        statement.setArray(1, sqlArray);
        statement.executeQuery();
        statement.close();
    }

    public void delete(Event event) throws SQLException {
        var statement = connection.prepareStatement("DELETE from events WHERE id = ?");
        statement.setInt(1, event.getId());
        try {
            if(statement.executeUpdate() == 0) throw new EventNotFoundException();
        } catch (EventNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }


}
