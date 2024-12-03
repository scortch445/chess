package websocket;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import ui.EscapeSequences;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@WebSocket
public class WebSocketHandler {

    private final Service service;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(Service service){
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        // System.out.println(message);
        UserGameCommand command = null;
        try{
            command = new Gson().fromJson(message, UserGameCommand.class);
            var role = service.getRole(command.getGameID(), command.getAuthToken());
            NotificationMessage notification;

            switch(Objects.requireNonNull(command).getCommandType()){
                case CONNECT:
                    var msg = new LoadGameMessage(service.getGame(command.getGameID()));
                    session.getRemote().sendString(msg.toJSON());
                    connections.add(command.getGameID(),command.getAuthToken(),session);
                    // Add functionality to call service and check which role the user is
                    notification = new NotificationMessage(
                            EscapeSequences.SET_TEXT_COLOR_WHITE+
                                    service.getUsername(command.getAuthToken())+
                                    " has joined the game as "+role);
                    connections.broadcast(command.getGameID(),command.getAuthToken(),notification);

                    break;
                case MAKE_MOVE:
                    var move = new Gson().fromJson(message, MakeMoveCommand.class).getChessMove();
                    service.makeMove(move, command.getGameID(), command.getAuthToken());
                    var updatedGame = service.getGame(command.getGameID());
                    var loadGameMessage = new LoadGameMessage(updatedGame);
                    connections.broadcast(command.getGameID(),null,loadGameMessage);
                    var promoPiece = move.getPromotionPiece()==null ? "" : " "+move.getPromotionPiece().toString();
                    notification = new NotificationMessage(
                            EscapeSequences.SET_TEXT_COLOR_WHITE+
                                    service.getUsername(command.getAuthToken())+
                                    " has made the move: "+ ChessPosition.getReadablePos(move.getStartPosition()) +
                                    " -> "+ChessPosition.getReadablePos(move.getEndPosition())+
                                    promoPiece);
                    connections.broadcast(command.getGameID(),command.getAuthToken(),notification);
                    if(updatedGame.game().isInCheck(ChessGame.TeamColor.WHITE)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_GREEN + updatedGame.whiteUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in check!");
                        connections.broadcast(command.getGameID(),null,notification);
                    } else if(updatedGame.game().isInCheck(ChessGame.TeamColor.BLACK)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_MAGENTA + updatedGame.blackUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in check!");
                        connections.broadcast(command.getGameID(),null,notification);
                    }
                    if(updatedGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_GREEN + updatedGame.whiteUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in checkmate!");
                        connections.broadcast(command.getGameID(),null,notification);
                    } else if(updatedGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_MAGENTA + updatedGame.blackUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in checkmate!");
                        connections.broadcast(command.getGameID(),null,notification);
                    }
                    if(updatedGame.game().isInStalemate(ChessGame.TeamColor.WHITE)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_GREEN + updatedGame.whiteUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in stalemate!");
                        connections.broadcast(command.getGameID(),null,notification);
                    } else if(updatedGame.game().isInStalemate(ChessGame.TeamColor.BLACK)){
                        notification = new NotificationMessage(
                                EscapeSequences.SET_TEXT_COLOR_MAGENTA + updatedGame.blackUsername()
                                        + EscapeSequences.SET_TEXT_COLOR_WHITE + " is now in stalemate!");
                        connections.broadcast(command.getGameID(),null,notification);
                    }
                    break;
                case LEAVE:
                    service.leaveGame(command.getGameID(), command.getAuthToken());
                    notification = new NotificationMessage(
                            EscapeSequences.SET_TEXT_COLOR_WHITE+
                                    service.getUsername(command.getAuthToken())+
                                    " was "+role+EscapeSequences.SET_TEXT_COLOR_WHITE+" and has left the game");
                    connections.broadcast(command.getGameID(), command.getAuthToken(), notification);
                    connections.remove(command.getGameID(), command.getAuthToken());
                    break;
                case RESIGN:
                    service.resign(command.getAuthToken(),command.getGameID());
                    notification = new NotificationMessage(
                            EscapeSequences.SET_TEXT_COLOR_WHITE+
                                    service.getUsername(command.getAuthToken())+
                                    " was "+role+EscapeSequences.SET_TEXT_COLOR_WHITE+" and has resigned");
                    connections.broadcast(command.getGameID(), null, notification);
                    break;
                default:
                    throw new IllegalStateException("Unexpected command: " + command.getCommandType());
            }
        } catch (Exception ex){
            session.getRemote().sendString(new ErrorMessage(ex.getMessage()).toJSON());
        }


    }
    @OnWebSocketError
    public void handleError(Session session, Throwable ex){
        // If the only error is that the client closed the connection, then ignore it
        if(!Objects.equals(ex.getCause().getMessage(), "Connection reset by peer")) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } else if(ex instanceof TimeoutException){

        }
    }

    @OnWebSocketClose
    public void close(Session session, int statusCode, String reason){

    }
}
