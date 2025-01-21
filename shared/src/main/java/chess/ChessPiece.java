package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor teamColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", pieceType=" + pieceType +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == ChessPiece.PieceType.BISHOP) {
            return BishopMoveCalc.getMoves(board, myPosition);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK) {
            return RookMoveCalc.getMoves(board, myPosition);
        }
        else if(pieceType == ChessPiece.PieceType.QUEEN){
            return QueenMoveCalculator.getMoves(board, myPosition);
        }
        else if (pieceType == ChessPiece.PieceType.KING){
            return KingMoveCalc.getMoves(board, myPosition);
        }
        else if (pieceType == ChessPiece.PieceType.KNIGHT){
            return KnightMoveCalc.getMoves(board, myPosition);
        }
        else{
            return null;
        }
//        return switch (pieceType) {
//            case BISHOP: BishopMoveCalc.getMoves(board, myPosition);
//            case KNIGHT: KnightMoveCalc.getMoves(board, myPosition);
//            case ROOK: RookMoveCalc.getMoves(board, myPosition);
//            case PAWN: PawnMoveCalc.getMoves(board, myPosition);
//            case KING: KingMoveCalc.getMoves(board, myPosition);
//            case QUEEN: QueenMoveCalculator.getMoves(board, myPosition);
//        }
}}
