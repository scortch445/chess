package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPosition[][] boardSpots = new ChessPosition[8][8];
    private ChessPiece[][] piecesOnBoard = new ChessPiece[8][8];

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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
