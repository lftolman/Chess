package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySQLGameDAO extends MySQLDataAccess implements GameDataAccess{
    public MySQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public void createGame(GameData gameData) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?,?,?,?,?)";
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameData.gameID());
                ps.setString(2, gameData.whiteUsername());
                ps.setString(3, gameData.blackUsername());
                ps.setString(4, gameData.gameName());
                var json = new Gson().toJson(gameData.game());
                ps.setString(5, json);
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    public GameData getGame(int gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM GameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"),ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    };

    public Collection<GameData> listGames() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM GameData";
            try (var ps = conn.prepareStatement(statement)){
                var rs = ps.executeQuery();
                Collection<GameData> games = new ArrayList<>();
                while (rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"),ChessGame.class);
                    games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                }
                return games;
            }

        }catch (Exception e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    };

    public void updateGame(int gameID, GameData gameData) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "UPDATE GameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGame = ? WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2, gameData.blackUsername());
                ps.setString(3, gameData.gameName());
                var json = new Gson().toJson(gameData.game());
                ps.setString(4, json);
                ps.setInt(5, gameData.gameID());

                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    public void deleteGames() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE GameData";
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    };

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS GameData (
              gameID int NOT NULL,
              whiteUsername VARCHAR(256) DEFAULT NULL,
              blackUsername VARCHAR(256) DEFAULT NULL,
              gameName VARCHAR(256) NOT NULL,
              chessGame json NOT NULL,
              PRIMARY KEY(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
