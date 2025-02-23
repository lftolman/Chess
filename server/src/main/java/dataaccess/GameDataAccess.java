package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess{
    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(String gameID) throws DataAccessException;

    Collection<ChessGame> listGames() throws DataAccessException;

    void updateGame(int gameID, ChessGame game) throws DataAccessException;

    void updateBlackPlayer(int gameID, String blackUsername) throws DataAccessException;

    void updateWhitePlayer(int gameID, String whiteUsername) throws DataAccessException;

    void deleteGames() throws DataAccessException;
}
