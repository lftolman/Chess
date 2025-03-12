package service;

import dataaccess.*;
import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

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
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

}
