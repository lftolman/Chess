package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;


import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KING;

public class ChessBoard {
    private static String[] whitePieces = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,
            WHITE_BISHOP, WHITE_KNIGHT,WHITE_ROOK};
    private static String[] blackPieces = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,
            BLACK_BISHOP, BLACK_KNIGHT,BLACK_ROOK};
    private String color = "WHITE";


    private static final int boardSize = 8;
    private static final int squareSize = 1;

    public static void main(String[] args){
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);


    }

    public static void drawHeaders(PrintStream out){
        String[] headers = {"   a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        for (int boardCol = 0; boardCol < boardSize; ++boardCol){
            drawHeader(out, headers[boardCol]);

//            out.print(EMPTY.repeat(lineWidth));
        }
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
        printHeaderText(out,"1 ");
        drawRowOne(out, whitePieces, 0, SET_TEXT_COLOR_WHITE);
        printHeaderText(out," 1");
        out.println();
        printHeaderText(out,"2 ");
        drawRowTwo(out,WHITE_PAWN,1,SET_TEXT_COLOR_WHITE);
        printHeaderText(out," 2");
        out.println();
        for (int i = 0; i<4;i++){
            printHeaderText(out,String.valueOf(i+3)+" ");
            drawMiddle(out,(i+1)%2 );
            printHeaderText(out," "+ String.valueOf(i+3));
            out.println();
        }
        printHeaderText(out,"7 ");
        drawRowTwo(out,BLACK_PAWN,0,SET_TEXT_COLOR_BLACK);
        printHeaderText(out," 7");
        out.println();
        printHeaderText(out,"8 ");
        drawRowOne(out, blackPieces,1,SET_TEXT_COLOR_BLACK);
        printHeaderText(out," 8");
        out.println();
        drawHeaders(out);
    }

    private static void drawRowOne(PrintStream out,String[] pieces, int whiteCol, String pieceColor){
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
    }

    private static void drawRowTwo(PrintStream out,String piece, int whiteCol, String pieceColor){
        for (int squareRow = 0; squareRow < squareSize; squareRow++) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
                out.print(pieceColor);
                if ((boardCol+whiteCol) %2 == 0 ){
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                else{
                    out.print(SET_BG_COLOR_DARK_GREY);
                }
                out.print(piece);
            }
        }
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
