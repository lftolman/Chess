package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDataAccess gameDAO;
    private final UserDataAccess userDAO;
    private final AuthDataAccess authDAO;

    public WebSocketHandler(GameDataAccess gameDAO, UserDataAccess userDA0,AuthDataAccess authDAO){
        this.gameDAO = gameDAO;
        this.userDAO = userDA0;
        this.authDAO = authDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try{
            UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(),action.getGameID(), session);
                case LEAVE -> leave(action.getAuthToken(),action.getGameID());
                case RESIGN -> resign(action.getAuthToken(),action.getGameID());
                case MAKE_MOVE -> {
                    MakeMoveCommand moveAction = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(moveAction.getAuthToken(),moveAction.getGameID(), moveAction.getChessMove());
                }
            }
        } catch (Exception e) {
            String error = "Error: " + e.getMessage();
            var notification = new ErrorMessage(error);
            session.getRemote().sendString(new Gson().toJson(notification));
        }
    }

    private void connect(String authToken, int gameID, Session session) throws ResponseException {
        try {
            String visitorName = authDAO.getAuth(authToken).username();
            connections.add(visitorName,session,gameID);
            GameData gameData = gameDAO.getGame(gameID);
            String color = teamColor(visitorName,gameData);
            var message = String.format("%s has joined as %s", visitorName, color);
            var notification  = new NotificationMessage(message);
            var gameMessage = new LoadGameMessage(gameData);
            connections.broadcast(visitorName,notification, gameID);
            session.getRemote().sendString(new Gson().toJson(gameMessage));

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

    private void leave(String authToken, int gameID) throws ResponseException {
        try{
            String visitorName = authDAO.getAuth(authToken).username();
            connections.remove(visitorName);
            var message = String.format("%s has left the game", visitorName);
            var notification  = new NotificationMessage(message);
            GameData gameData = gameDAO.getGame(gameID);
            connections.broadcast(visitorName,notification,gameID);
            if (teamColor(visitorName,gameData).equals("white")){
                gameDAO.updateGame(gameID, new GameData(gameID,null,
                        gameData.blackUsername(),gameData.gameName(),gameData.game()));
            }
            else if (teamColor(visitorName,gameData).equals("black")){
                gameDAO.updateGame(gameID, new GameData(gameID,gameData.whiteUsername(),
                        null,gameData.gameName(),gameData.game()));
            }
        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }

    }

    private void resign(String authToken, int gameID) throws ResponseException {
        try{
            String visitorName = authDAO.getAuth(authToken).username();
            GameData gameData = gameDAO.getGame(gameID);
            if (!gameData.game().isInGamePlay() || teamColor(visitorName, gameData).equals("observer")){
                throw new ResponseException(500, "resign not allowed");
            }
            String opponent = null;
            if (teamColor(visitorName,gameData).equals("white")){
                opponent = gameData.blackUsername();
            }
            else if (teamColor(visitorName,gameData).equals("black")){
                opponent = gameData.whiteUsername();
            }
            ChessGame chessGame = gameData.game();
            chessGame.setGamePlay(false);
            GameData newGame = new GameData(gameID,gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(),chessGame);
            gameDAO.updateGame(gameID, newGame);
            var message = String.format("%s has forfeited, %s wins", visitorName, opponent);
            var notification  = new NotificationMessage(message);
            connections.broadcast(null,notification,gameID);

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

     private void makeMove(String authToken, int gameID, ChessMove chessMove) throws ResponseException {
         try{
             String visitorName = authDAO.getAuth(authToken).username();
             GameData gameData = gameDAO.getGame(gameID);
             if (!gameData.game().isInGamePlay() ||
                     !teamColor(visitorName, gameData).equals(gameData.game().getTeamTurn().toString().toLowerCase())) {
                 throw new ResponseException(500, "make move not allowed");
             }
             if (chessMove == null){
                 throw new ResponseException(500, "no move provided");
             }

             ChessGame game = gameData.game();
             game.makeMove(chessMove);
             GameData newData = new GameData(gameData.gameID(),gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
             gameDAO.updateGame(gameID,newData);
             String from = ChessPosition.stringPosition(chessMove.getStartPosition());
             String to = ChessPosition.stringPosition(chessMove.getEndPosition());
             var message = String.format("%s moved from %s to %s", visitorName,from,to);
             var notification  = new NotificationMessage(message);
             connections.broadcast(visitorName,notification, gameID);
             var gameMessage = new LoadGameMessage(gameData);
             connections.broadcast(null,gameMessage, gameID);
         } catch (Exception e) {
             throw new ResponseException(500,e.getMessage());
         }

     }

     private String teamColor(String user, GameData gameData){
        if (user.equals(gameData.whiteUsername())){
            return "white";
        }
        else if (user.equals(gameData.blackUsername())){
            return  "black";
        }
        else{
            return "observer";
        }
     }
}
