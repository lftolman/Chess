package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalc implements PieceMovesCalculator{
    public static Collection<ChessMove> getMoves(ChessBoard chessBoard, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPiece.PieceType> pieces = new ArrayList<>();
        pieces.add(ChessPiece.PieceType.QUEEN); pieces.add(ChessPiece.PieceType.BISHOP); pieces.add(ChessPiece.PieceType.ROOK); pieces.add(ChessPiece.PieceType.KNIGHT);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece myPiece = chessBoard.getPiece(myPosition);
        ChessGame.TeamColor color = myPiece.getTeamColor();
        int sign = 0;
        int startRow = 0;
        if (color == ChessGame.TeamColor.BLACK) {
            sign--; startRow += 7;}
        else {
            sign++; startRow += 2;}
        ChessPosition newPosition = new ChessPosition(sign + row, col);
        if (PieceMovesCalculator.checkSquare(chessBoard, myPosition,newPosition)[1]){
//          add move immediately forward if no piece in the way
            if (sign + row == startRow + (sign*6)){
                for (ChessPiece.PieceType pieceType : pieces) {
                    ChessMove move = new ChessMove(myPosition,newPosition,pieceType);
                    moves.add(move);
                }}
            else {
                ChessMove move = new ChessMove(myPosition,newPosition,null);
                moves.add(move);}
            ChessPosition newPosition2 = new ChessPosition(2*sign + row, col);
            if (row == startRow && PieceMovesCalculator.checkSquare(chessBoard, myPosition,newPosition2)[1]){
//                add move 2 spaces forward if in original row
                ChessMove move2 = new ChessMove(myPosition,newPosition2,null);
                moves.add(move2);}
        }
//        add diagonal moves if piece of opposite color in correct spot
        ChessPosition diag1 = new ChessPosition(sign + row, col+1);
        ChessPosition diag2 = new ChessPosition(sign + row, col-1);
        if (PieceMovesCalculator.checkSquare(chessBoard, myPosition,diag1)[0]&& !PieceMovesCalculator.checkSquare(chessBoard, myPosition,diag1)[1]){
            if (sign + row == startRow + (sign*6)){
                for (ChessPiece.PieceType pieceType : pieces) {
                    ChessMove move = new ChessMove(myPosition,diag1,pieceType);
                    moves.add(move);
                }}
            else{
                ChessMove move = new ChessMove(myPosition,diag1,null);
                moves.add(move);}
            }
        if (PieceMovesCalculator.checkSquare(chessBoard, myPosition,diag2)[0]&& !PieceMovesCalculator.checkSquare(chessBoard, myPosition,diag2)[1]){
            if (sign + row == startRow + (sign*6)){
                for (ChessPiece.PieceType pieceType : pieces) {
                    ChessMove move = new ChessMove(myPosition,diag2,pieceType);
                    moves.add(move);
                }}
            else {
                ChessMove move = new ChessMove(myPosition,diag2,null);
                moves.add(move);}}

        return moves;
    }
}
