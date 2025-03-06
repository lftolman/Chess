package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class MySQLAuthDAO extends MySQLDataAccess implements AuthDataAccess{

    public MySQLAuthDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public void createAuth(AuthData authData) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO AuthData (authToken, username) VALUES (?,?)";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    public AuthData getAuth(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(authToken, rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    };

    public void deleteAuth(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM AuthData WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    public void deleteAuthData() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE AuthData";
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              authToken VARCHAR(256) NOT NULL,
              username VARCHAR(256) DEFAULT NULL,
              PRIMARY KEY(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
