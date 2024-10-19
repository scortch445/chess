package server;

public class InvalidRequest extends ServerException{
    public InvalidRequest(String message){
        super(400,message);
    }

    public InvalidRequest(int statusCode, String message) {
        super(statusCode, message);
    }

    public InvalidRequest(){
        super(400,"Error: bad request");
    }
}
