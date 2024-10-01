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
    private ChessPiece[][] piecesOnBoard = new ChessPiece[8][8];
    private static final ChessPiece.PieceType[] initialRowSetup = {
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.ROOK
    };

    @Override
    public String toString() {
        return "ChessBoard{" +
                "piecesOnBoard=" + Arrays.deepToString(piecesOnBoard) +
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

    }


    // Copy constructor
    public ChessBoard(ChessBoard that){
        this.piecesOnBoard = that.piecesOnBoard;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piecesOnBoard[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Removes a chess piece from the chessboard
     *
     * @param position where to remove the piece from
     */
    public void removePiece(ChessPosition position){
        piecesOnBoard[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return piecesOnBoard[position.getRow()-1][position.getColumn()-1];
    }

    public ChessPosition findPiece(ChessGame.TeamColor team, ChessPiece.PieceType type){
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition checkPosition = new ChessPosition(i,j);
                ChessPiece piece = getPiece(checkPosition);
                if(piece!=null && piece.getTeamColor() == team && piece.getPieceType() == type){
                    return checkPosition;
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        piecesOnBoard = new ChessPiece[8][8];

        // White Pieces

        // First Row
        for(int i = 0; i<8; i++){
            piecesOnBoard[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, initialRowSetup[i]);
        }

        // Pawn Row
        for(int i=0; i<8; i++){
            piecesOnBoard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        // Black Pieces

        // Back Row
        for(int i = 0; i<8; i++){
            piecesOnBoard[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, initialRowSetup[i]);
        }

        // Pawn Row
        for(int i=0; i<8; i++){
            piecesOnBoard[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }
}
