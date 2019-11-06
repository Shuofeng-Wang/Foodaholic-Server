package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private Connection connection;

    public UserRepository(Connection connection) throws SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS items (identifier INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT)");
        statement.close();
    }

    public List<User> getAll() throws SQLException {
        var items = new ArrayList<User>();
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT identifier, description FROM items");
        while (result.next()) {
            items.add(
                new User(
                    result.getInt("identifier"),
                    result.getString("description")
                )
            );
        }
        result.close();
        statement.close();
        return items;
    }

    public User getOne(int identifier) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement("SELECT identifier, description FROM items WHERE identifier = ?");
        statement.setInt(1, identifier);
        var result = statement.executeQuery();
        try {
            if (result.next()) {
                return new User(
                        result.getInt("identifier"),
                        result.getString("description")
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

    public void create() throws SQLException {
        var statement = connection.createStatement();
        statement.execute("INSERT INTO items (description) VALUES (\"\")");
        statement.close();
    }

    public void update(User user) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement("UPDATE items SET description = ? WHERE identifier = ?");
        statement.setString(1, user.getDescription());
        statement.setInt(2, user.getIdentifier());
        try {
            if (statement.executeUpdate() == 0) throw new UserNotFoundException();
        }
        finally {
            statement.close();
        }
    }

    public void delete(User user) throws SQLException, UserNotFoundException {
        var statement = connection.prepareStatement("DELETE FROM items WHERE identifier = ?");
        statement.setInt(1, user.getIdentifier());
        try {
            if (statement.executeUpdate() == 0) throw new UserNotFoundException();
        }
        finally {
            statement.close();
        }
    }
}
