package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
        } catch (Exception ex){
            // Send back ErrorMessage
        }

        switch(Objects.requireNonNull(command).getCommandType()){
            case CONNECT:
                var msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                connections.add(command.getGameID(),command.getAuthToken(),session);
                // Add functionality to call service and check which role the user is
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
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

        session.getRemote().sendString("Hi back to you!");
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
