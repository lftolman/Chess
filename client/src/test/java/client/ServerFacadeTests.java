package client;

import exception.ResponseException;
import model.*;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp(){
        try{
            facade.clearApp();
            RegisterRequest registerRequest =
                    new RegisterRequest("username", "password", "player@email.com");
            facade.register(registerRequest);
        } catch (ResponseException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void registerPositive() {
        try {
            RegisterRequest registerRequest =
                    new RegisterRequest("player1", "password1", "player1@email.com");
            RegisterResult registerResult = facade.register(registerRequest);
            assertNotNull(registerResult.authToken(), "no associated authToken");
        }catch (ResponseException e){
            fail("Register failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void registerNegative() {
        RegisterRequest registerRequest =
                new RegisterRequest("username", "password1", "player1@email.com");
        Exception exception = assertThrows(ResponseException.class, ()->facade.register(registerRequest));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    @Order(3)
    void loginPositive(){
        try {
            LoginRequest loginRequest = new LoginRequest("username", "password");
            LoginResult loginResult = facade.login(loginRequest);
            assertNotNull(loginResult.authToken(), "no associated authToken");
        } catch (ResponseException e) {
            fail("Login failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void loginNegative(){
        LoginRequest loginRequest = new LoginRequest("username", "wrongPassword");
        Exception exception = assertThrows(ResponseException.class, ()->facade.login(loginRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    void logoutPositive(){
        try {
            LoginRequest loginRequest = new LoginRequest("username", "password");
            facade.login(loginRequest);
        } catch (ResponseException e) {
            fail("Logout failed due to unexpected Exception: " + e.getMessage());
        }
        assertDoesNotThrow(()->facade.logout());
    }

    @Test
    @Order(6)
    void logoutNegative(){
        Exception exception = assertThrows(ResponseException.class,() ->facade.logout());
        assertEquals("Error: unauthorized", exception.getMessage());
    }


    @Test
    @Order(7)
    void createPositive(){
        try {
            LoginRequest loginRequest = new LoginRequest("username", "password");
            facade.login(loginRequest);
        } catch (ResponseException e) {
            fail("create failed due to unexpected Exception: " + e.getMessage());
        }
        assertDoesNotThrow(()->facade.createGame("game"));
    }

    @Test
    @Order(8)
    void createNegative(){
        Exception exception = assertThrows(ResponseException.class,()->facade.createGame("game"));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(9)
    void joinPositive(){
        try{
            LoginRequest loginRequest = new LoginRequest("username", "password");
            facade.login(loginRequest);
            facade.createGame("game");
        } catch (ResponseException e) {
            fail("Join failed due to unexpected Exception: " + e.getMessage());
        }
        assertDoesNotThrow(()->facade.join(1,"WHITE"));
    }

    @Test
    @Order(10)
    void joinNegative(){
        LoginRequest loginRequest = new LoginRequest("username", "password");
        try {
            facade.login(loginRequest);
        } catch (ResponseException e) {
            fail("Join failed due to unexpected Exception: " + e.getMessage());
        }
        Exception exception = assertThrows(ResponseException.class,()->facade.join(0,"WHITE"));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(11)
    void listPositive(){
        try{
            LoginRequest loginRequest = new LoginRequest("username", "password");
            facade.login(loginRequest);
            ListGamesResult listGamesResult = facade.listGames();
            assertTrue(listGamesResult.games().isEmpty());
        } catch (ResponseException e) {
            fail("List games failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(12)
    void listNegative(){
        Exception exception = assertThrows(ResponseException.class, ()-> facade.listGames());
        assertEquals("Error: unauthorized", exception.getMessage());
    }



    @Test
    @Order(13)
    void testClear(){
        assertDoesNotThrow(()->facade.clearApp());
    }



}
