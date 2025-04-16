package chess;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private static HashMap<Integer,String> letters = new HashMap<>();

    private int row;
    private int col;


    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
        mapLetters();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    private static void mapLetters(){
        letters.put(0, "a");
        letters.put(1, "b");
        letters.put(2, "c");
        letters.put(3, "d");
        letters.put(4, "e");
        letters.put(5, "f");
        letters.put(6, "g");
        letters.put(7, "h");
    }

    public static String stringPosition(ChessPosition position){
        return letters.get(position.getColumn()-1) + position.getRow();
    }

    public static HashMap<Integer,String> getLetterMap(){
        return letters;
    }
}
