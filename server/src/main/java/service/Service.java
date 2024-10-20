package service;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.InvalidRequest;
import server.ServerException;
import server.UnauthorizedRequest;
import spark.Request;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws InvalidRequest {
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
        } else return null;


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

}
