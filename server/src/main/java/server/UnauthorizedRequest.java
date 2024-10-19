package server;

public class UnauthorizedRequest extends ServerException {
    public UnauthorizedRequest() {
        super(401,"Error: unauthorized");
    }
}
