package service;

import dataaccess.*;
import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserTests {

    private static UserDataAccess userDAO;

    @BeforeAll
    public static void init(){
        try {
            userDAO = new MySQLUserDAO();
        } catch (Exception e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp(){
        try{
            userDAO.deleteUserData();
            UserData user = new UserData("username", "password", "email@email.com");
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testInsertUserPositive(){
        try {
            UserData user = new UserData("username1", "password1", "email1@email.com");
            userDAO.insertUser(user);
            UserData givenUser = userDAO.getUser("username1");
            assertEquals(user.toString(),givenUser.toString());
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testInsertUserNegative(){
        UserData user = new UserData("username", "password1", "email1@email.com");
        Exception e = assertThrows(DataAccessException.class, () ->userDAO.insertUser(user));
        String message = e.getMessage();
        assertTrue(message.contains("Duplicate"));
    }

    @Test
    @Order(3)
    void testGetUserPositive(){
        try{
            UserData userData = userDAO.getUser("username");
            UserData expected = new UserData("username", "password", "email@email.com");
            assertEquals(expected.toString(),userData.toString());
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testGetUserNegative(){
        try{
            UserData userData = userDAO.getUser("usernameWrong");
            assertNull(userData);
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void testDeleteUserData(){
        assertDoesNotThrow(()->userDAO.deleteUserData());
    }

}
