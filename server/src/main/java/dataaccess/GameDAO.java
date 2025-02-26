package dataaccess;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDataAccess{
    HashMap<Integer, GameData> games = new HashMap<Integer,GameData>();

    public void createGame(GameData gameData){
        games.put(gameData.gameID(),gameData);
    }

    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    public Collection<GameData> listGames(){
        return games.values();
    }

    public void updateGame(int gameID, GameData game){
        games.replace(gameID,game);
    }

    public void deleteGames(){
        games.clear();
    }

}
