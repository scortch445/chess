package websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

    }
    @OnWebSocketError
    public void handleError(Throwable ex){
        // TODO why does websocket throw an error with message null when the client disconnects?
        if(ex.getMessage()!=null) {
            System.err.println(ex.getMessage());
            System.err.println(ex.getStackTrace());
        }
    }
}
