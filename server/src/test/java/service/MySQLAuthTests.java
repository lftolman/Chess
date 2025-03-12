package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthTests {
    private static AuthDataAccess authDAO;

    @BeforeAll
    public static void init(){
        try {
            authDAO = new MySQLAuthDAO();
        } catch (ResponseException | DataAccessException e){
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp(){
        try{
            authDAO.deleteAuthData();
            AuthData auth = new AuthData("authToken","username");
            authDAO.createAuth(auth);
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testCreateAuthPositive(){
        try{
            AuthData authData = new AuthData("authToken1","username");
            authDAO.createAuth(authData);
            assertNotNull(authDAO.getAuth("authToken1"), "Auth Token not added");
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testCreateAuthNegative(){
        AuthData authData = new AuthData("authToken","username2");
        Exception e = assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));
        String message = e.getMessage();
        assertTrue(message.contains("SQLException"));
    }

    @Test
    @Order(3)
    void testGetAuthPositive(){
        try {
            AuthData authData = authDAO.getAuth("authToken");
            AuthData expected = new AuthData("authToken", "username");
            assertEquals(expected.toString(),authData.toString());
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testGetAuthNegative(){
        try {
            AuthData authData = authDAO.getAuth("falseAuthToken");
            assertNull(authData);
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    void testDeleteAuthPositive(){
        try {
            AuthData authData = new AuthData("authToken2","username2");
            authDAO.createAuth(authData);
            authDAO.deleteAuth("authToken2");
            AuthData authData1 = authDAO.getAuth("authToken2");
            assertNull(authData1);
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    void testDeleteAuthDataPositive(){
        try{
            assertDoesNotThrow(()->authDAO.deleteAuthData());
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    }
