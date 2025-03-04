package service;
import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import exception.ResponseException;
import model.*;

import java.util.Objects;
import java.util.UUID;


public class GameService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;
    private int gameID = 0;


    public GameService(GameDataAccess DAO, AuthDataAccess aDAO){
        this.gameDataAccess = DAO;
        this.authDataAccess = aDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        if (listGamesRequest.authToken() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            String authToken = listGamesRequest.authToken();
            if (authDataAccess.getAuth(authToken) == null){
                throw new ResponseException(401,"Error: unauthorized");
            }

            return new ListGamesResult(gameDataAccess.listGames());
        }catch (DataAccessException e) {
            throw new ResponseException(500,"Error: "+ e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        if (createGameRequest.gameName() == null || createGameRequest.authToken()==null){
            throw new ResponseException(401, "Error: bad request");
        }
        try {
            gameID ++;
            String authToken = createGameRequest.authToken();
            if (authDataAccess.getAuth(authToken) == null){
                throw new ResponseException(401,"Error: unauthorized");
            }
            String gameName = createGameRequest.gameName();
            GameData gameData = new GameData(gameID, null,null, gameName, new ChessGame());
            gameDataAccess.createGame(gameData);
            return new CreateGameResult(gameID);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: "+ e.getMessage());
        }

    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        try{
            if ((joinGameRequest.playerColor() == null )|| (gameID == 0)||((!joinGameRequest.playerColor().equals("WHITE")&&!joinGameRequest.playerColor().equals("BLACK")))){
                throw new ResponseException(400,"Error: bad request");
            }
            GameData gameData = gameDataAccess.getGame(joinGameRequest.gameID());
            AuthData authData = authDataAccess.getAuth(joinGameRequest.authToken());
            if (authData == null){
                throw new ResponseException(401,"Error: unauthorized");
            }
            if (gameData == null){
                throw new ResponseException(400,"Error: bad request");
            }if ((gameData.whiteUsername() == null)&& Objects.equals(joinGameRequest.playerColor(), "WHITE")){
                GameData newData = new GameData(gameID,authData.username(),gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDataAccess.updateGame(joinGameRequest.gameID(),newData);
            }
            else if ((gameData.blackUsername() == null)&& Objects.equals(joinGameRequest.playerColor(), "BLACK")){
                GameData newData = new GameData(gameID,gameData.whiteUsername(),authData.username(), gameData.gameName(), gameData.game());
                gameDataAccess.updateGame(joinGameRequest.gameID(),newData);
            }
            else{
                throw new ResponseException(403,"Error: already taken");
            }
        }catch (DataAccessException e){
            throw new ResponseException(500,"Error: "+e.getMessage());
        }
    }
}
