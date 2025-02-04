package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private boolean anyLegalMoves;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board.toString() +
                ", teamTurn=" + teamTurn +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece current = board.getPiece(startPosition);
        Collection<ChessMove> allPossible = current.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (var move : allPossible){
            if (tryMove(move)){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */


    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPiece current = board.getPiece(start);
        if ((current == null) || (current.getTeamColor() != teamTurn)){
            throw new InvalidMoveException("Provided move is not legal");
        }
        Collection<ChessMove> valid = validMoves(start);
        if (valid.contains(move)){
            movePiece(move);
        }
        else{
            throw new InvalidMoveException("Provided move is not legal");
        }
    }

    void movePiece(ChessMove move){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece mover = board.getPiece(start);
        TeamColor color = mover.getTeamColor();
        board.removePiece(end);
        board.removePiece(start);
        if (promo != null){
            mover = new ChessPiece(color, promo);
        }
        if (color == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
        board.addPiece(end, mover);
    }

    boolean tryMove(ChessMove move){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece mover = board.getPiece(start);
        TeamColor color = mover.getTeamColor();
        ChessPiece temp = board.getPiece(end);
        movePiece(move);
        boolean legal = !isInCheck(color);
        board.addPiece(start,mover);
        board.removePiece(end);
        board.addPiece(end,temp);
        setTeamTurn(color);
        return legal;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (var position: traverseBoard()){
            ChessPiece piece = board.getPiece(position);
            if ((piece != null) && (piece.getTeamColor() != teamColor)){
                for (var move: piece.pieceMoves(board,position)){
                        ChessPiece endPiece= board.getPiece(move.getEndPosition());
                        if ((endPiece != null) && (endPiece.getPieceType()== ChessPiece.PieceType.KING)){
                            return true;}
                    }
                }
        }
        return false;
    }

    Collection<ChessPosition> traverseBoard(){
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int i = 1; i<9; i++) {
            for (int j = 1; j < 9; j++) {
                positions.add(new ChessPosition(i,j));
            }
        }
        return positions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        isInStalemate(teamColor);
        return isInCheck(teamColor) && !anyLegalMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean kingThreatened = isInCheck(teamColor);
        for (var position: traverseBoard()){
            ChessPiece piece = board.getPiece(position);
            if (piece != null && (piece.getTeamColor() == teamColor)){
                for (var move: piece.pieceMoves(board,position)){
                    boolean legal = tryMove(move);
                    if (legal){
                        anyLegalMoves = true;
                        return false;}
                    }
                }
        }
        return !kingThreatened;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

}
