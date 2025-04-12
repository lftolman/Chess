package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KING;

public class ChessBoard {
    private static final String[] WHITE_PIECES = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static final String[] WHITE_REVERSED = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static final String[] WHITE_PAWNS = {WHITE_PAWN, WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,
            WHITE_PAWN,WHITE_PAWN};
    private static final String[] BLACK_PIECES = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static final String[] BLACK_REVERSED= {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static final String[] BLACK_PAWNS = {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,
            BLACK_PAWN,BLACK_PAWN};

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 1;

    private static String color;

    private static HashMap<Integer,String> letters = new HashMap<>();
    private static HashMap<String, Integer> columns = new HashMap<>();
    private static HashMap<String, String> pieceMap = new HashMap<>();

    public static void drawBoard(String color, chess.ChessBoard board, List<String> possibleMoves, String startPosition){
        ChessBoard.color = color;
        mapPieces();
        mapColumns();
        mapLetters();

        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawChessBoard(out,board, possibleMoves, startPosition);
        out.println(ANSI_RESET);
    }

    public static void drawHeaders(PrintStream out, String[] headers){
        for (int boardCol = 0; boardCol < BOARD_SIZE; ++boardCol){
            drawHeader(out, headers[boardCol]);}
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE / 2;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
        setWhite(out);
    }

    private static void drawChessBoard(PrintStream out,chess.ChessBoard board, List<String> possibleMoves, String startPosition) {
        if (Objects.equals(color, "WHITE")) {
            String[] headers = {"    a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
            drawHeaders(out, headers);
            for (int i = 7; i > -1; i--) {
                List<String> pieces = new ArrayList<>();
                List<String> colors = new ArrayList<>();
                for (int j = 7; j > -1; j--) {
                    drawMiddle(board, possibleMoves, startPosition, i, pieces, colors, j);

                }
                drawRow(out, pieces, i+1, colors);
            }
            drawHeaders(out, headers);
        }
        else{
            String[] headers = {"    h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
            drawHeaders(out, headers);

            for (int i = 0; i < 8; i++) {
                List<String> pieces = new ArrayList<>();
                List<String> colors = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    drawMiddle(board, possibleMoves, startPosition, i, pieces, colors, j);
                }
                drawRow(out, pieces, i+1, colors);
            }
            drawHeaders(out, headers);
        }
    }

    private static void drawMiddle(chess.ChessBoard board, List<String> possibleMoves, String startPosition, int i, List<String> pieces, List<String> colors, int j) {
        ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
        if (piece == null) {
            pieces.add(EMPTY);
        } else {
            pieces.add(pieceMap.get(piece.toString()));
        }
        int row = i+1;
        if ((startPosition!=null)&&(letters.get(j) + row).equals(startPosition)) {
            colors.add(SET_BG_COLOR_LIGHT_PURPLE);
        }
        else {
            if ((i + j) %2 == 0) {
                if ((possibleMoves!=null)&&possibleMoves.contains(letters.get(j) + row)) {
                    colors.add(SET_BG_COLOR_DARK_PURPLE);}
                else{colors.add(SET_BG_COLOR_LIGHT_BLUE);}
            } else {
                if ((possibleMoves!=null)&&possibleMoves.contains(letters.get(j) + row)) {
                    colors.add(SET_BG_COLOR_BLUE_PURPLE);}
                else{colors.add(SET_BG_COLOR_PURPLE);}
            }
        }
    }

    private static void drawRow(PrintStream out, List<String> pieces, int row, List<String> background){
        printHeaderText(out, " "+row +" ");
        for (int squareRow = 0; squareRow < SQUARE_SIZE; squareRow++) {
            for (int boardCol = 0; boardCol < BOARD_SIZE; ++boardCol) {
                out.print(background.get(boardCol));
                out.print(pieces.get(boardCol));
            }
        }
        printHeaderText(out," "+row);
        out.println();
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void mapPieces(){
        pieceMap.put("p",SET_TEXT_COLOR_BLACK + BLACK_PAWN);
        pieceMap.put("P",SET_TEXT_COLOR_WHITE + WHITE_PAWN);
        pieceMap.put("n",SET_TEXT_COLOR_BLACK +BLACK_KNIGHT);
        pieceMap.put("N",SET_TEXT_COLOR_WHITE +WHITE_KNIGHT);
        pieceMap.put("b",SET_TEXT_COLOR_BLACK +BLACK_BISHOP);
        pieceMap.put("B",SET_TEXT_COLOR_WHITE +WHITE_BISHOP);
        pieceMap.put("r",SET_TEXT_COLOR_BLACK +BLACK_ROOK);
        pieceMap.put("R",SET_TEXT_COLOR_WHITE +WHITE_ROOK);
        pieceMap.put("q",SET_TEXT_COLOR_BLACK +BLACK_QUEEN);
        pieceMap.put("Q",SET_TEXT_COLOR_WHITE +WHITE_QUEEN);
        pieceMap.put("k",SET_TEXT_COLOR_BLACK +BLACK_KING);
        pieceMap.put("K",SET_TEXT_COLOR_WHITE +WHITE_KING);
    }

    private static void mapLetters(){
        letters.put(0, "a");
        letters.put(1, "b");
        letters.put(2, "c");
        letters.put(3, "d");
        letters.put(4, "e");
        letters.put(5, "f");
        letters.put(6, "g");
        letters.put(7, "h");
    }
    private static void mapColumns(){
        columns.put("a", 7);
        columns.put("b", 6);
        columns.put("c", 5);
        columns.put("d", 4);
        columns.put("e", 3);
        columns.put("f", 2);
        columns.put("g", 1);
        columns.put("h", 0);
    }

    public static ChessPosition chessPosition(String[] stringPosition){
        return new ChessPosition(Integer.parseInt(stringPosition[1]),columns.get(stringPosition[0].toLowerCase())+1);
    }

    public static String stringPosition(ChessPosition position){
       return letters.get(position.getColumn()-1) + position.getRow();
    }


    }
