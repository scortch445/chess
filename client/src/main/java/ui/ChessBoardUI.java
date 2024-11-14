package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static final Map<ChessPiece.PieceType, String> PIECES_TO_CHAR = Map.of(
            ChessPiece.PieceType.KING,BLACK_KING,
            ChessPiece.PieceType.QUEEN,BLACK_QUEEN,
            ChessPiece.PieceType.BISHOP,BLACK_BISHOP,
            ChessPiece.PieceType.KNIGHT,BLACK_KNIGHT,
            ChessPiece.PieceType.ROOK,BLACK_ROOK,
            ChessPiece.PieceType.PAWN,BLACK_PAWN
    );

    public void draw(GameData data){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var game = data.game();

        out.print(ERASE_SCREEN);

        drawChessBoard(out, game);

        clearFormat(out);
    }


    private void drawChessBoard(PrintStream out, ChessGame game) {

        for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if(isLightTile(boardRow,boardCol)){
                    setWhite(out);
                } else {
                    setGrey(out);
                }
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(boardRow + 1,boardCol + 1));


                if(piece!=null){
                    printChessPiece(out, piece);
                } else{
                    out.print(EMPTY);
                }

                clearFormat(out);
            }

            out.println();

        }
    }

    private boolean isLightTile(int row, int col){
        return (row+col) % 2 == 1;
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private void clearFormat(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void printChessPiece(PrintStream out, ChessPiece piece) {
        var color = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                ? SET_TEXT_COLOR_GREEN : SET_TEXT_COLOR_MAGENTA;
        out.print(color);

        out.print(PIECES_TO_CHAR.get(piece.getPieceType()));

        setWhite(out);
    }
}
