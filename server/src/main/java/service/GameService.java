package service;
import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
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

    public ListGamesResult listGames(ListGamesRequest listGamesRequest){
        String authToken = listGamesRequest.authToken();
        return new ListGamesRequest(gameDAO.listGames());

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        GameData gameData = new GameData(gameID, null,null, createGameRequest.gameName(), new ChessGame());
        gameDAO.createGame(gameData);
        gameID ++;
        return new CreateGameResult(gameID -1);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        GameData gameData = gameDAO.getGame(joinGameRequest.gameID());
        AuthData authData = authDAO.getAuth(joinGameRequest.authToken());
        if ((gameData.whiteUsername() == null)&& joinGameRequest.playerColor()== ChessGame.TeamColor.WHITE){
            GameData newData = new GameData(gameID,authData.username(),gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(joinGameRequest.gameID(),newData);
        }
        else if ((gameData.blackUsername() == null)&& joinGameRequest.playerColor()== ChessGame.TeamColor.BLACK){
            GameData newData = new GameData(gameID,gameData.whiteUsername(),authData.username(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(joinGameRequest.gameID(),newData);
        }
        else{
            throw new DataAccessException("player color taken");
        }
    }
}
