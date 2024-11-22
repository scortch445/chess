package websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(message);

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
