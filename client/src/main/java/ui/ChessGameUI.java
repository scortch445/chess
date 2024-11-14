package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessGameUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 0;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static final Map<ChessPiece.PieceType, String> PIECES_TO_CHAR = Map.of(
            ChessPiece.PieceType.KING,BLACK_KING,
            ChessPiece.PieceType.QUEEN,BLACK_QUEEN,
            ChessPiece.PieceType.BISHOP,BLACK_BISHOP,
            ChessPiece.PieceType.KNIGHT,BLACK_KNIGHT,
            ChessPiece.PieceType.ROOK,BLACK_ROOK,
            ChessPiece.PieceType.PAWN,BLACK_PAWN
    );

    public void draw(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawTicTacToeBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private void drawTicTacToeBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);
        }
    }

    private void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);

                int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                int suffixLength = 0;

                out.print(EMPTY.repeat(prefixLength));
                printChessPiece(out, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                out.print(EMPTY.repeat(suffixLength));

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }

                setBlack(out);
            }

            out.println();
        }
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void printChessPiece(PrintStream out, ChessPiece piece) {
        var color = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                ? SET_TEXT_COLOR_YELLOW : SET_TEXT_COLOR_MAGENTA;
        out.print(color);

        out.print(PIECES_TO_CHAR.get(piece.getPieceType()));

        setWhite(out);
    }
}
