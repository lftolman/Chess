package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import service.GameService;

import spark.Request;
import spark.Response;

import java.util.HashMap;

public class GameHandler {
    GameService gameService;
    public GameHandler(GameService gameService){
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
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
        var data = new Gson().fromJson(req.body(), HashMap.class);
        String gameName = null;
        if (data.get("gameName")!= null){
            gameName = data.get("gameName").toString();
        }

        CreateGameRequest createGameRequest = new CreateGameRequest(req.headers("authorization"),gameName);
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
        try{
            var data = new Gson().fromJson(req.body(), HashMap.class);
            String playerColor = null;
            int gameID = 0;
            if (data.get("playerColor") != null){
                playerColor = data.get("playerColor").toString();}
            if (data.get("gameID") != null){
                double num = Double.parseDouble(data.get("gameID").toString());
                gameID = (int) num;}
            JoinGameRequest joinGameRequest = new JoinGameRequest(req.headers("authorization"),playerColor,gameID);

            gameService.joinGame(joinGameRequest);
            res.status(200);
            return "{}";
        } catch (ResponseException e){
            res.status(e.StatusCode());
            throw e;
        }
    }

}
