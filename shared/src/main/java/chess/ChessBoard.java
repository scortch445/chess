package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPosition[][] boardSpots = new ChessPosition[8][8];
    private ChessPiece[][] piecesOnBoard = new ChessPiece[8][8];

    private ChessPiece.PieceType[] starterPieces = {
            ChessPiece.PieceType.KING,
            ChessPiece.PieceType.QUEEN,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.KNIGHT,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.BISHOP,
            ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.ROOK,

            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,

            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN,
            ChessPiece.PieceType.PAWN
            };

    /**
     * Number of white pieces off the board of each type:
     * King,Queen,Knight,Bishop,Rook,Pawn
     */
    private ArrayList<ChessPiece.PieceType> whitePieces;

    /**
     * Number of black pieces off the board of each type:
     * King,Queen,Knight,Bishop,Rook,Pawn
     */
    private ArrayList<ChessPiece.PieceType> blackPieces;

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

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        whitePieces.addAll(List.of(starterPieces));
        blackPieces.addAll(List.of(starterPieces));
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piecesOnBoard[position.getColumn()-1][position.getRow()-1] = piece;
//        TODO take away from whitePieces or blackPieces
        Collection<ChessPiece.PieceType> teamToRemoveFrom = piece.getTeamColor()== ChessGame.TeamColor.WHITE ? whitePieces : blackPieces;
        teamToRemoveFrom.remove(piece.getPieceType());
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

    public Collection<ChessPiece.PieceType> promotionPiecesAvailable(ChessGame.TeamColor color){
        Collection<ChessPiece.PieceType> piecesAvailable = new ArrayList<>();
        Collection<ChessPiece.PieceType> piecesToCheck = color == ChessGame.TeamColor.WHITE ? whitePieces : blackPieces;
        if(color == ChessGame.TeamColor.WHITE){
            if(piecesToCheck.contains(ChessPiece.PieceType.QUEEN)){
                piecesAvailable.add(ChessPiece.PieceType.QUEEN);
            }
            if(piecesToCheck.contains(ChessPiece.PieceType.KNIGHT)){
                piecesAvailable.add(ChessPiece.PieceType.KNIGHT);
            }
            if(piecesToCheck.contains(ChessPiece.PieceType.BISHOP)){
                piecesAvailable.add(ChessPiece.PieceType.BISHOP);
            }
            if(piecesToCheck.contains(ChessPiece.PieceType.ROOK)){
                piecesAvailable.add(ChessPiece.PieceType.ROOK);
            }
        }
        return piecesAvailable;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
