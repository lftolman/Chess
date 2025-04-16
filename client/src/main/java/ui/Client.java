package ui;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;

import chess.*;
import exception.ResponseException;
import model.*;
import net.REPL;
import net.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.*;


public class Client {
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    public boolean loggedIn = false;
    public boolean inGamePlay = false;
    private final ServerFacade server;
    private GameData chessGame;
    private String color;
    private HashMap<Integer, GameData> games = new HashMap<>();
    private List<Integer> uiMapping = new ArrayList<>();

    private WebSocketFacade ws;
    private String authToken;

    public Client(String serverUrl, REPL repl){
        this.server = new ServerFacade(serverUrl);
        this.notificationHandler = new NotificationHandler();
        this.serverUrl = serverUrl;

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
                case "redraw" -> {return redraw();}
                case "make" -> {return make(result);}
                case "resign" -> {return resign();}
                case "highlight" -> {return highlight(result);}
                case "leave" -> {return leave();}
                case "quit" -> {return null;}
                default -> { return SET_TEXT_COLOR_RED +"input not recognized, try again.";}
            }
    }

    public String leave() {
        try{
            ws = new WebSocketFacade(serverUrl,notificationHandler);
            if (!inGamePlay) {
                return SET_TEXT_COLOR_RED + "must be in gameplay to resign";
            }
            ws.leave(authToken, chessGame.gameID());
            inGamePlay = false;
            return SET_TEXT_COLOR_GREEN + "successfully left the game";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "leave game unsuccessful" + e.getMessage();
        }
    }

    public String redraw() {
        ChessBoard.drawBoard(color,chessGame.game().getBoard(),null,null);
        return SET_TEXT_COLOR_GREEN + "redraw board successful";

    }

    private String make(String[] result) {
        try {
            if (!Objects.equals(color, chessGame.game().getTeamTurn().toString().toLowerCase())){
                return SET_TEXT_COLOR_RED + "it's not your turn";
            }
            ws = new WebSocketFacade(serverUrl,notificationHandler);
            chess.ChessBoard board = chessGame.game().getBoard();
            ChessPiece.PieceType promo = null;
            ChessPosition from = ChessBoard.chessPosition(result[1].split(""));
            ChessPosition to = ChessBoard.chessPosition(result[2].split(""));
            Collection<ChessMove> validMoves = chessGame.game().validMoves(from);
            if (board.getPiece(from).getPieceType() == ChessPiece.PieceType.PAWN) {
                for (var move : validMoves) {
                    if (move.getPromotionPiece() != null && (move.getEndPosition() == to)) {
                        if (result.length != 4) {
                            return SET_TEXT_COLOR_RED + "promotion piece required";
                        }
                        promo = getPieceType(result[3]);
                        if (promo == null) {
                            return SET_TEXT_COLOR_RED + "promotion piece must be QUEEN or KNIGHT or BISHOP or ROOK";
                        }
                    }
                }
            }
            ChessMove move = new ChessMove(from, to, promo);
            if (!validMoves.contains(move)) {
                return SET_TEXT_COLOR_RED + "invalid move, expecting <COLUMN><ROW>, type highlight for options,";
            }
            ChessGame game = chessGame.game();

            ws.makeMove(authToken, chessGame.gameID(), move);

            return SET_TEXT_COLOR_GREEN + "move made successfully";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "make move unsuccessful, " + e.getMessage();
        }

    }

    public String resign() {
        try {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            if (!inGamePlay) {
                return SET_TEXT_COLOR_RED + "must be in gameplay to resign";
            }
            ws.resign(authToken, chessGame.gameID());
            inGamePlay = false;
            return SET_TEXT_COLOR_GREEN + "successfully left the game";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "resign unsuccessful, " + e.getMessage();
        }
    }

    public String highlight(String[] result) {
        String[] characters = result[1].split("");
        if (!characters[1].matches("\\d")||!characters[0].matches("[a-zA-Z]")){
            return SET_TEXT_COLOR_RED + "invalid input, try again";
        }
        ChessPosition position = ChessBoard.chessPosition(characters);
        chess.ChessBoard board = chessGame.game().getBoard();
        Collection<ChessMove> moves =  chessGame.game().validMoves(position);
        List<String> stringMoves = new ArrayList<>();
        if (moves.isEmpty()){
            ChessBoard.drawBoard(color,chessGame.game().getBoard(),null,null);
            return SET_TEXT_COLOR_GREEN + "no available moves for " + result[1];
        }
        for (var move : moves) {
            stringMoves.add(ChessBoard.stringPosition(move.getEndPosition()));
        }
        ChessBoard.drawBoard(color,chessGame.game().getBoard(),stringMoves,ChessBoard.stringPosition(position));

        return SET_TEXT_COLOR_GREEN + "highlighted moves for " + result[1];

    }

    public String help(){
        String output = "";
        output = output + SET_TEXT_COLOR_LIGHT_BLUE+"help "+SET_TEXT_COLOR_PURPLE + "- with possible commands";
        if (!loggedIn){
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nregister <USERNAME> <PASSWORD> <EMAIL> " +
                    SET_TEXT_COLOR_PURPLE + "- to create an account";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nlogin <USERNAME> <PASSWORD> " +SET_TEXT_COLOR_PURPLE +
                    "- to play chess";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nquit " +SET_TEXT_COLOR_PURPLE + "- playing chess";
        }
        else if (!inGamePlay){
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nlist "+SET_TEXT_COLOR_PURPLE + "- games";
            output = output+SET_TEXT_COLOR_LIGHT_BLUE+"\ncreate <NAME> "+SET_TEXT_COLOR_PURPLE+
                    "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\njoin <ID> [WHITE|BLACK] "
                    +SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nobserve <ID> "+SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nlogout "+SET_TEXT_COLOR_PURPLE + "- when you are done";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nquit " +SET_TEXT_COLOR_PURPLE + "- playing chess";

        }
        else{
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nredraw " +
                    SET_TEXT_COLOR_PURPLE + "- chess board";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nleave " +SET_TEXT_COLOR_PURPLE +
                    "- the game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nmake move <MOVE> " +SET_TEXT_COLOR_PURPLE +
                    "- your";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nresign " +SET_TEXT_COLOR_PURPLE +
                    "- the game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nhighlight <POSITION> " +SET_TEXT_COLOR_PURPLE +
                    "- legal moves";
        }
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
            return SET_TEXT_COLOR_RED  + e.getMessage();
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
            authToken = loginResult.authToken();
            return SET_TEXT_COLOR_GREEN + "login successful for " + loginResult.username();
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED  + e.getMessage();
        }
    }

    public String create(String[] result){
        if (!loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";
        }
        if (result.length!=2){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, try again";
        }
        try {
            CreateGameResult createGameResult = server.createGame(result[1]);
            String listResult = list();
            return SET_TEXT_COLOR_GREEN + "game creation successful for game: "+ result[1];
        } catch (ResponseException e) {
            return SET_TEXT_COLOR_RED + "create game unsuccessful, " + e.getMessage();
        }
    }

    public String join(String[] result){
        if (!loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";
        }
        if (result.length!=3){
            return SET_TEXT_COLOR_RED  + "incorrect number of arguments, expected <ID> [WHITE|BLACK]";
        }
        if (!result[1].chars().allMatch( Character::isDigit )){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        int index = parseInt(result[1]) - 1;
        if (index < 0 || index >= uiMapping.size()){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        String playerColor = result[2].toUpperCase();
        if (!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK") ){
            return SET_TEXT_COLOR_RED + "player color must be WHITE or BLACK";
        }
        try {
            ws = new WebSocketFacade(serverUrl,notificationHandler);
            int gameID = uiMapping.get(index);
            server.join(gameID, playerColor);
            this.color = playerColor;
            chessGame = games.get(gameID);
            ChessBoard.drawBoard(playerColor,chessGame.game().getBoard(),null,null );
            inGamePlay = true;
            ws.connect(authToken, gameID);
            return "game joined successfully";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED+ e.getMessage();
        }
    }

    public String observe(String[] result){
        if (!loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";
        }
        if (result.length!=2){
            return SET_TEXT_COLOR_RED + "incorrect number of arguments, try again";
        }
        if (!result[1].chars().allMatch( Character::isDigit )){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        int index = parseInt(result[1]) - 1;
        if (index < 0 || index >= uiMapping.size()){
            return SET_TEXT_COLOR_RED + "game nonexistent, run \"list\" to see options";
        }
        try{
            ws = new WebSocketFacade(serverUrl,notificationHandler);
            int gameID = uiMapping.get(index);
            chessGame = games.get(gameID);
            ws.connect(authToken, gameID);
            ChessBoard.drawBoard("WHITE", chessGame.game().getBoard(), null,null);
            return "you are now observing the game";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "observe unsuccessful, " + e.getMessage();
        }
    }

    public String logout(){
        if (!this.loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";}
        try{
            server.logout();
        } catch(Exception e){
            return SET_TEXT_COLOR_RED + "logout failed, " + e.getMessage();}
        this.loggedIn = false;
        authToken = null;
        return SET_TEXT_COLOR_GREEN + "logout successful";
    }

    public String list(){
        if (!loggedIn){
            return SET_TEXT_COLOR_RED + "must be logged in";
        }
        try {
            ListGamesResult listGamesResult = server.listGames();
            HashMap<Integer,GameData> mapTwo = new HashMap<>();
            uiMapping.clear();
            int i = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(SET_TEXT_COLOR_PURPLE);
            sb.append("gameID    game name    white player    black player    \n");
            for (var game: listGamesResult.games()){
                uiMapping.add(game.gameID());
                i++;
                mapTwo.put(game.gameID(),game);
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
                }sb.append(i);
                sb.append(" ".repeat(10 - String.valueOf(i).length()));
                sb.append(game.gameName());
                sb.append(" ".repeat(13 - game.gameName().length()));
                sb.append(whiteUsername);
                sb.append(" ".repeat(16 - whiteLength));
                sb.append(blackUsername);
                sb.append(" ".repeat(16 - blackLength));
                sb.append("\n");
            }
            this.games = mapTwo;
            return sb.toString();
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "list games unsuccessful, " + e.getMessage();
        }
    }

    private ChessPiece.PieceType getPieceType(String pieceString){
        switch (pieceString.toLowerCase()){
            case "queen" -> {return ChessPiece.PieceType.QUEEN;}
            case "rook" -> {return ChessPiece.PieceType.ROOK;}
            case "bishop" -> {return ChessPiece.PieceType.BISHOP;}
            case "knight" -> {return ChessPiece.PieceType.KNIGHT;}
        }
        return null;
    }


}
