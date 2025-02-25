package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess{
    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    void deleteGames() throws DataAccessException;
}
