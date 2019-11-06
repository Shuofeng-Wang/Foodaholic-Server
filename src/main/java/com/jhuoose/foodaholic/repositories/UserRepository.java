package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRepository {
    private Connection connection;

    public UserRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS users (email TEXT PRIMARY KEY, password TEXT)");
        statement.close();
    }

    public User getOne(String login) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement("SELECT email, password FROM users WHERE email = ?");
        statement.setString(1, login);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                return new User(
                        result.getString("email"),
                        result.getString("password")
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

    public void create(User user) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.execute();
        statement.close();
    }
}
