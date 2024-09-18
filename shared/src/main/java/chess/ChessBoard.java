package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPosition[][] boardSpots = new ChessPosition[8][8];
    private ChessPiece[][] piecesOnBoard = new ChessPiece[8][8];

    @Override
    public String toString() {
        return "ChessBoard{" +
                "boardSpots=" + Arrays.toString(boardSpots) +
                ", piecesOnBoard=" + Arrays.toString(piecesOnBoard) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardSpots, that.boardSpots) && Objects.deepEquals(piecesOnBoard, that.piecesOnBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(boardSpots), Arrays.deepHashCode(piecesOnBoard));
    }

    public ChessBoard() {
        for(int i=0; i< boardSpots.length; i++){
            for(int j=0; j<boardSpots[i].length; j++){
                boardSpots[i][j] = new ChessPosition(i,j);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piecesOnBoard[position.getColumn()][position.getRow()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return piecesOnBoard[position.getColumn()][position.getRow()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
