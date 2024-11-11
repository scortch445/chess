package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import server.InvalidRequest;
import server.ServerException;
import service.Service;
import spark.Request;
import spark.Response;

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
            return new Gson().toJson(Map.of("games",service.getGames(authToken)));
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }

    }

    public String createGame(Request req, Response res){
        String authToken = req.headers("authorization");
        String gameName = new Gson().fromJson(req.body(), GameData.class).gameName();
        if(authToken==null || gameName==null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            return new Gson().toJson(Map.of("gameID",service.createGame(authToken,gameName)));
        } catch(ServerException e){
            res.status(e.statusCode);
            return e.toJson();
        } catch(Exception e){
            var serverException = new ServerException(500,e.getMessage());
            res.status(serverException.statusCode);
            return serverException.toJson();
        }
    }

    public String joinGame(Request req, Response res){
        // clean up code in future
        JoinGameRequest joinGameRequestWithoutAuth = new Gson().fromJson(req.body(),JoinGameRequest.class);
        String authToken = req.headers("authorization");
        JoinGameRequest joinGameRequest =
                new JoinGameRequest(
                        joinGameRequestWithoutAuth.playerColor(), joinGameRequestWithoutAuth.gameID(), authToken);

        if(joinGameRequest.authToken()==null || joinGameRequest.gameID()<=0 || joinGameRequest.playerColor() == null){
            var e = new InvalidRequest();
            res.status(e.statusCode);
            return e.toJson();
        }

        try {
            service.joinGame(joinGameRequest);
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


    // X don't pass in a function, instead, let it pass if it succeeds
    // Then I can shorten/simplify the repeated co de here
    // Not gonna work, cuz it needs to try the function

}
