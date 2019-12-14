package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.Server;
import com.jhuoose.foodaholic.models.Activity;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityRepository {
    private static ActivityRepository ourInstance;

    static {
        try {
            ourInstance = new ActivityRepository(Server.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ActivityRepository getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private ActivityRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists activities (" +
                "id SERIAL PRIMARY KEY , " +
                "activityName TEXT, " +
                "description TEXT, " +
                "vote integer, " +
                "money float, " +
                "category TEXT, " +
                "eventId integer, " +
                "voteIdList integer[], " +
                "booIdList integer[], " +
                "participantIdList integer[], " +
                "payerId integer)");
        statement.close();
    }

    public List<Activity> getAll() throws ActivityNotFoundException, SQLException {
        var activities = new ArrayList<Activity>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM activities");
        try {
            while(result.next()) {
                var activity = new Activity();
                activity.setId(result.getInt("id"));
                activity.setActivityName(result.getString("activityName"));
                activity.setDescription(result.getString("description"));
                activity.setVote(result.getInt("vote"));
                activity.setMoney(result.getDouble("money"));
                activity.setCategory(result.getString("category"));
                activity.setEventId(result.getInt("eventId"));
                activity.setVoteIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("voteIdList")).getArray())));
                activity.setBooIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("booIdList")).getArray())));
                activity.setParticipantIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdList")).getArray())));
                activity.setPayerId(result.getInt("payerId"));
                activities.add(activity);
            }
            return activities;
        } finally {
            statement.close();
            result.close();
        }
    }

    public Activity getOne(int id) throws ActivityNotFoundException, SQLException {
        var statement = connection.prepareStatement("SELECT * FROM activities WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                var activity = new Activity();
                activity.setId(result.getInt("id"));
                activity.setActivityName(result.getString("activityName"));
                activity.setDescription(result.getString("description"));
                activity.setVote(result.getInt("vote"));
                activity.setMoney(result.getDouble("money"));
                activity.setCategory(result.getString("category"));
                activity.setEventId(result.getInt("eventId"));
                activity.setVoteIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("voteIdList")).getArray())));
                activity.setBooIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("booIdList")).getArray())));
                activity.setParticipantIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdList")).getArray())));
                activity.setPayerId(result.getInt("payerId"));
                return activity;
            } else {
                throw new ActivityNotFoundException();
            }
        } finally {
            statement.close();
            result.close();
        }
    }

    public List<Activity> getMultiple(List<Integer> idList) throws SQLException {
        var activities = new ArrayList<Activity>();
        var statement = connection.prepareStatement(
                "SELECT * FROM activities WHERE id IN (SELECT(UNNEST(?::integer[])))");
        statement.setArray(1, connection.createArrayOf("integer", idList.toArray()));
        var result = statement.executeQuery();
        try {
            while(result.next()) {
                var activity = new Activity();
                activity.setId(result.getInt("id"));
                activity.setActivityName(result.getString("activityName"));
                activity.setDescription(result.getString("description"));
                activity.setVote(result.getInt("vote"));
                activity.setMoney(result.getDouble("money"));
                activity.setCategory(result.getString("category"));
                activity.setEventId(result.getInt("eventId"));
                activity.setVoteIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("voteIdList")).getArray())));
                activity.setBooIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("booIdList")).getArray())));
                activity.setParticipantIdList(new ArrayList<Integer>(Arrays.asList((Integer[])(result.getArray("participantIdList")).getArray())));
                activity.setPayerId(result.getInt("payerId"));
                activities.add(activity);
            }
            return activities;
        } finally {
            statement.close();
            result.close();
        }
    }

    public int create(Activity activity) throws SQLException{
        //var statement = connection.createStatement();
        //statement.execute("INSERT INTO activities (activityName, description, location, startTime, endTime, organizerId, theme, participantIdArray, activityIdArray) VALUES ('testActivityName', 'testDesc', 'test','test','test',100,'test','{10000, 10000, 10000, 10000}','{20000, 25000, 25000, 25000}');");
        //statement.close();
        var statement = connection.prepareStatement("INSERT INTO activities (" +
                "activityName, description, vote, money, category, " +
                "eventId, voteIdList, booIdList, participantIdList, payerId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING id");
        statement.setString(1, activity.getActivityName());
        statement.setString(2, activity.getDescription());
        statement.setInt(3, activity.getVote());
        statement.setDouble(4, activity.getMoney());
        statement.setString(5, activity.getCategory());
        statement.setInt(6, activity.getEventId());
        statement.setArray(7, connection.createArrayOf("integer",activity.getVoteIdList().toArray()));
        statement.setArray(8, connection.createArrayOf("integer",activity.getBooIdList().toArray()));
        statement.setArray(9, connection.createArrayOf("integer",activity.getParticipantIdList().toArray()));
        statement.setInt(10, activity.getPayerId());
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

    public void delete(Activity activity) throws SQLException {
        delete(activity.getId());
    }

    public void delete(int id) throws SQLException {
        var statement = connection.prepareStatement("DELETE from activities WHERE id = ?");
        statement.setInt(1, id);
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } finally{
            statement.close();
        }
    }

    public void update(Activity activity) throws SQLException, ActivityNotFoundException {
        var statement = connection.prepareStatement("UPDATE activities SET " +
                "activityName = ?, description = ?, vote = ?, money = ?, category = ?, " +
                "eventId = ?, voteIdList = ?, booIdList = ?, participantIdList = ?, payerId = ? " +
                "WHERE id = ?");
        statement.setString(1, activity.getActivityName());
        statement.setString(2, activity.getDescription());
        statement.setInt(3, activity.getVote());
        statement.setDouble(4, activity.getMoney());
        statement.setString(5, activity.getCategory());
        statement.setInt(6, activity.getEventId());
        statement.setArray(7, connection.createArrayOf("integer",activity.getVoteIdList().toArray()));
        statement.setArray(8, connection.createArrayOf("integer",activity.getBooIdList().toArray()));
        statement.setArray(9, connection.createArrayOf("integer",activity.getParticipantIdList().toArray()));
        statement.setInt(10, activity.getPayerId());
        statement.setInt(11, activity.getId());
        try {
            if(statement.executeUpdate() == 0) throw new ActivityNotFoundException();
        }
        finally {
            statement.close();
        }
    }
}
