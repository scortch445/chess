package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;
import server.InvalidRequest;
import server.ServerException;
import server.UnauthorizedRequest;
import service.Service;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

    static SqlDataAccess dataAccess;
    @BeforeAll
    static void init() throws ServerException {
        dataAccess = new SqlDataAccess();
        dataAccess.clear();
    }

    @BeforeEach
    void setup() throws ServerException{
        dataAccess.clear();
    }


    // DataAccess should use @ParameterizedTest with @ValueSource

    @Test
    @DisplayName("Clear Database")
    void clear(){
        assertDoesNotThrow(()->dataAccess.clear());
    }

    @Test
    @DisplayName("Save User")
    void saveUser(){
        UserData user = new UserData("test username", "test-password","test@test.com");
        assertDoesNotThrow(()->dataAccess.saveUser(user));
    }

    @Test
    @DisplayName("Get User Failure")
    void getUserBeforeCreated(){
        assertNull(assertDoesNotThrow(() -> dataAccess.getUser("ThisUserDoesn'tExist")));
    }

    @Test
    @DisplayName("Get User Success")
    void getUser(){
        UserData user = new UserData("test username", "test-password","test@test.com");
        assertDoesNotThrow(()->dataAccess.saveUser(user));

        UserData userReturned = assertInstanceOf(UserData.class, assertDoesNotThrow(()->dataAccess.getUser("test username")));
        assertEquals(user.username(), userReturned.username());
        assertEquals(user.email(), userReturned.email());
    }

    @Test
    @DisplayName("Check Hashed Password")
    void getUserEncryptedPassword(){
        UserData user = new UserData("test username", "test-password","test@test.com");
        assertDoesNotThrow(()->dataAccess.saveUser(user));

        UserData userReturned = assertInstanceOf(UserData.class, assertDoesNotThrow(()->dataAccess.getUser("test username")));
        assertEquals(user.username(), userReturned.username());
        assertEquals(user.email(), userReturned.email());
        assertNotEquals(user.password(),userReturned.password());
    }


}
