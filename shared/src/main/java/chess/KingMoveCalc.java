package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalc implements PieceMovesCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[] rows = {1,-1,0};
        int[] cols = {0,1,-1};
        for (var i: rows) {
            for (var j: cols) {
                int newRow = row + i;
                int newCol = col + j;
                ChessPosition newPosition = new ChessPosition(newRow,newCol);
                if (PieceMovesCalculator.checkSquare(chessBoard,myPosition,newPosition)[0]){
                    ChessMove move = new ChessMove(myPosition,newPosition, null);
                    moves.add(move);}
            }
        }

        return moves;
    }
}
