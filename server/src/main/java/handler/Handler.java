package handler;

import com.google.gson.Gson;
import model.UserData;
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
        service.register(userData);

        return "{registered! (not really, still need to implement this part)}";
    }

}
