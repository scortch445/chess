package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" + pieceColor +
                "," + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new java.util.ArrayList<>(List.of());
        if(this.type==PieceType.BISHOP){
            // Check Four Diagonal Directions

            possibleMoves.addAll(checkAdjacents(board,myPosition, -1, 1));
            possibleMoves.addAll(checkAdjacents(board,myPosition, 1, -1));
            possibleMoves.addAll(checkAdjacents(board,myPosition,1,1));
            possibleMoves.addAll(checkAdjacents(board,myPosition, -1, -1));
        }
        if(this.type==PieceType.ROOK){
            possibleMoves.addAll(checkAdjacents(board,myPosition, 0, 1));
            possibleMoves.addAll(checkAdjacents(board,myPosition, 1, 0));
            possibleMoves.addAll(checkAdjacents(board,myPosition,-1,0));
            possibleMoves.addAll(checkAdjacents(board,myPosition, 0, -1));
        }

        return possibleMoves;
    }

    private Collection<ChessMove> checkAdjacents(ChessBoard board, ChessPosition myPosition, int dirX, int dirY) {
        Collection<ChessMove> possibleMoves = new java.util.ArrayList<>(List.of());
        ChessPosition currentSpot = new ChessPosition(myPosition.getRow()+dirY, myPosition.getColumn()+dirX);
//             Needs to check if the spot is empty
//             or if it is out of bounds
        while(currentSpot.isInBounds()){
            if(board.getPiece(currentSpot)!=null) {
                if(board.getPiece(currentSpot).pieceColor!=this.pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition,new ChessPosition(currentSpot.getRow(), currentSpot.getColumn()),null));
                }
                break;
            } else{
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(currentSpot.getRow(), currentSpot.getColumn()),null));
                currentSpot.offset(dirX,dirY);
            }

        }

        return possibleMoves;
    }
}
