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

        Spark.post("/user", handler::registerUser); // Register
        Spark.get("/game", (req, res) -> ("Hello World")); // List Games

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
