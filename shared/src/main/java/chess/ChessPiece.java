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

    // Copy Constructor
    public ChessPiece(ChessPiece that) {
        this.pieceColor = that.pieceColor;
        this.type = that.type;
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
        if(this.type==PieceType.BISHOP || this.type==PieceType.QUEEN){
            // Check Four Diagonal Directions
            possibleMoves.addAll(checkLongPath(board,myPosition, -1, 1));
            possibleMoves.addAll(checkLongPath(board,myPosition, 1, -1));
            possibleMoves.addAll(checkLongPath(board,myPosition,1,1));
            possibleMoves.addAll(checkLongPath(board,myPosition, -1, -1));
        }
        if(this.type==PieceType.ROOK ||this.type==PieceType.QUEEN){
            // Check in four straight lines
            possibleMoves.addAll(checkLongPath(board,myPosition, 0, 1));
            possibleMoves.addAll(checkLongPath(board,myPosition, 1, 0));
            possibleMoves.addAll(checkLongPath(board,myPosition,-1,0));
            possibleMoves.addAll(checkLongPath(board,myPosition, 0, -1));
        }

        else if(this.type==PieceType.KING){
            // Check adjacent tiles
            possibleMoves.addAll(checkShortPath(board,myPosition, 0, 1));
            possibleMoves.addAll(checkShortPath(board,myPosition, 1, 0));
            possibleMoves.addAll(checkShortPath(board,myPosition, -1, 0));
            possibleMoves.addAll(checkShortPath(board,myPosition, 0, -1));

            // Check diagonal tiles
            possibleMoves.addAll(checkShortPath(board,myPosition, 1, 1));
            possibleMoves.addAll(checkShortPath(board,myPosition, 1, -1));
            possibleMoves.addAll(checkShortPath(board,myPosition, -1, 1));
            possibleMoves.addAll(checkShortPath(board,myPosition, -1, -1));
        }

        else if(this.type==PieceType.KNIGHT){
            // Check Right Side
            possibleMoves.addAll(checkShortPath(board,myPosition, 1, 2));
            possibleMoves.addAll(checkShortPath(board,myPosition, 2, 1));
            possibleMoves.addAll(checkShortPath(board,myPosition, 2, -1));
            possibleMoves.addAll(checkShortPath(board,myPosition, 1, -2));

            // Check Left Side
            possibleMoves.addAll(checkShortPath(board,myPosition, -1, 2));
            possibleMoves.addAll(checkShortPath(board,myPosition, -2, 1));
            possibleMoves.addAll(checkShortPath(board,myPosition, -2, -1));
            possibleMoves.addAll(checkShortPath(board,myPosition, -1, -2));
        }

        else if(this.type==PieceType.PAWN){
            // Check Forward
            int offsetY = pieceColor==ChessGame.TeamColor.WHITE ? 1 : -1;
            ChessPosition forwardSpot = new ChessPosition(myPosition.getRow()+offsetY, myPosition.getColumn());


            if(forwardSpot.isInBounds() &&
                    board.getPiece(forwardSpot)==null){
                addPawnPromotionMoves(possibleMoves,myPosition,forwardSpot,board);

                // If the forward spot is open, then we can check for the second
                // forward spot and if it's the first move

                // Check White initial move
                if(this.pieceColor==ChessGame.TeamColor.WHITE
                        && myPosition.getRow()==2){
                    ChessPosition newMoveSpot = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn());
                    if(board.getPiece(newMoveSpot) == null){
                        possibleMoves.add(new ChessMove(myPosition,newMoveSpot,null));
                    }
                }
                // Check Black initial move
                if(this.pieceColor==ChessGame.TeamColor.BLACK
                        && myPosition.getRow()==7){
                    ChessPosition newMoveSpot = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn());
                    if(board.getPiece(newMoveSpot) == null){
                        possibleMoves.add(new ChessMove(myPosition,newMoveSpot,null));
                    }
                }
            }

            // Check Attacks

            // Left Attack
            ChessPosition forwardLeft = new ChessPosition(forwardSpot.getRow(),forwardSpot.getColumn()-1);
            if(forwardLeft.isInBounds() &&
                    board.getPiece(forwardLeft)!=null &&
                    board.getPiece(forwardLeft).pieceColor!=this.pieceColor){
                addPawnPromotionMoves(possibleMoves,myPosition,forwardLeft,board);
            }

            // Right Attack
            ChessPosition forwardRight = new ChessPosition(forwardSpot.getRow(),forwardSpot.getColumn()+1);
            // Check if about to be promoted (Let's do this last)
            if(forwardRight.isInBounds() &&
                    board.getPiece(forwardRight)!=null &&
                    board.getPiece(forwardRight).pieceColor!=this.pieceColor){
                addPawnPromotionMoves(possibleMoves,myPosition,forwardRight,board);
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> checkLongPath(ChessBoard board, ChessPosition myPosition, int dirX, int dirY) {
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

    private Collection<ChessMove> checkShortPath(ChessBoard board, ChessPosition myPosition, int dirX, int dirY) {
        Collection<ChessMove> possibleMoves = new java.util.ArrayList<>(List.of());
        ChessPosition currentSpot = new ChessPosition(myPosition.getRow()+dirY, myPosition.getColumn()+dirX);
        if(currentSpot.isInBounds() &&
                (board.getPiece(currentSpot)==null ||
                board.getPiece(currentSpot).pieceColor!=this.pieceColor)) {
            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(currentSpot.getRow(), currentSpot.getColumn()),null));
        }
        return possibleMoves;
    }

    private void addPawnPromotionMoves(Collection<ChessMove> possibleMoves,
                                       ChessPosition myPosition,
                                       ChessPosition forwardSpot,
                                       ChessBoard board){
        // Check if it should be promoted
        if(
           ((this.pieceColor == ChessGame.TeamColor.WHITE && forwardSpot.getRow() == 8)
        || (this.pieceColor == ChessGame.TeamColor.BLACK && forwardSpot.getRow() == 1))

        ){
            for(ChessPiece.PieceType p : PieceType.values()){
                //For every piece available, add that as a possible move

                if(p != PieceType.KING && p != PieceType.PAWN){
                    possibleMoves.add(new ChessMove(myPosition,forwardSpot,p));
                }
            }
        }

        // Otherwise, just add the move without a promotion
        else {
            possibleMoves.add(new ChessMove(myPosition,forwardSpot,null));
        }

    }
}
