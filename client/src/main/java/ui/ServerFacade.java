package ui;

import model.*;
import request.*;

import java.util.ArrayList;

public class ServerFacade {

    public ServerFacade(){

    }

    public AuthData register(UserData userData){
        return null;
    }

    public AuthData login(UserData userData) {
        return null;
    }

    public void logout(String authToken) {

    }

    public ArrayList<GameData> getGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName){
        return 0;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

}
