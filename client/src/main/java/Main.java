import chess.*;

import java.util.Scanner;

public class Main {

    private enum State {
        PRELOGIN,
        POSTLOGIN,
        INGAME
    }

    private static State state;
    private static Scanner scanner;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        state = State.PRELOGIN;
        scanner = new Scanner(System.in);

        System.out.println("♕ 240 Chess Client: " + piece);

        runREPL();
    }

    private static void runREPL(){
        while(true){
            if(state==State.PRELOGIN){
                System.out.print("[LOGGED OUT] >>> ");
                String output = scanner.nextLine();

                System.out.println("Inputted: " + output);

            } else if (state==State.POSTLOGIN) {

            } else if (state==State.INGAME){

            }
        }
    }
}