package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KING;

public class ChessBoard {
    private static final String[] whitePieces = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static final String[] whiteReversed = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static final String[] whitePawns = {WHITE_PAWN, WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,
            WHITE_PAWN,WHITE_PAWN};
    private static final String[] blackPieces = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static final String[] blackReversed= {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static final String[] blackPawns = {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,
            BLACK_PAWN,BLACK_PAWN};

    private static final int boardSize = 8;
    private static final int squareSize = 1;

    public static void drawBoard(String color, chess.ChessBoard board){

        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawChessBoard(out, color, board);
        out.println(ANSI_RESET);
    }

    public static void drawHeaders(PrintStream out, String[] headers){
        for (int boardCol = 0; boardCol < boardSize; ++boardCol){
            drawHeader(out, headers[boardCol]);}
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = squareSize / 2;
        int suffixLength = squareSize - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
        setWhite(out);
    }

    private static void drawChessBoard(PrintStream out, String color,chess.ChessBoard board ) {
        if (Objects.equals(color, "WHITE")){
            String[] headers = {"    a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
            drawHeaders(out,headers);
            drawRow(out, blackPieces, 0, SET_TEXT_COLOR_BLACK, 8);
            drawRow(out, blackPawns, 1, SET_TEXT_COLOR_BLACK, 7);
            for (int i = 0; i<4;i++){
                printHeaderText(out," "+(6-i)+" ");
                drawMiddle(out,(i+1)%2 );
                printHeaderText(out," "+ (6 - i));
                out.println();
            }
            drawRow(out, whitePawns,0,SET_TEXT_COLOR_WHITE,2);
            drawRow(out, whitePieces,1,SET_TEXT_COLOR_WHITE, 1);
            drawHeaders(out,headers);}
        else{
            String[] headers = {"    h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
            drawHeaders(out,headers);
            drawRow(out, whiteReversed,0,SET_TEXT_COLOR_WHITE, 1);
            drawRow(out, whitePawns,1,SET_TEXT_COLOR_WHITE, 2);
            for (int i = 0; i<4;i++){
                printHeaderText(out," "+(i+3)+" ");
                drawMiddle(out,(i+1)%2 );
                printHeaderText(out," "+ (i+3));
                out.println();
            }
            drawRow(out, blackPawns, 0, SET_TEXT_COLOR_BLACK, 7);
            drawRow(out, blackReversed, 1, SET_TEXT_COLOR_BLACK, 8);
            drawHeaders(out,headers);
        }

    }

    private static void drawRow(PrintStream out,String[] pieces, int whiteCol, String pieceColor, int row){
        printHeaderText(out, " "+row +" ");
        for (int squareRow = 0; squareRow < squareSize; squareRow++) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
                out.print(pieceColor);
                if ((boardCol+whiteCol) %2 == 0 ){
                    out.print(SET_BG_COLOR_LIGHT_BLUE);
                }
                else{
                    out.print(SET_BG_COLOR_PURPLE);
                }
                out.print(pieces[boardCol]);
            }
        }
        printHeaderText(out," "+row);
        out.println();
    }

    private static void drawMiddle(PrintStream out, int whiteCol) {

        for (int squareRow = 0; squareRow < squareSize; squareRow++) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
                if ((boardCol + whiteCol) %2 == 1 ){
                    out.print(SET_BG_COLOR_LIGHT_BLUE);
                }
                else{
                    out.print(SET_BG_COLOR_PURPLE);
                }
                out.print(EMPTY.repeat(squareSize));
            }
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private String getPiece(ChessPiece piece){
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return pieceSwitch(piece, WHITE_QUEEN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_KING, WHITE_PAWN);
        }
        else{
            return pieceSwitch(piece, BLACK_QUEEN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_KING, BLACK_PAWN);
        }
    }

    private String pieceSwitch(ChessPiece piece, String queen, String knight, String bishop, String rook, String king, String pawn) {
        switch (piece.getPieceType()){
            case QUEEN -> {return queen;}
            case KNIGHT -> {return knight;}
            case BISHOP -> {return bishop;}
            case ROOK -> {return rook;}
            case KING -> {return king;}
            case PAWN -> {return pawn;}
        }
        return null;
    }


    }
