package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class MySQLGameDAO implements GameDataAccess{
    public MySQLGameDAO() throws ResponseException{
        configureDatabase();
    }

    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int gameID, GameData game) throws DataAccessException;

    void deleteGames() throws DataAccessException;

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              'chessGame' json NOT NULL;
              PRIMARY KEY('gameID'),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
