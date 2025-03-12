package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;

import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

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
            GameData game = new GameData(1,null,"blackUsername","gameName",new ChessGame());
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
            assertNotNull(gameDAO.getGame(2), "Game Data not added");
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
        assertTrue(message.contains("Duplicate"));
    }

    @Test
    @Order(3)
    void testGetGamePositive(){
        try {
            GameData gameData = gameDAO.getGame(1);
            GameData expected = new GameData(1,null,"blackUsername","gameName",new ChessGame());
            assertEquals(expected.toString(),gameData.toString());
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testGetGameNegative(){
        try {
            GameData gameData = gameDAO.getGame(3);
            assertNull(gameData);
        } catch (Exception e){
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }


    @Test
    @Order(5)
    void testListGamesPositive(){
        try{
            GameData game1 = new GameData(1,null,"blackUsername","gameName",new ChessGame());
            GameData game2 = new GameData(2,"whiteUsername2","blackUsername2","gameName2",new ChessGame());
            gameDAO.createGame(game2);

            Collection<GameData> games = gameDAO.listGames();
            Collection<GameData> expected = new ArrayList<>();
            expected.add(game1);
            expected.add(game2);

            assertEquals(expected,games);
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    void testListGamesNegative(){
        try{
            gameDAO.deleteGames();
            assertEquals(new ArrayList<GameData>(),gameDAO.listGames());
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }


    @Test
    @Order(6)
    void testUpdateGamePositive(){
        try {
            GameData newGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", new ChessGame());
            gameDAO.updateGame(1, newGame);
            GameData game = gameDAO.getGame(1);
            assertEquals(newGame,game);
        } catch (DataAccessException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }

    }

    @Test
    @Order(7)
    void testUpdateGameNegative(){
        try {
            Collection<GameData> games = gameDAO.listGames();
            GameData newGame = new GameData(3, "whiteUsername", "blackUsername", "gameName", new ChessGame());
            gameDAO.updateGame(3, newGame);
            Collection<GameData> updatedGames = gameDAO.listGames();
            assertEquals(games,updatedGames);
        } catch (DataAccessException e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    @Order(8)
    void testDeleteGames(){
        try{
            assertDoesNotThrow(()->gameDAO.deleteGames());
        } catch (Exception e) {
            fail("Test failed due to unexpected Exception: " + e.getMessage());
        }
    }

}
