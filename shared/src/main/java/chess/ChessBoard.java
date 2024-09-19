package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    // boardSpots[row][col]
    private final ChessPosition[][] boardSpots = new ChessPosition[8][8];
    private ChessPiece[][] piecesOnBoard = new ChessPiece[8][8];

    @Override
    public String toString() {
        return "ChessBoard{" +
                "boardSpots=" + Arrays.deepToString(boardSpots) +
                ", piecesOnBoard=" + Arrays.deepToString(piecesOnBoard) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(piecesOnBoard, that.piecesOnBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(piecesOnBoard);
    }

    public ChessBoard() {
        for(int i=0; i< boardSpots.length; i++){
            for(int j=0; j<boardSpots[i].length; j++){
                boardSpots[i][j] = new ChessPosition(i+1,j+1);
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
        piecesOnBoard[position.getColumn()-1][position.getRow()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return piecesOnBoard[position.getColumn()-1][position.getRow()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        piecesOnBoard = new ChessPiece[8][8];

        // White Pieces

        // First Row
        addPiece(boardSpots[0][0],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(boardSpots[0][1],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(boardSpots[0][2],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(boardSpots[0][3],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(boardSpots[0][4],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(boardSpots[0][5],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(boardSpots[0][6],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(boardSpots[0][7],new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        // Pawn Row
        for(int i=0; i<8; i++){
            addPiece(boardSpots[1][i], new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        // Black Pieces

        // Back Row
        addPiece(boardSpots[7][0],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(boardSpots[7][1],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(boardSpots[7][2],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(boardSpots[7][3],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(boardSpots[7][4],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(boardSpots[7][5],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(boardSpots[7][6],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(boardSpots[7][7],new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        // Pawn Row
        for(int i=0; i<8; i++){
            addPiece(boardSpots[6][i], new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }
}
