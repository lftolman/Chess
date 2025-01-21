package chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalc implements PieceMovesCalculator {

        public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> moves = new ArrayList<>();
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            for (int i = 1; i > -2; i = i-2) {
                for (int j = 1; j > -2; j = j-2) {
                    RookMoveCalc.updateStep(board, myPosition, moves, row, col, i, j);

                }
            }

        return moves;}
}
