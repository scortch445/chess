package service;

import server.ServerException;

public class ServiceException extends ServerException {
    public ServiceException(String message) {
        // This Service end exception has a default Status code of 500
        super(500, message);
    }
}
