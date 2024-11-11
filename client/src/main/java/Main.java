import chess.*;
import ui.Client;

import static ui.EscapeSequences.*;

public class Main {



    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);



        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        System.out.println("♕ Welcome to the Chess ♕");
        System.out.println("For a list of commands, type "+SET_TEXT_COLOR_GREEN + "help\n");

        new Client(serverUrl).run();
    }


}