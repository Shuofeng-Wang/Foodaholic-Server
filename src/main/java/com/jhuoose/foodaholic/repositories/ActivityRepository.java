package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.Activity;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityRepository {
    private Connection connection;

    public ActivityRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists activitys (id SERIAL PRIMARY KEY , activityName TEXT, description TEXT, location TEXT, startTime TEXT, endTime TEXT, organizerId INTEGER, theme TEXT, participantIdArray integer[], activityIdArray integer[])");
        statement.close();
    }

    public List<Activity> getAll() throws ActivityNotFoundException, SQLException {
        var activitys = new ArrayList<Activity>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM activitys");
        try {
            while(result.next()) {
                //Integer[] array = (Integer[])(result.getArray("participantIdArray")).getArray();//.getClass().getSimpleName());
                //for(Integer it :array){
                //    System.out.println(it);
                //}
                activitys.add(
                        new Activity(
                                result.getInt("id"),
                                result.getString("activityName"),
                                result.getString("description"),
                                result.getString("location"),
                                result.getString("startTime"),
                                result.getString("endTime"),
                                result.getInt("organizerId"),
                                result.getString("theme"),
                                Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray()),
                                Arrays.asList((Integer[])(result.getArray("activityIdArray")).getArray())
                        )
                );
            }
        } finally {
            statement.close();
            result.close();
            return activitys;
        }
    }

    public Activity getOne(int id) throws ActivityNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT * FROM activitys WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                //Integer[] array = (Integer[])(result.getArray("participantIdArray")).getArray();//.getClass().getSimpleName());
                //for(Integer it :array){
                //    System.out.println(it);
                //}
                return new Activity(
                        result.getInt("id"),
                        result.getString("activityName"),
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
                throw new ActivityNotFoundException();
            }
        } finally {
            statement.close();
            result.close();
        }
    }

    public void create() throws SQLException{
        //var statement = connection.createStatement();
        //statement.execute("INSERT INTO activitys (activityName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testActivityName', 'testDesc', 'test','test','test',100,'test','{10000, 10000, 10000, 10000}','{20000, 25000, 25000, 25000}');");
        //statement.close();
        var statement = connection.prepareStatement("INSERT INTO activitys (activityName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testActivityName', 'testDesc', 'test','test','test',100,'test',?,'{20000, 25000, 25000, 25000}');");
        var array = new Integer[]{1, 2, 3, 4};
        Array sqlArray = connection.createArrayOf("integer", array);
        statement.setArray(1, sqlArray);
        statement.execute();
        statement.close();
    }

    public void delete(Activity activity) throws SQLException {
        var statement = connection.prepareStatement("DELETE from activitys WHERE id = ?");
        statement.setInt(1, activity.getId());
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }
}
