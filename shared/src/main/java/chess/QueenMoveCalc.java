package chess;

import java.util.Collection;

public class QueenMoveCalc implements PieceMovesCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> bishop_moves = BishopMoveCalc.getMoves(chessBoard, myPosition);
        Collection<ChessMove> rook_moves = RookMoveCalc.getMoves(chessBoard, myPosition);
        bishop_moves.addAll(rook_moves);
        return bishop_moves;
    }
}
