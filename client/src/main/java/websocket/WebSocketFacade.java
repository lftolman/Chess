package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http","ws");
            URI uri = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;


            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    private void handleMessage(String message) {
        ServerMessage notification = new Gson().fromJson(message,ServerMessage.class);
        ServerMessage.ServerMessageType type = notification.getServerMessageType();
        switch (type){
            case NOTIFICATION -> notificationHandler.notify(new Gson().fromJson(message, NotificationMessage.class));
            case LOAD_GAME -> notificationHandler.loadGame(new Gson().fromJson(message, LoadGameMessage.class));
            case ERROR -> notificationHandler.error(new Gson().fromJson(message, ErrorMessage.class));
        }
    }

    public void connect(String authToken, Integer gameID) throws ResponseException {
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try{
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws ResponseException {
        try{
            var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken,gameID,move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
