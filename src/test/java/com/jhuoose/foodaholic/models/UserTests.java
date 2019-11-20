package com.jhuoose.foodaholic.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    @Test
    void testGettersAndSetters() {
        var user = new User("testUser", "testPassword");
        user.addFriend(1);
        user.addNotification(2);
        user.addParticipatingEvent(3);
        user.setId(4);
        user.setUserName("5");
        user.setPhone("6");
        user.setPhotoURL("7");
        assertEquals("testUser", user.getEmail());
        assertEquals("testPassword", user.getPassword());
        assertEquals(1, user.getFriendIdList().get(0));
        assertEquals(2, user.getNotificationIdList().get(0));
        assertEquals(3, user.getParticipatingEventIdList().get(0));
        assertEquals(4, user.getId());
        assertEquals("5", user.getUserName());
        assertEquals("6", user.getPhone());
        assertEquals("7", user.getPhotoURL());
        assertTrue(user.isFriend(1));
        assertTrue(user.isNotification(2));
        assertTrue(user.isParticipatingEvent(3));
        user.deleteFriend(1);
        user.deleteNotification(2);
        user.deleteParticipatingEvent(3);
        assertFalse(user.isFriend(1));
        assertFalse(user.isNotification(2));
        assertFalse(user.isParticipatingEvent(3));
    }
}
