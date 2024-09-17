package chess;

import java.util.Collection;
import java.util.List;

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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = List.of();
        if(this.type==PieceType.BISHOP){
            // Check Four Diagonal Directions


            // TODO make this a function that can be called by Bishop
            ChessPosition currentSpot = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            // Needs to check if the spot is empty
            // or if it is
            while(board.getPiece(currentSpot)==null){
                possibleMoves.add(new ChessMove(myPosition,currentSpot,type));
                currentSpot.setColumn(currentSpot.getColumn()+1);
                currentSpot.setRow(currentSpot.getRow()+1);
            }
        }
        return possibleMoves;
    }
}
