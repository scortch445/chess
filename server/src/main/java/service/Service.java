package service;
import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;
import server.InvalidRequest;
import server.ServerException;
import spark.Request;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws InvalidRequest {
        if(dataAccess.getUser(userData.username())==null){
            dataAccess.saveUser(userData);

            return null;
        } else{
            throw new InvalidRequest(403, "Error: already taken");
        }
    }

}
