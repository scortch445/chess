package service;
import dataaccess.DataAccess;
import model.UserData;
import spark.Request;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public void register(UserData userData){
        dataAccess.getUser(userData.username());
    }

}
