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

    @BeforeEach
    void setup() throws ServerException{
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
        var user = new UserData("Test Username", "Test Password", "Test Email");

        assertEquals(AuthData.class, assertDoesNotThrow(() -> service.register(user)).getClass());

        UserData loginDetails = new UserData("Test Username", "Test Password", null);
        assertEquals(AuthData.class, assertDoesNotThrow(()->service.login(loginDetails)).getClass());
    }

    @Test
    void logoutSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertDoesNotThrow(()->service.logout(authData.authToken()));
    }

    @Test
    void logoutUnauthorized(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertThrows(UnauthorizedRequest.class, ()->service.logout("Wrong authToken"));
    }

    @Test
    void listGamesEmpty(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertEquals(0, assertDoesNotThrow(() -> service.getGames(authData.authToken()).size()));
    }

    @Test
    void unauthorizedListGames(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertThrows(UnauthorizedRequest.class, () -> service.getGames("Invalid authToken").size());
    }

    @Test
    void createGameSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertInstanceOf(Integer.class,
                assertDoesNotThrow(()->service.createGame(authData.authToken(),"Test Game Name")));
    }

    @Test
    void unauthorizedCreateGame(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> service.register(user));

        assertThrows(UnauthorizedRequest.class, ()->service.createGame("Invalid Authentication","Test Game Name"));
    }


}
