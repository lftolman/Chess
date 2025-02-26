package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import service.GameService;

import spark.Request;
import spark.Response;

public class GameHandler {
    GameService gameService;
    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) throws ResponseException {
        ListGamesRequest listGamesRequest = new Gson().fromJson(req.body(), ListGamesRequest.class);
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            res.status(200);
            return new Gson().toJson(listGamesResult);
        } catch (ResponseException e) {
            res.status(e.StatusCode());
            throw e;
        }
    }

    public Object createGame(Request req, Response res) throws ResponseException{
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(),CreateGameRequest.class);
        try{
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            res.status(200);
            return new Gson().toJson(createGameResult);
        }catch (ResponseException e){
            res.status(e.StatusCode());
            throw e;
        }
    }

    public Object joinGame(Request req, Response res) throws ResponseException{
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(),JoinGameRequest.class);
        try{
            gameService.joinGame(joinGameRequest);
            res.status(200);
            return "{}";
        } catch (ResponseException e){
            throw e;
        }
    }

}
