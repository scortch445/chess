package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor whosTurnIsIt;

    public ChessGame() {
        whosTurnIsIt = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return whosTurnIsIt;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        whosTurnIsIt = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(this.board.getPiece(startPosition)==null){
            return null;
        } else{
//             TODO implement isInCheck logic
            return this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Implements the function below with the current Game's ChessBoard
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @param boardToCheck which board (simulated or current) to check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard boardToCheck) {
        ChessPosition kingPosition = boardToCheck.findPiece(teamColor, ChessPiece.PieceType.KING);
        TeamColor otherTeamColor = teamColor==TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> otherTeamsPossibleMoves = getAllPossibleMoves(otherTeamColor, boardToCheck);

        for(ChessMove move : otherTeamsPossibleMoves){
            if(move.getEndPosition().getRow() == kingPosition.getRow()
                    && move.getEndPosition().getColumn() == kingPosition.getColumn()){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> checkedTeamPossibleMoves = getAllPossibleMoves(teamColor, this.board);

        // This board is meant to test every move of the checked team
        // to see if the enemy can still kill the king after that move


        // 1) Loop through each possible move
        Collection<ChessMove> movesToGetOutOfCheck = new ArrayList<>();
        for(ChessMove move : checkedTeamPossibleMoves){
            // 2) create a board having done that move
            ChessBoard testBoard = new ChessBoard(this.board);
            ChessPiece movingPiece = testBoard.getPiece(move.getStartPosition());

            testBoard.removePiece(move.getStartPosition());
            testBoard.addPiece(move.getEndPosition(), movingPiece);

            // 3) See if we are still in check
            if(!isInCheck(teamColor, testBoard)){
                // 4) If not, add that to the movesToGetOutOfCheck ArrayList
                movesToGetOutOfCheck.add(new ChessMove(move));
            }
        }

        // If there is no way to get out of check, then return that we are in Checkmate
        return movesToGetOutOfCheck.isEmpty();

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private Collection<ChessMove> getAllPossibleMoves(TeamColor team, ChessBoard boardToCheck){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition checkPosition = new ChessPosition(i,j);
                ChessPiece piece = boardToCheck.getPiece(checkPosition);
                if(piece!=null && piece.getTeamColor() == team){
                    possibleMoves.addAll(piece.pieceMoves(boardToCheck,checkPosition));
                }
            }
        }

        return possibleMoves;
    }

    // If it is currently white's turn, make it black's turn
    // and vice versa
    private void switchWhosTurnItIs(){
        whosTurnIsIt = whosTurnIsIt == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }
}
