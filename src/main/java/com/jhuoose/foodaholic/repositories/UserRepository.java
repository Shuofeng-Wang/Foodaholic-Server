package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.Server;
import com.jhuoose.foodaholic.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository {
    private static UserRepository ourInstance;

    static {
        try {
            ourInstance = new UserRepository(Server.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static UserRepository getInstance() {
        return ourInstance;
    }

    private Connection connection;

    private UserRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "email TEXT UNIQUE, " +
                        "password TEXT, " +
                        "userName TEXT, " +
                        "photoURL TEXT, " +
                        "phone TEXT, " +
                        "friendIdList INTEGER[], " +
                        "participatingEventIdList INTEGER[], " +
                        "notificationIdList INTEGER[])");
        statement.close();
    }

    public int create(User user) throws SQLException {
        var statement = connection.prepareStatement(
                "INSERT INTO users (email, password, userName, photoURL, phone, " +
                        "friendIdList, participatingEventIdList, notificationIdList) " +
                        "VALUES (?, ?, ?, ?, ?, '{}', '{}', '{}') " +
                        "RETURNING id");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getUserName());
        statement.setString(4, user.getPhotoURL());
        statement.setString(5, user.getPhone());
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

    public void delete(User user) throws SQLException, UserNotFoundException {
        delete(user.getId());
    }

    public void delete(int id) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement(
                "DELETE FROM users WHERE id = ?");
        statement.setInt(1, id);
        try {
            if (statement.executeUpdate() == 0) throw new UserNotFoundException();
        }
        finally {
            statement.close();
        }
    }

    public void update(User user) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement(
                "UPDATE users SET " +
                        "email = ?, " +
                        "password = ?, " +
                        "userName = ?, " +
                        "photoURL = ?, " +
                        "phone = ?, " +
                        "friendIdList = ?, " +
                        "participatingEventIdList = ?, " +
                        "notificationIdList = ?" +
                        "WHERE id = ?");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getUserName());
        statement.setString(4, user.getPhotoURL());
        statement.setString(5, user.getPhone());
        statement.setArray(6, connection.createArrayOf("integer", user.getFriendIdList().toArray()));
        statement.setArray(7, connection.createArrayOf("integer", user.getParticipatingEventIdList().toArray()));
        statement.setArray(8, connection.createArrayOf("integer", user.getNotificationIdList().toArray()));
        statement.setInt(9, user.getId());
        try {
            if (statement.executeUpdate() == 0) throw new UserNotFoundException();
        }
        finally {
            statement.close();
        }
    }

    public User getOne(int id) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement(
                "SELECT * FROM users WHERE id = ?");
        statement.setInt(1, id);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                return new User(
                        result.getInt("id"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("userName"),
                        result.getString("photoURL"),
                        result.getString("phone"),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("friendIdList").getArray())),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("participatingEventIdList").getArray())),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("notificationIdList").getArray()))
                );
            } else {
                throw new UserNotFoundException();
            }
        }
        finally {
            statement.close();
            result.close();
        }
    }

    public User getOne(String email) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement(
                "SELECT * FROM users WHERE email = ?");
        statement.setString(1, email);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                return new User(
                        result.getInt("id"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getString("userName"),
                        result.getString("photoURL"),
                        result.getString("phone"),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("friendIdList").getArray())),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("participatingEventIdList").getArray())),
                        new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("notificationIdList").getArray()))
                );
            } else {
                throw new UserNotFoundException();
            }
        }
        finally {
            statement.close();
            result.close();
        }
    }

    public List<User> getMultiple(List<Integer> idList) throws SQLException {
        var users = new ArrayList<User>();
        var statement = connection.prepareStatement(
                "SELECT * FROM users WHERE id IN (SELECT(UNNEST(?::integer[])))");
        statement.setArray(1, connection.createArrayOf("integer", idList.toArray()));
        var result = statement.executeQuery();
        try {
            while(result.next()) {
                users.add(
                    new User(
                            result.getInt("id"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getString("userName"),
                            result.getString("photoURL"),
                            result.getString("phone"),
                            new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("friendIdList").getArray())),
                            new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("participatingEventIdList").getArray())),
                            new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("notificationIdList").getArray()))
                    )
                );
            }
            return users;
        } finally {
            statement.close();
            result.close();
        }
    }

    public List<User> getMultipleByEmail(List<String> emailList) throws SQLException {
        var users = new ArrayList<User>();
        var statement = connection.prepareStatement(
                "SELECT * FROM users WHERE email IN (SELECT(UNNEST(?::text[])))");
        statement.setArray(1, connection.createArrayOf("text", emailList.toArray()));
        var result = statement.executeQuery();
        try {
            while(result.next()) {
                users.add(
                        new User(
                                result.getInt("id"),
                                result.getString("email"),
                                result.getString("password"),
                                result.getString("userName"),
                                result.getString("photoURL"),
                                result.getString("phone"),
                                new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("friendIdList").getArray())),
                                new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("participatingEventIdList").getArray())),
                                new ArrayList<Integer>(Arrays.asList((Integer[]) result.getArray("notificationIdList").getArray()))
                        )
                );
            }
            return users;
        } finally {
            statement.close();
            result.close();
        }
    }

    public boolean isEmailExist(String email) throws SQLException {
        var statement = connection.prepareStatement(
                "SELECT id FROM users WHERE email = ?"
        );
        statement.setString(1, email);
        var result = statement.executeQuery();
        try {
            return result.next();
        }
        finally {
            statement.close();
            result.close();
        }
    }
}
