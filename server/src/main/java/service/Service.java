package service;
import UI.EscapeSequences;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.JoinGameRequest;
import server.InvalidRequest;
import server.ServerException;
import server.UnauthorizedRequest;

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
        if(usr==null|| !BCrypt.checkpw(userData.password(),usr.password())){
            throw new UnauthorizedRequest();
        } else{
            AuthData authData = createAuth(usr.username());
            dataAccess.saveAuth(authData);
            return authData;
        }
    }

    public void logout(String authToken) throws ServerException {
        if(authorized(authToken)){
            AuthData authData = dataAccess.getAuth(authToken);
            dataAccess.deleteAuth(authData);
        }
    }

    public ArrayList<GameData> getGames(String authToken) throws  ServerException {
        if(authorized(authToken)){
            return dataAccess.getGames();
        } else {
            return null;
        }


    }

    public int createGame(String authToken, String gameName) throws ServerException{
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

    public String getRole(int gameID, String authToken) throws ServerException{
        String username = dataAccess.getAuth(authToken).username();
        var game = dataAccess.getGame(gameID);
        if(Objects.equals(game.blackUsername(), username)){
            return EscapeSequences.SET_TEXT_COLOR_MAGENTA+"BLACK";
        } else if(Objects.equals(game.whiteUsername(), username)){
            return EscapeSequences.SET_TEXT_COLOR_GREEN+"WHITE";
        } else{
            return EscapeSequences.SET_TEXT_COLOR_BLUE+"OBSERVER";
        }
    }
    public String getUsername(String authToken) throws ServerException{
        return dataAccess.getAuth(authToken).username();
    }
    public GameData getGame(int gameID) throws ServerException{
        return dataAccess.getGame(gameID);
    }
    public void leaveGame(int gameID, String authToken) throws ServerException{
        String username = getUsername(authToken);
        var game = dataAccess.getGame(gameID);
        GameData updatedGame;
        if(Objects.equals(game.whiteUsername(), username)){
            updatedGame = new GameData(
                    game.gameID(),null,game.blackUsername(),game.gameName(),game.game());
        } else if(Objects.equals(game.blackUsername(), username)){
            updatedGame = new GameData(
                    game.gameID(),game.whiteUsername(),null,game.gameName(),game.game());
        } else{
            updatedGame = game;
        }
        dataAccess.saveGame(updatedGame);
    }

    public void makeMove(ChessMove move, int gameID, String authToken) throws Exception {
        var gameData = this.getGame(gameID);
        var username = this.getUsername(authToken);
        if(Objects.equals(getRole(gameID, authToken), EscapeSequences.SET_TEXT_COLOR_BLUE + "OBSERVER")){
            throw new InvalidMoveException("You are an observer! You cannot make a move");
        }
        var userRole =
                (Objects.equals(gameData.whiteUsername(), username)) ? ChessGame.TeamColor.WHITE
                        : ChessGame.TeamColor.BLACK;
        if(gameData.game().getTeamTurn()!=userRole){
            throw new InvalidRequest("It's not your turn!");
        }
        gameData.game().makeMove(move);

        dataAccess.saveGame(gameData);
    }

    public void resign(String authToken, int gameID) throws Exception {
        var username = getUsername(authToken);
        var gameData = getGame(gameID);

        if(!Objects.equals(username, gameData.whiteUsername())
                || !Objects.equals(username, gameData.blackUsername())){
            throw new InvalidRequest("You cannot resign as an observer!");
        }
        // TODO lock game and save to database

    }

    private AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

    private boolean authorized(String authToken) throws ServerException{
        if(dataAccess.getAuth(authToken)==null){
            throw new UnauthorizedRequest();
        } else {
            return true;
        }
    }

    private int getNextGameID() throws ServerException {
        while(dataAccess.getGame(nextGameID)!=null){
            nextGameID++;
        }
        return nextGameID;
    }

}
