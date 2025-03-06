package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDataAccess{

    public MySQLAuthDAO() throws ResponseException {
        configureDatabase();
    }

    void createAuth(AuthData authData) throws DataAccessException{

    };

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAuthData() throws DataAccessException;


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              `authToken` varchar(256) DEFAULT NULL,
              `username` varchar(256) DEFAULT NULL,
              PRIMARY KEY('authToken'),
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
