package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import server.InvalidRequest;
import server.ServerException;
import server.UnauthorizedRequest;
import service.Service;

public class ServiceTests {
    private static Service service;
    // Options: @BeforeAll, @BeforeEach, @AfterEach, @AfterAll
    // @Test


    @BeforeAll
    static void init() throws ServerException {
        MemoryDataAccess memoryDataAccess = new MemoryDataAccess();
        service = new Service(memoryDataAccess);
        service.clear();
    }


    // DataAccess should use @ParameterizedTest with @ValueSource

    @Test
    void clear(){
        assertDoesNotThrow(()->service.clear());
    }

    // Use Run with Coverage to make sure all code is run and tested
    @Test
    void registerSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");

        assertEquals(AuthData.class, assertDoesNotThrow(() -> service.register(user)).getClass());
    }

    @Test
    void registerAlreadyTaken(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        assertDoesNotThrow(() -> service.register(user));

        assertThrows(InvalidRequest.class, () -> service.register(user), "Error: already taken");
    }

    @Test
    void loginWrongUsername(){
        var user = new UserData("Wrong username", "password", null);

        assertThrows(UnauthorizedRequest.class, () -> service.login(user));
    }

    @Test
    void loginWrongPassword(){
        registerSuccess();
        UserData loginDetails = new UserData("Test Username", "Wrong Password", null);
        assertThrows(UnauthorizedRequest.class,()->service.login(loginDetails));
    }

    @Test
    void loginSuccess(){
        registerSuccess();
        UserData loginDetails = new UserData("Test Username", "Wrong Password", null);
        assertEquals(AuthData.class, assertDoesNotThrow(()->service.login(loginDetails)).getClass());
    }


}
