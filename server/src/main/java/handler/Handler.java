package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import server.InvalidRequest;
import server.ServerException;
import service.Service;
import service.ServiceException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public class Handler {
    private final Service service;

    public Handler(Service service){
        this.service = service;
    }

    public String clear(Request req, Response res){
        try{
            service.clear();
            return "{}";
        } catch (ServerException e){
            res.status(e.statusCode);
            return e.getMessage();
        }
    }

    public String registerUser(Request req, Response res){
        // catch the response if an exception is thrown
        var userData = new Gson().fromJson(req.body(), UserData.class);
        if(userData.username()==null || userData.password()==null || userData.email()==null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            AuthData authData = service.register(userData);
            return new Gson().toJson(Map.of("username",authData.username(),"authToken", authData.authToken()));
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }


    }

    public String login(Request req, Response res){
        var userData = new Gson().fromJson(req.body(), UserData.class);
        if(userData.username()==null || userData.password()==null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            AuthData authData = service.login(userData);
            return new Gson().toJson(Map.of("username",authData.username(),"authToken", authData.authToken()));
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }

    }

    public String logout(Request req, Response res){
        String authToken = req.headers("authorization");
        if(authToken==null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            service.logout(authToken);
            return "{}";
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }

    }

    public String listGames(Request req, Response res){
        String authToken = req.headers("authorization");
        if(authToken==null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            return new Gson().toJson(service.getGames(authToken));
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }

    }

//    private String handleExceptions(Route route){
//        try {
//            return route.handle(req,res).toString();
//        } catch(ServerException e){
//            res.status(e.statusCode);
//            return e.toJson();
//        } catch(Exception e){
//            var serverException = new ServerException(500,e.getMessage());
//            res.status(serverException.statusCode);
//            return serverException.toJson();
//        }
//    }

}
