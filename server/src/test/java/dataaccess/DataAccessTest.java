package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
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

import java.util.List;

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

    @Test
    @DisplayName("Auth Data Null")
    void getAuthFailure(){
        assertNull(assertDoesNotThrow(()->dataAccess.getAuth("This Auth Token Doesn't Exist!")));
    }

    @Test
    @DisplayName("getAuth Success")
    void getAuth(){
        AuthData authData = new AuthData("testAuthToken","test-username");
        assertDoesNotThrow(()->dataAccess.saveAuth(authData));

        AuthData authDataReturned = assertInstanceOf(AuthData.class,
                assertDoesNotThrow(()->dataAccess.getAuth("testAuthToken")));

        assertEquals(authData.authToken(),authDataReturned.authToken());
    }

    @Test
    @DisplayName("Delete Auth Success")
    void deleteAuth(){
        AuthData authData = new AuthData("testAuthToken","test-username");
        assertDoesNotThrow(()->dataAccess.saveAuth(authData));

        AuthData authDataReturned = assertInstanceOf(AuthData.class,
                assertDoesNotThrow(()->dataAccess.getAuth("testAuthToken")));

        assertEquals(authData.authToken(),authDataReturned.authToken());

        assertDoesNotThrow(()->dataAccess.deleteAuth(authData));
        assertNull(assertDoesNotThrow(()->dataAccess.getAuth(authData.authToken())));
    }

    @Test
    @DisplayName("Get Empty List of Games")
    void getEmptyGames(){
        assertNull(assertDoesNotThrow(()->dataAccess.getGames()));
    }

    @Test
    @DisplayName("Get List of created Games")
    void createAndListGames(){
        assertNull(assertDoesNotThrow(()->dataAccess.getGames()));

        GameData gameData = new GameData(
                1,"testUsername","testUsername2",
                "testGameName", new ChessGame());

        assertDoesNotThrow(()->dataAccess.createGame(gameData));

        assertNotNull(assertDoesNotThrow(()->dataAccess.getGames()));

        GameData gameData2 = new GameData(
                2,"testUsername3","testUsername4",
                "testGameName2", new ChessGame());
        GameData gameData3 = new GameData(
                3,"testUsername5","testUsername6",
                "testGameName3", new ChessGame());

        assertDoesNotThrow(()->dataAccess.createGame(gameData2));
        assertDoesNotThrow(()->dataAccess.createGame(gameData3));

        assertEquals(3, assertDoesNotThrow(()->dataAccess.getGames()).size());
    }

    @Test
    @DisplayName("Get specific games")
    void getSpecificGame(){
        assertNull(assertDoesNotThrow(()->dataAccess.getGames()));

        GameData gameData = new GameData(
                1,"testUsername","testUsername2",
                "testGameName", new ChessGame());
        GameData gameData2 = new GameData(
                2,"testUsername3","testUsername4",
                "testGameName2", new ChessGame());
        GameData gameData3 = new GameData(
                3,"testUsername5","testUsername6",
                "testGameName3", new ChessGame());

        assertDoesNotThrow(()->dataAccess.createGame(gameData));
        assertDoesNotThrow(()->dataAccess.createGame(gameData2));
        assertDoesNotThrow(()->dataAccess.createGame(gameData3));

        assertEquals(gameData,assertDoesNotThrow(()->dataAccess.getGame(1)));
        assertEquals(gameData2,assertDoesNotThrow(()->dataAccess.getGame(2)));
        assertEquals(gameData3,assertDoesNotThrow(()->dataAccess.getGame(3)));
    }

    @Test
    @DisplayName("Update Game")
    void updateGame(){
        assertNull(assertDoesNotThrow(()->dataAccess.getGames()));
        ChessGame game = new ChessGame();

        GameData gameData = new GameData(
                1,null,null,
                "testGameName", new ChessGame());

        assertDoesNotThrow(()->dataAccess.createGame(gameData));

        var move = new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null);
        assertDoesNotThrow(()->game.makeMove(move));

        GameData updatedGame = new GameData(1,null,"testUsername2",
                "testGameName",   game);

        assertDoesNotThrow(()->dataAccess.saveGame(updatedGame));

        var returnedGame = assertDoesNotThrow(()->dataAccess.getGame(1));
        assertEquals(updatedGame,returnedGame);
    }


}
