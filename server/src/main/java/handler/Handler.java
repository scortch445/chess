package handler;

import com.google.gson.Gson;
import model.UserData;
import server.ServerException;
import service.Service;
import service.ServiceException;
import spark.Request;
import spark.Response;

public class Handler {
    private final Service service;

    public Handler(Service service){
        this.service = service;
    }

    public String registerUser(Request req, Response res){
        // catch the response if an exception is thrown
        var userData = new Gson().fromJson(req.body(), UserData.class);
        try {
            service.register(userData);
        } catch(ServerException e){
            // TODO return the proper status code and json
            return "error";
        }

        return "{registered! (not really, still need to implement this part)}";
    }

}
