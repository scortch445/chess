package dataaccess;

import server.ServerException;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ServerException {

    public DataAccessException(int statusCode, String message) {
        super(statusCode, message);
    }
}
