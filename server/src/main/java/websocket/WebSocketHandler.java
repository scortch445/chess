package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import UI.EscapeSequences;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final Service service;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(Service service){
        this.service = service;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(message);
        UserGameCommand command = null;
        try{
            command = new Gson().fromJson(message, UserGameCommand.class);

            switch(Objects.requireNonNull(command).getCommandType()){
                case CONNECT:
                    var msg = new LoadGameMessage(service.getGame(command.getGameID()));
                    connections.add(command.getGameID(),command.getAuthToken(),session);
                    // Add functionality to call service and check which role the user is
                    var role = service.getRole(command.getGameID(), command.getAuthToken());
                    var notification = new NotificationMessage(
                            EscapeSequences.SET_TEXT_COLOR_WHITE+
                                    service.getUsername(command.getAuthToken())+
                                    " has joined the game as "+role);
                    connections.broadcast(command.getGameID(),command.getAuthToken(),notification);

                    session.getRemote().sendString(msg.toJSON());
                    break;
                case MAKE_MOVE:
                    break;
                case LEAVE:
                    break;
                case RESIGN:
                    break;
                default:
                    throw new IllegalStateException("Unexpected command: " + command.getCommandType());
            }
        } catch (Exception ex){
            session.getRemote().sendString(new ErrorMessage(ex.getMessage()).toJSON());
        }


    }
    @OnWebSocketError
    public void handleError(Throwable ex){
        // If the only error is that the client closed the connection, then ignore it
        if(!Objects.equals(ex.getCause().getMessage(), "Connection reset by peer")) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void close(Session session, int statusCode, String reason){

    }
}
