package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;


public class NotificationHandler {
    void notify(NotificationMessage notification) {}

    void error(ErrorMessage error){}

    void loadGame(LoadGameMessage game){}
}
