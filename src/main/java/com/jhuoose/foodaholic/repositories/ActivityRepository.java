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
        statement.execute("Create Table IF NOT exists activities (id SERIAL PRIMARY KEY , activityName TEXT, description TEXT, vote integer, money float, category TEXT, participantIdArray integer[])");
        statement.close();
    }

    public List<Activity> getAll() throws ActivityNotFoundException, SQLException {
        var activities = new ArrayList<Activity>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM activities");
        try {
            while(result.next()) {
                //Integer[] array = (Integer[])(result.getArray("participantIdArray")).getArray();//.getClass().getSimpleName());
                //for(Integer it :array){
                //    System.out.println(it);
                //}
                activities.add(
                        new Activity(
                                result.getInt("id"),
                                result.getString("activityName"),
                                result.getString("description"),
                                result.getInt("vote"),
                                result.getFloat("money"),
                                result.getString("category"),
                                Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray())
                        )
                );
            }
        } finally {
            statement.close();
            result.close();
            return activities;
        }
    }

    public Activity getOne(int id) throws ActivityNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT * FROM activities WHERE id = ?");
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
                        result.getInt("vote"),
                        result.getFloat("money"),
                        result.getString("category"),
                        Arrays.asList((Integer[])(result.getArray("participantIdArray")).getArray())
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
        //statement.execute("INSERT INTO activities (activityName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testActivityName', 'testDesc', 'test','test','test',100,'test','{10000, 10000, 10000, 10000}','{20000, 25000, 25000, 25000}');");
        //statement.close();
        var statement = connection.prepareStatement("INSERT INTO activities (activityName, description, vote, money, category, participantIdArray) VALUES ('testActivityName', 'testDesc', 10, 11.15, 'testCategory', ?);");
        var array = new Integer[]{1, 2, 3, 4};
        Array sqlArray = connection.createArrayOf("integer", array);
        statement.setArray(1, sqlArray);
        statement.execute();
        statement.close();
    }

    public void delete(Activity activity) throws SQLException {
        var statement = connection.prepareStatement("DELETE from activities WHERE id = ?");
        statement.setInt(1, activity.getId());
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }

    public void edit(Activity activity) throws SQLException, ActivityNotFoundException {
        var statement = connection.prepareStatement("UPDATE activities SET activityName = ?, description = ?, vote = ?, money = ?, category = ?, participantIdArray = ? WHERE id = ?");
        statement.setString(1, activity.getActivityName());
        statement.setString(2, activity.getDescription());
        statement.setInt(3, activity.getVote());
        statement.setFloat(4,activity.getMoney());
        statement.setString(5, activity.getCategory());
        statement.setArray(6, connection.createArrayOf("integer",activity.getParticipantIdList().toArray()));
        statement.setInt(7, activity.getId());
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        }
        finally {
            statement.close();
        }
    }
}
