package chess;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public static final char[] COL_LETTERS = {
            'a','b','c','d','e','f','g','h'
    };

    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "[" + row +
                "," + col +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
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

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    /**
     * Change the Row
     */
    public void setRow(int row) {
        this.row=row;
    }

    /**
     * Change the Column
     */
    public void setColumn(int col) {
        this.col = col;
    }

    public void offset(int offsetCol, int offSetRow){
        this.col+=offsetCol;
        this.row+=offSetRow;
    }

    public boolean isInBounds(){
        return col > 0 && col <= 8
                && row > 0 && row <= 8;
    }

    public static String getReadablePos(ChessPosition pos){
        return COL_LETTERS[pos.getColumn()-1]+String.valueOf(pos.getRow());
    }

}
