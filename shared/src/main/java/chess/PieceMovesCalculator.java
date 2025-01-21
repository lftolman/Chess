package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    static boolean[] checkSquare(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition){
//        return if you can move and if there is a piece in the new spot
        boolean[] canMove = new boolean[2];
        boolean onBoard = newPosition.getRow() >=1 && newPosition.getRow() <=8 && newPosition.getColumn() >=1 && newPosition.getColumn() <=8;
        if (onBoard){
            ChessPiece newPiece = board.getPiece(newPosition);
            ChessPiece currentPiece = board.getPiece(myPosition);
            if (newPiece == null){
                canMove[0] = true;
                canMove[1] = true;
                return canMove;
            }
            canMove[0] = (newPiece.getTeamColor() != currentPiece.getTeamColor());
            return canMove;
        }
        return canMove;
    }
}
