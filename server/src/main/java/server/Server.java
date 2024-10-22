package server;

import dataaccess.MemoryDataAccess;
import handler.Handler;
import spark.*;
import service.Service;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        MemoryDataAccess memoryDataAccess = new MemoryDataAccess();
        Service service = new Service(memoryDataAccess);
        Handler handler = new Handler(service);

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
}
