package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalc implements PieceMovesCalculator{

    public static Collection<ChessMove> getMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[] rows = {2,2,-2,-2,1,1,-1,-1};
        int[] cols = {1,-1,1,-1,2,-2,2,-2};
        for (int i = 0; i < rows.length; i++) {
            int newRow = row + rows[i];
            int newCol = col + cols[i];
            ChessPosition newPosition = new ChessPosition(newRow,newCol);
            if (PieceMovesCalculator.checkSquare(chessBoard,myPosition,newPosition)[0]){
                ChessMove move = new ChessMove(myPosition,newPosition, null);
                moves.add(move);
            }
        }
        return moves;
    }
}
