package service;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyTests {
    private static GameDataAccess gameDAO;
    private static UserDataAccess userDAO;

    private static GameService gameService;
    private static UserService userService;
    public static ClearService clearService;

    private static String authToken;

    @BeforeAll
    public static void init(){
        gameDAO = new GameDAO();
        AuthDataAccess authDAO = new AuthDAO();
        userDAO = new UserDAO();

        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(gameDAO, authDAO,userDAO);

    }

    @BeforeEach
    public void oneUser(){
        try {
            ClearAppRequest clearAppRequest = new ClearAppRequest();
            clearService.clearApplication(clearAppRequest);
            RegisterRequest registerRequest = new RegisterRequest("username","password","email@email.com");
            RegisterResult registerResult = userService.register(registerRequest);
            authToken = registerResult.authToken();
        } catch (ResponseException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testRegisterPositive(){
        try{
            RegisterRequest registerRequest = new RegisterRequest("username1","password","email@email.com");
            RegisterResult registerResult = userService.register(registerRequest);
            assertNotNull(userDAO.getUser(registerResult.username()), "User not registered");
        }
        catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testRegisterNegative(){
        RegisterRequest registerRequest = new RegisterRequest("username","password","email@email.com");
        Exception exception = assertThrows(ResponseException.class, () ->userService.register(registerRequest));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    @Order(3)
    void testLoginPositive(){
        try {
            LoginRequest loginRequest = new LoginRequest("username","password");
            LoginResult loginResult = userService.login(loginRequest);
            assertNotNull(loginResult.authToken(), "No authentication token associated with user");
        } catch (ResponseException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }


    }

    @Test
    @Order(4)
    void testLoginNegative(){
        LoginRequest loginRequest = new LoginRequest("username","wrongPassword");
        Exception exception = assertThrows(ResponseException.class, () ->userService.login(loginRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    void testLogoutPositive(){
        try{
            LoginRequest loginRequest = new LoginRequest("username","password");
            LoginResult loginResult = userService.login(loginRequest);
            LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
            assertDoesNotThrow(() -> userService.logout(logoutRequest));
        } catch (ResponseException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    void testLogoutNegative(){
        try{
            LoginRequest loginRequest = new LoginRequest("username","password");
            LoginResult loginResult = userService.login(loginRequest);
            LogoutRequest logoutRequest = new LogoutRequest(null);
            Exception exception = assertThrows(ResponseException.class, () ->userService.logout(logoutRequest));
            assertEquals("Error: unauthorized", exception.getMessage());
        } catch (ResponseException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    void testCreateGamePositive(){
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken,"New Game");
        assertDoesNotThrow(() ->gameService.createGame(createGameRequest));
    }

    @Test
    @Order(8)
    void testCreateGameNegative() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null, "New Game");
        Exception exception = assertThrows(ResponseException.class, () -> gameService.createGame(createGameRequest));
        assertEquals("Error: bad request", exception.getMessage(), "Wrong error message");
    }

    @Test
    @Order(9)
    void testJoinGamePositive(){
        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "New Game");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", createGameResult.gameID());
            gameService.joinGame(joinGameRequest);
            assertNotNull(gameDAO.getGame(createGameResult.gameID()), "Game with gameID does not exist");
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());}
    }

    @Test
    @Order(10)
    void testJoinGameNegative(){
        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "New Game");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", 0);
            Exception exception = assertThrows(ResponseException.class, () ->gameService.joinGame(joinGameRequest));
            assertEquals("Error: bad request", exception.getMessage());
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());}
    }

    @Test
    @Order(11)
    void testListGamesPositive(){
        try {
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
            assertTrue(gameService.listGames(listGamesRequest).games().isEmpty(), "Games list should be empty");
        } catch (ResponseException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(12)
    void testListGamesNegative(){
        ListGamesRequest listGamesRequest = new ListGamesRequest("authToken");
        Exception exception = assertThrows(ResponseException.class, ()-> gameService.listGames(listGamesRequest));
        assertEquals("Error: unauthorized", exception.getMessage());
    }


    @Test
    @Order(13)
    void testClear(){
        ClearAppRequest clearAppRequest = new ClearAppRequest();
        assertDoesNotThrow(() -> clearService.clearApplication(clearAppRequest), "ClearApplication threw an error");
    }


}
