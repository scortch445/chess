package server;

public class ServerException extends Exception {
    public final int statusCode;
    public ServerException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }
}
