package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDataAccess{

    public MySQLUserDAO() throws ResponseException{
        configureDatabase();
    }

    void insertUser(UserData u) throws DataAccessException{

    };

    UserData getUser(String username) throws DataAccessException;

    void deleteUserData() throws DataAccessException;

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY('username'),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws ResponseException {
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
