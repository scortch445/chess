package websocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

import javax.websocket.Session;


@WebSocket
public class SimpleChatServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", SimpleChatServer.class);
        Spark.get("/echo/:msg",(req,res)->"HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getBasicRemote().sendText("SERVER: " + message);
    }
}
