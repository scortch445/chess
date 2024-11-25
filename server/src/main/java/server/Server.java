package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import handler.Handler;
import spark.*;
import service.Service;
import websocket.WebSocketHandler;

public class Server {

    public SqlDataAccess dataAccess = null;

    public Server(){
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        try {
            dataAccess = new SqlDataAccess();
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }

        Service service = new Service(dataAccess);
        var webSocketHandler = new WebSocketHandler(service);
        Handler handler = new Handler(service);

        Spark.webSocket("/ws",webSocketHandler);

        Spark.delete("/db", handler::clear); // Clear Database
        Spark.post("/user", handler::registerUser);
        Spark.post("/session",handler::login);
        Spark.delete("/session",handler::logout);
        Spark.get("/game", handler::listGames);
        Spark.post("/game",handler::createGame);
        Spark.put("/game", handler::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void clearDatabase() throws DataAccessException{
        dataAccess.clear();
    }
}
