package http;

public class ResponseException extends RuntimeException {
    public final int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
