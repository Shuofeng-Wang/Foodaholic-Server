package com.jhuoose.foodaholic.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    @Test
    void testGettersAndSetters() {
        var user = new User("testUser", "testPassword");
        assertEquals("testUser", user.getEmail());
        assertEquals("testPassword", user.getPassword());
    }
}
