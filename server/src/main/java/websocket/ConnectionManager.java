package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // Maps GameIDs to the authToken-connections they have
    public final ConcurrentHashMap<Integer,
            ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        if(!connections.containsKey(gameID)){
            connections.put(gameID,new ConcurrentHashMap<>());
        }
        var connection = new Connection(authToken, session);
        connections.get(gameID).put(authToken, connection);
    }

    public void remove(int gameID, String authToken) {
        connections.get(gameID).remove(authToken);
    }

    public void broadcast(int gameID, String excludeAuthToken, ServerMessage msg) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.get(gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(msg.toJSON());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.get(gameID).remove(c.authToken);
        }
    }
}