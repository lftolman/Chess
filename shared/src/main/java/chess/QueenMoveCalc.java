package chess;

import java.util.Collection;

public class QueenMoveCalc implements PieceMovesCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = BishopMoveCalc.getMoves(chessBoard, myPosition);
        Collection<ChessMove> rookMoves = RookMoveCalc.getMoves(chessBoard, myPosition);
        bishopMoves.addAll(rookMoves);
        return bishopMoves;
    }
}
