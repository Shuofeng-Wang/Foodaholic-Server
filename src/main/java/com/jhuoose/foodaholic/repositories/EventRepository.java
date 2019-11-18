package com.jhuoose.foodaholic.repositories;

import  java.sql.Connection;
import java.sql.SQLException;

public class EventRepository{
    private  Connection connection;

    public EventRepository(Connection connection) throws SQLException throes SQLException {
        this.connection = connection;
        var statement = connection.createStatement();
        statement.execute("Create Table IF NOT exists events (identifier INTEGER PRIMARY KEY AUTOINCREMENT， eventName TEXT, startTime TEXT, endTime TEXT");
        statement.close();
    }


}
