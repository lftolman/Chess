package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySQLGameTests {
    private static GameDataAccess gameDAO;

    @BeforeAll
    public static void init(){
        try {
            gameDAO = new MySQLGameDAO();
        } catch (ResponseException | DataAccessException e){
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp(){
        try{
            gameDAO.deleteGames();
            GameData game = new GameData(1,"whiteUsername","blackUsername","gameName",new ChessGame());
            gameDAO.createGame(game);
        } catch (DataAccessException e) {
            fail("Setup failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    void testCreateGamePositive(){
        try{
            GameData game = new GameData(2,"whiteUsername2","blackUsername2","gameName2",new ChessGame());
            gameDAO.createGame(game);
            assertNotNull(gameDAO.getGame(2), "Auth Token not added");
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testCreateGameNegative(){
        GameData game = new GameData(1,"whiteUsername","blackUsername","gameName",new ChessGame());
        Exception e = assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
        String message = e.getMessage();
        assertTrue(message.contains("SQLException"));
    }

}
