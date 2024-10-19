package server;

import com.google.gson.Gson;

import java.util.Map;

public class ServerException extends Exception {
    public final int statusCode;
    public ServerException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message",this.getMessage()));
    }
}
