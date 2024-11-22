package websocket;

import http.ResponseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try{
            url = url.replace("http","ws");
            URI socketURI = new URI(url+"/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    System.out.println(message);
                }
            });
        } catch(DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
        try {
session.getBasicRemote().sendText("Hi!");
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void close() throws ResponseException {
        try{
            this.session.close();
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
