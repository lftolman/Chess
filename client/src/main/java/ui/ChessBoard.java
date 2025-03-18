package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;


import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KING;

public class ChessBoard {
    private static String[] whitePieces = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static String[] whiteReversed = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static String[] whitePawns = {WHITE_PAWN, WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,
            WHITE_PAWN,WHITE_PAWN};
    private static String[] blackPieces = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static String[] blackReversed= {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private static String[] blackPawns = {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,
            BLACK_PAWN,BLACK_PAWN};
    private static String color = "WHITE";


    private static final int boardSize = 8;
    private static final int squareSize = 1;

    public static void main(String[] args){
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);


        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);


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
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawTicTacToeBoard(PrintStream out) {
        if (Objects.equals(color, "WHITE")){
            String[] headers = {"   a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
            drawHeaders(out,headers);
            drawRow(out, blackPieces, 0, SET_TEXT_COLOR_BLACK, 8);
            drawRow(out, blackPawns, 1, SET_BG_COLOR_BLACK, 7);
            for (int i = 0; i<4;i++){
                printHeaderText(out,(6-i)+" ");
                drawMiddle(out,(i+1)%2 );
                printHeaderText(out," "+ (6 - i));
                out.println();
            }
            drawRow(out, whitePawns,0,SET_TEXT_COLOR_WHITE,2);
            drawRow(out, whitePieces,1,SET_TEXT_COLOR_WHITE, 1);
            drawHeaders(out,headers);}
        else{
            String[] headers = {"   h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
            drawHeaders(out,headers);
            drawRow(out, whiteReversed,0,SET_TEXT_COLOR_WHITE, 1);
            drawRow(out, whitePawns,1,SET_TEXT_COLOR_WHITE, 2);
            for (int i = 0; i<4;i++){
                printHeaderText(out,(i+3)+" ");
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
        printHeaderText(out, row +" ");
        for (int squareRow = 0; squareRow < squareSize; squareRow++) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
                out.print(pieceColor);
                if ((boardCol+whiteCol) %2 == 0 ){
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else{
                    out.print(SET_BG_COLOR_DARK_GREY);
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
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else{
                    out.print(SET_BG_COLOR_DARK_GREY);
                }
                out.print(EMPTY.repeat(squareSize));
            }
        }
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}
