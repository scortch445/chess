package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import http.ResponseException;
import ui.ChessBoardUI;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static UI.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class WebSocketFacade extends Endpoint {
    private final Session session;
    private final ChessBoardUI boardUI;
    private final ChessGame.TeamColor role;


    public WebSocketFacade(String url, ChessGame.TeamColor role) throws ResponseException {
        try{
            boardUI = new ChessBoardUI();
            this.role = role;

            url = url.replace("http","ws");
            URI socketURI = new URI(url+"/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                    @Override
                    public void onMessage(String message) {
                    var msg = new Gson().fromJson(message, ServerMessage.class);
                    if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        var gameData = new Gson().fromJson(message, LoadGameMessage.class).game;
                        boardUI.draw(gameData,role);
                    } else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        String notification = new Gson().fromJson(message, NotificationMessage.class).message;
                        System.out.println(notification);
                        System.out.println();
                    }
                    System.out.print("\n" + SET_TEXT_COLOR_LIGHT_GREY + ">>> ");
                }
            });
        } catch(DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void sendCommand(UserGameCommand command){
        try {
            session.getBasicRemote().sendText(command.toJSON());
        } catch (Exception ex){
            System.err.println(ex.getMessage());
//            ex.printStackTrace();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){

    }

    public void close() throws ResponseException {
        try{
            this.session.close();
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
