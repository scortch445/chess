package service;
import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;
import spark.Request;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData){
        if(dataAccess.getUser(userData.username())==null){
            dataAccess.saveUser(userData);

            return null;
        } else{
            return null;
        }
    }

}
