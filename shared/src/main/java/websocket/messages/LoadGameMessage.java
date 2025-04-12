package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    private final GameData gameData;

    public LoadGameMessage(GameData gameData){
        super(ServerMessage.ServerMessageType.LOAD_GAME );
        this.gameData = gameData;
    }

    public GameData getGame() {
        return gameData;
    }
}
