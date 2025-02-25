package service;
import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import exception.ResponseException;
import model.*;

import java.util.UUID;


public class GameService {
    private GameDataAccess gameDAO;
    private AuthDataAccess authDAO;
    private int gameID = 1;


    public GameService(GameDataAccess DAO, AuthDataAccess aDAO){
        this.gameDAO = DAO;
        this.authDAO = aDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = listGamesRequest.authToken();
        if (authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        try {
            return new ListGamesResult(gameDAO.listGames());
        }catch (DataAccessException e) {
            throw new RuntimeException("Error: %e");
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String authToken = createGameRequest.authToken();
        if (authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        try {
            GameData gameData = new GameData(gameID, null,null, createGameRequest.gameName(), new ChessGame());
            gameDAO.createGame(gameData);
            gameID ++;
            return new CreateGameResult(gameID -1);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error: %e");
        }

    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        try{
            GameData gameData = gameDAO.getGame(joinGameRequest.gameID());
            AuthData authData = authDAO.getAuth(joinGameRequest.authToken());
            if (authData == null){
                throw new ResponseException(401,"Error: unauthorized");
            }
            if (gameData == null){
                throw new ResponseException(400,"Error: bad request");
            }
            if ((gameData.whiteUsername() == null)&& joinGameRequest.playerColor()== ChessGame.TeamColor.WHITE){
                GameData newData = new GameData(gameID,authData.username(),gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(joinGameRequest.gameID(),newData);
            }
            else if ((gameData.blackUsername() == null)&& joinGameRequest.playerColor()== ChessGame.TeamColor.BLACK){
                GameData newData = new GameData(gameID,gameData.whiteUsername(),authData.username(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(joinGameRequest.gameID(),newData);
            }
            else{
                throw new ResponseException(403,"Error: already taken");
            }
        }catch (DataAccessException e){
            throw new ResponseException(500,"Error: &e");
        }
    }
}
