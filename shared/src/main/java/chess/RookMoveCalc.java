package chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalc implements PieceMovesCalculator {

    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[] rows = {1,-1,0,0};
        int[] cols = {0,0,1,-1};
        for (int k = 0; k < 4; k++) {
                int i = rows[k];
                int j = cols[k];
            updateStep(board, myPosition, moves, row, col, i, j);
        };

        return moves;}

    static void updateStep(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int col, int i, int j) {
        int step = 1;
        int newRow = row + step*i;
        int newCol = col + step*j;
        ChessPosition new_position = new ChessPosition(newRow,newCol);
        while (PieceMovesCalculator.checkSquare(board,myPosition,new_position)[0]){
            ChessMove move = new ChessMove(myPosition,new_position, null);
            moves.add(move);
            System.out.println(move);
            if (PieceMovesCalculator.checkSquare(board,myPosition,new_position)[1]){
                step ++;
                newRow = row + step*i;
                newCol = col + step*j;
                new_position = new ChessPosition(newRow,newCol);}
            else{
                break;
            }
        }
    }
}
