package com.jhuoose.foodaholic.repositories;

import com.jhuoose.foodaholic.models.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTests {
    Connection connection;
    UserRepository userRepository;

    UserRepositoryTests() {
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodaholic", "postgres", "postgres");
            userRepository = new UserRepository(connection);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    @Test
    void testCreateDeleteUpdateGetOne() throws SQLException, UserNotFoundException {
        var testUser = new User("test@test.com", "abcdef");
        userRepository.create(testUser);
        var resultUser = userRepository.getOne("test@test.com");
        assertEquals(testUser.getEmail(), resultUser.getEmail());
        assertEquals(testUser.getPassword(), resultUser.getPassword());
        resultUser.setPhotoURL("testURL");
        resultUser.setPhone("testPhone");
        resultUser.setUserName("testName");
        userRepository.update(resultUser);
        assertEquals(resultUser.getPhotoURL(), userRepository.getOne(resultUser.getId()).getPhotoURL());
        assertEquals(resultUser.getPhone(), userRepository.getOne(resultUser.getId()).getPhone());
        assertEquals(resultUser.getUserName(), userRepository.getOne(resultUser.getId()).getUserName());
        userRepository.delete(resultUser);
        assertThrows(UserNotFoundException.class, () -> { userRepository.getOne(resultUser.getId()); });
    }

    @Test
    void testIsEmailExist() throws SQLException, UserNotFoundException {
        var testUser = new User("test@test.com", "abcdef");
        if (userRepository.isEmailExist(testUser.getEmail())) {
            userRepository.delete(userRepository.getOne(testUser.getEmail()));
        }
        userRepository.create(testUser);
        assertTrue(userRepository.isEmailExist(testUser.getEmail()));
        userRepository.delete(userRepository.getOne(testUser.getEmail()));
        assertFalse(userRepository.isEmailExist(testUser.getEmail()));
    }
}
