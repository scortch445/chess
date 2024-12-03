package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    public static final Map<Character,Integer> COL_LETTER_TO_INT = Map.of(
            'a',1,
            'b',2,
            'c',3,
            'd',4,
            'e',5,
            'f',6,
            'g',7,
            'h',8
    );

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

    private static final String[] COL_HEADERS = {
            EMPTY," a ","  b "," c ","  d "," e ","  f "," g ","  h ",EMPTY
    };
    private static final String[] ROW_HEADERS = {
            " 1 "," 2 "," 3 "," 4 "," 5 "," 6 "," 7 "," 8 "
    };

    public void draw(GameData data, ChessGame.TeamColor role){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var game = data.game();

        out.println();
        out.print(ERASE_SCREEN);
        // If an observer, observe from the white POV
        if(role==null) {
            role= ChessGame.TeamColor.WHITE;
        }
        drawChessBoard(out, game, role);
        out.println();

        clearFormat(out);
    }

    public void draw(GameData data, ChessGame.TeamColor role, ArrayList<ChessPosition> positionsToHighlight){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var game = data.game();

        out.println();
        out.print(ERASE_SCREEN);
        // If an observer, observe from the white POV
        if(role==null) {
            role= ChessGame.TeamColor.WHITE;
        }
        drawChessBoard(out, game, role, positionsToHighlight);
        out.println();

        clearFormat(out);
    }


    private void drawChessBoard(PrintStream out, ChessGame game, ChessGame.TeamColor color) {
        drawColumnLabels(out);

        if(color== ChessGame.TeamColor.WHITE){
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRow(out,boardRow,game);
            }
        } else{
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                drawRow(out,boardRow,game);
            }
        }


        drawColumnLabels(out);
    }

    private void drawChessBoard(PrintStream out, ChessGame game, ChessGame.TeamColor color, ArrayList<ChessPosition> positionsToHighlight) {
        drawColumnLabels(out);

        if(color== ChessGame.TeamColor.WHITE){
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRow(out,boardRow,game, positionsToHighlight);
            }
        } else{
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                drawRow(out,boardRow,game, positionsToHighlight);
            }
        }


        drawColumnLabels(out);
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

    private void setYellow(PrintStream out){
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_YELLOW);
    }

    private void clearFormat(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void drawColumnLabels(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        for(var string : COL_HEADERS){
            out.print(string);
        }

        clearFormat(out);
        out.println();
    }

    private void drawRowLabel(PrintStream out, int row) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(ROW_HEADERS[row]);

        clearFormat(out);
    }

    private void drawRow(PrintStream out, int row, ChessGame game){
        drawRowLabel(out,row);

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if(isLightTile(row,boardCol)){
                setWhite(out);
            } else {
                setGrey(out);
            }
            ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1,boardCol + 1));


            if(piece!=null){
                printChessPiece(out, piece);
            } else{
                out.print(EMPTY);
            }

            clearFormat(out);
        }

        drawRowLabel(out,row);

        out.println();
    }

    private void drawRow(PrintStream out, int row, ChessGame game, ArrayList<ChessPosition> positionsToHighlight){
        drawRowLabel(out,row);

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            var pos = new ChessPosition(row+1,boardCol+1);
            if (positionsToHighlight.contains(pos)) {
                setYellow(out);
            }
            else if(isLightTile(row,boardCol)){
                setWhite(out);
            } else {
                setGrey(out);
            }
            ChessPiece piece = game.getBoard().getPiece(pos);


            if(piece!=null){
                printChessPiece(out, piece);
            } else{
                out.print(EMPTY);
            }

            clearFormat(out);
        }

        drawRowLabel(out,row);

        out.println();
    }

    private void printChessPiece(PrintStream out, ChessPiece piece) {
        var color = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                ? SET_TEXT_COLOR_GREEN : SET_TEXT_COLOR_MAGENTA;
        out.print(color);

        out.print(PIECES_TO_CHAR.get(piece.getPieceType()));

        setWhite(out);
    }
}
