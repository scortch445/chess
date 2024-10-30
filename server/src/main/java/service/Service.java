package service;
import chess.ChessGame;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import server.InvalidRequest;
import server.ServerException;
import server.UnauthorizedRequest;
import spark.Request;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;
    private int nextGameID;

    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
        nextGameID = 1;
    }

    public AuthData register(UserData userData) throws ServerException {
        if(dataAccess.getUser(userData.username())==null){
            dataAccess.saveUser(userData);

            AuthData authData = createAuth(userData.username());
            dataAccess.saveAuth(authData);

            return authData;
        } else{
            throw new InvalidRequest(403, "Error: already taken");
        }
    }

    public void clear() throws ServerException {
        dataAccess.clear();
    }

    public AuthData login(UserData userData) throws ServerException {

        UserData usr = dataAccess.getUser(userData.username());
        if(usr==null|| !Objects.equals(usr.password(), userData.password())){
            throw new UnauthorizedRequest();
        } else{
            AuthData authData = createAuth(usr.username());
            dataAccess.saveAuth(authData);
            return authData;
        }
    }

    public void logout(String authToken) throws UnauthorizedRequest {
        if(authorized(authToken)){
            AuthData authData = dataAccess.getAuth(authToken);
            dataAccess.deleteAuth(authData);
        }
    }

    public ArrayList<GameData> getGames(String authToken) throws  UnauthorizedRequest {
        if(authorized(authToken)){
            return dataAccess.getGames();
        } else {
            return null;
        }


    }

    public int createGame(String authToken, String gameName) throws UnauthorizedRequest{
        if(authorized(authToken)){
            int gameID = getNextGameID();
            GameData game = new GameData(gameID,null,null,gameName,new ChessGame());
            dataAccess.createGame(game);

            return gameID;
        } else {
            return -1;
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ServerException{
        if(authorized(joinGameRequest.authToken())) {
            GameData game = dataAccess.getGame(joinGameRequest.gameID());
            if(game == null) {
                throw new InvalidRequest();
            }
            var user = dataAccess.getAuth(joinGameRequest.authToken());
            if(joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                if(game.whiteUsername()!=null) {
                    throw new InvalidRequest(403, "Error: already taken");
                }
                // Copy the game and update the whiteUsername
                var newGame = new GameData(game.gameID(),
                        user.username(),
                        game.blackUsername(),
                        game.gameName(),
                        game.game());
                dataAccess.saveGame(newGame);
            } else {
                if(game.blackUsername()!=null) {
                    throw new InvalidRequest(403, "Error: already taken");
                }
                // Copy the game and update the blackUsername
                var newGame = new GameData(game.gameID(),
                        game.whiteUsername(),
                        user.username(),
                        game.gameName(),
                        game.game());
                dataAccess.saveGame(newGame);
            }
        }
    }

    private AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

    private boolean authorized(String authToken) throws UnauthorizedRequest{
        if(dataAccess.getAuth(authToken)==null){
            throw new UnauthorizedRequest();
        } else {
            return true;
        }
    }

    private int getNextGameID(){
        return nextGameID++;
    }

}
