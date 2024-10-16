import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        Server server = new Server();
        System.out.println("♕ 240 Chess Server: " + server.run(8080));
    }
}