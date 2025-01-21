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
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.PAWN) {return "p";};
        if (teamColor == ChessGame.TeamColor.WHITE && pieceType == PieceType.PAWN) {return "P";};
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.KNIGHT) {return "n";};
        if (teamColor == ChessGame.TeamColor.WHITE && pieceType == PieceType.KNIGHT) {return "N";};
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.BISHOP) {return "b";};
        if (teamColor == ChessGame.TeamColor.WHITE && pieceType == PieceType.BISHOP) {return "B";};
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.ROOK) {return "r";};
        if (teamColor == ChessGame.TeamColor.WHITE && pieceType == PieceType.ROOK) {return "R";};
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.QUEEN) {return "q";};
        if (teamColor == ChessGame.TeamColor.WHITE && pieceType == PieceType.QUEEN) {return "Q";};
        if (teamColor == ChessGame.TeamColor.BLACK && pieceType == PieceType.KING) {return "k";};
        return "K";
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
        return switch (getPieceType()){
            case QUEEN ->  QueenMoveCalc.getMoves(board,myPosition);
            case KING ->  KingMoveCalc.getMoves(board,myPosition);
            case BISHOP ->  BishopMoveCalc.getMoves(board,myPosition);
            case KNIGHT ->  KnightMoveCalc.getMoves(board,myPosition);
            case ROOK ->  RookMoveCalc.getMoves(board,myPosition);
            case PAWN ->  PawnMoveCalc.getMoves(board,myPosition);
        };

}}
