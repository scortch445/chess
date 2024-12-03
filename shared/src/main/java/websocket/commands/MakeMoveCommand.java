package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public final ChessMove move;
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }
    public ChessMove getChessMove(){
        return this.move;
    }
}
