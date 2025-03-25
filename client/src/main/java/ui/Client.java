package ui;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;

import exception.ResponseException;
import model.*;
import net.REPL;
import net.ServerFacade;

import java.util.HashMap;
import java.util.Objects;


public class Client {
    public boolean loggedIn = false;
    private final ServerFacade server;
    private HashMap<Integer, Integer> gameIDs = new HashMap<>();
    private HashMap<Integer, chess.ChessBoard> games = new HashMap<>();
    private int viewerID = 0;

    public Client(String serverUrl, REPL repl){
        this.server = new ServerFacade(serverUrl);

    }

    public String readInput(String[] result){
            switch (result[0].toLowerCase()){
                case "help" -> {return help();}
                case "register" -> {return register(result);}
                case "login" -> {return login(result);}
                case "create" -> {return create(result);}
                case "join" -> {return join(result);}
                case "list" -> {return list();}
                case "observe" -> {return observe(result);}
                case "logout" -> {return logout();}
                case "quit" -> {return null;}
                default -> { return SET_TEXT_COLOR_RED +"Input not recognized, try again.";}
            }
    }

    public String help(){
        String output = "";
        output = output + SET_TEXT_COLOR_LIGHT_BLUE+"help "+SET_TEXT_COLOR_PURPLE + "- with possible commands";
        if (!loggedIn){
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nregister <USERNAME> <PASSWORD> <EMAIL> " +
                    SET_TEXT_COLOR_PURPLE + "- to create an account";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nlogin <USERNAME> <PASSWORD> " +SET_TEXT_COLOR_PURPLE +
                    "- to play chess";}
        else{
            output = output+SET_TEXT_COLOR_LIGHT_BLUE+"\ncreate <NAME> "+SET_TEXT_COLOR_PURPLE+
                    "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\njoin <ID> [WHITE|BLACK] "
                    +SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nobserve <ID> "+SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nlogout "+SET_TEXT_COLOR_PURPLE + "- when you are done";
        }
        output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nquit " +SET_TEXT_COLOR_PURPLE + "- playing chess";
        return output;
    }

    public String register(String[] result)  {
        if (this.loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged out";
        }
        if (result.length != 4){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, try again";
        }
        try {
            RegisterRequest request = new RegisterRequest(result[1],result[2], result[3]);
            RegisterResult registerResult = server.register(request);
            LoginRequest loginRequest = new LoginRequest(result[1],result[2]);
            LoginResult loginResult = server.login(loginRequest);
            loggedIn = true;
            return SET_TEXT_COLOR_GREEN+"register successful for "+registerResult.username()+". you are now logged in.";
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED  + "register unsuccessful, " + e.getMessage();
        }
    }

    public String login(String[] result){
        if (this.loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged out";
        }
        if (result.length != 3){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, try again";
        }
        try{
            LoginRequest request = new LoginRequest(result[1],result[2]);
            LoginResult loginResult = server.login(request);
            loggedIn = true;
            return SET_TEXT_COLOR_GREEN + "login successful for " + loginResult.username();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED  + "login unsuccessful, " + e.getMessage();
        }
    }

    public String create(String[] result){
        if (result.length!=2){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, try again";
        }
        try {
            CreateGameResult createGameResult = server.createGame(result[1]);
            viewerID++;
            gameIDs.put(viewerID,createGameResult.gameID());
            games.put(createGameResult.gameID(),new chess.ChessBoard());
            return SET_TEXT_COLOR_GREEN + "game creation successful for game: "+ result[1];
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "create game unsuccessful, " + e.getMessage();
        }
    }

    public String join(String[] result){
        if (result.length!=3){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, try again";
        }
        String playerColor = result[2].toUpperCase();
        if (!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK") ){
            return SET_TEXT_COLOR_RED + "player color must be WHITE or BLACK";
        }
        if (!result[1].chars().allMatch( Character::isDigit )||!gameIDs.containsKey(parseInt(result[1]))){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        try {
            int gameID = gameIDs.get(parseInt(result[1]));
            server.join(gameID, playerColor);
            ChessBoard.drawBoard(playerColor, games.get(gameID));
            return "";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED+"could not join game: " + e.getMessage();
        }
    }

    public String observe(String[] result){
        if (result.length!=2){
            return SET_TEXT_COLOR_RED + "incorrect number of arguments, try again";
        }
        if (!result[1].chars().allMatch( Character::isDigit )||!gameIDs.containsKey(parseInt(result[1]))){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        try{
            int gameID = gameIDs.get(parseInt(result[1]));
            ChessBoard.drawBoard("WHITE", games.get(gameID));
            return "";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "observe unsuccessful, " + e.getMessage();
        }
    }

    public String logout(){
        if (!this.loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";
        }
        try{
            server.logout();
        } catch(Exception e){
            return SET_TEXT_COLOR_RED + "logout failed: " + e.getMessage();
        }
        this.loggedIn = false;
        return SET_TEXT_COLOR_GREEN + "logout successful";
    }

    public String list(){
        try {
            ListGamesResult listGamesResult = server.listGames();
            HashMap<Integer,Integer> map = new HashMap<>();
            int i = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(SET_TEXT_COLOR_PURPLE);
            sb.append("gameID    game name    white player    black player    \n");
            for (var game: listGamesResult.games()){
                i++;
                games.put(game.gameID(),game.game().getBoard());
                String whiteUsername = game.whiteUsername();
                String blackUsername = game.blackUsername();
                int whiteLength = 0;
                int blackLength = 0;
                if (whiteUsername !=null){
                    whiteLength = whiteUsername.length();
                }
                if (blackUsername != null){
                    blackLength = blackUsername.length();
                }
                if (whiteUsername == null){
                    whiteUsername = "";
                }
                if (blackUsername == null){
                    blackUsername = "";
                }
                map.put(i,game.gameID());
                sb.append(i);
                sb.append(" ".repeat(10 - String.valueOf(i).length()));
                sb.append(game.gameName());
                sb.append(" ".repeat(13 - game.gameName().length()));
                sb.append(whiteUsername);
                sb.append(" ".repeat(16 - whiteLength));
                sb.append(blackUsername);
                sb.append(" ".repeat(16 - blackLength));
                sb.append("\n");
            }
            gameIDs = map;
            return sb.toString();
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "list games unsuccessful, " + e.getMessage();
        }


    }



}
