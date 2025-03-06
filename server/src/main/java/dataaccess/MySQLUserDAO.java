package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO extends MySQLDataAccess implements UserDataAccess{

    public MySQLUserDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public void insertUser(UserData u) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO UserData (username,password,email) VALUES (?,?,?)";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, u.username());
                ps.setString(2, u.password());
                ps.setString(3,u.email());
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM UserData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(username, rs.getString("password"),rs.getString("email"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteUserData() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE UserData";
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              username VARCHAR(256) NOT NULL,
              password VARCHAR(256) NOT NULL,
              email VARCHAR(256) NOT NULL,
              PRIMARY KEY(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };




}
