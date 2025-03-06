package dataaccess;

import model.GameData;

import java.util.Collection;

public class MySQLGameDAO implements GameDataAccess{
    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    void deleteGames() throws DataAccessException;
}
