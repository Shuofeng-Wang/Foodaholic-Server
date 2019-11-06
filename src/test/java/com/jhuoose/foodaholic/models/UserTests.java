package com.jhuoose.foodaholic.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    @Test
    void testGettersAndSetters() {
        var item = new User(5, "This is a test");
        assertEquals(5, item.getIdentifier());
        assertEquals("This is a test", item.getDescription());
    }
}
