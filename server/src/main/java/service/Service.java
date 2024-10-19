package service;
import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;
import server.InvalidRequest;
import server.ServerException;
import spark.Request;

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

    public void clear(){
        dataAccess.clear();
    }

    private AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        return new AuthData(username, authToken);
    }

}
