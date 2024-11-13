package client;

import chess.ChessGame;
import http.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.JoinGameRequest;
import server.InvalidRequest;
import server.Server;
import server.UnauthorizedRequest;
import http.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        assertDoesNotThrow(()->server.clearDatabase());
        serverFacade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void resetDatabase(){
        assertDoesNotThrow(()->server.clearDatabase());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
        assertDoesNotThrow(()->server.clearDatabase());
    }

    // Use Run with Coverage to make sure all code is run and tested
    @Test
    @DisplayName("Register Test")
    void registerSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");

        assertEquals(AuthData.class, assertDoesNotThrow(() -> serverFacade.register(user)).getClass());
    }

    @Test
    @DisplayName("Register with Username Already Taken")
    void registerAlreadyTaken(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        assertDoesNotThrow(() -> serverFacade.register(user));

        assertThrows(ResponseException.class, () -> serverFacade.register(user), "Error: already taken");
    }

    @Test
    @DisplayName("Incorrect Username Login")
    void loginWrongUsername(){
        var user = new UserData("Wrong username", "password", null);

        assertThrows(ResponseException.class, () -> serverFacade.login(user), "Error: unauthorized");
    }

    @Test
    @DisplayName("Incorrect Password Login")
    void loginWrongPassword(){
        registerSuccess();
        UserData loginDetails = new UserData("Test Username", "Wrong Password", null);
        assertThrows(ResponseException.class,()->serverFacade.login(loginDetails),"Error: unauthorized");
    }

    @Test
    @DisplayName("Login Test")
    void loginSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");

        assertEquals(AuthData.class, assertDoesNotThrow(() -> serverFacade.register(user)).getClass());

        UserData loginDetails = new UserData("Test Username", "Test Password", null);
        assertEquals(AuthData.class, assertDoesNotThrow(()->serverFacade.login(loginDetails)).getClass());
    }

    @Test
    @DisplayName("Logout Test")
    void logoutSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertDoesNotThrow(()->serverFacade.logout(authData.authToken()));
    }

    @Test
    @DisplayName("Logout Unauthorized")
    void logoutUnauthorized(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertThrows(ResponseException.class, ()->serverFacade.logout("Wrong authToken"));
    }

    @Test
    @DisplayName("List Games Before Creating Any")
    void listGamesEmpty(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertEquals(0, assertDoesNotThrow(() -> serverFacade.getGames(authData.authToken()).size()));
    }

    @Test
    @DisplayName("List Games With Invalid Credentials")
    void unauthorizedListGames(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertThrows(ResponseException.class, () -> serverFacade.getGames("Invalid authToken").size());
    }

    @Test
    @DisplayName("Create Game Test")
    void createGameSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertInstanceOf(Integer.class,
                assertDoesNotThrow(()->serverFacade.createGame(authData.authToken(),"Test Game Name")));
    }

    @Test
    @DisplayName("Create Game with Invalid Credentials")
    void unauthorizedCreateGame(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertThrows(ResponseException.class, ()->serverFacade.createGame("Invalid Authentication","Test Game Name"));
    }

    @Test
    @DisplayName("List Games Test")
    void listGames(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        assertDoesNotThrow(()->serverFacade.createGame(authData.authToken(),"Test Game Name"));
        assertDoesNotThrow(()->serverFacade.createGame(authData.authToken(),"Test Game Name 2"));
        assertDoesNotThrow(()->serverFacade.createGame(authData.authToken(),"Test Game Name 3"));

        assertEquals(3, assertDoesNotThrow(() -> serverFacade.getGames(authData.authToken()).size()));
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGameSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        var gameID = assertDoesNotThrow(() -> serverFacade.createGame(authData.authToken(), "Test Game"));
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK,gameID, authData.authToken());

        assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("Join Game User Unauthorized")
    void joinGameUnauthorized(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        var gameID = assertDoesNotThrow(() -> serverFacade.createGame(authData.authToken(), "Test Game"));
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK,gameID, "Invalid AuthToken");

        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("Invalid GameID")
    void joinGameInvalidID(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        var gameID = assertDoesNotThrow(() -> serverFacade.createGame(authData.authToken(), "Test Game"));
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK,13894127, authData.authToken());

        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("Join Game Spot Taken")
    void joinGameSpotTaken(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        AuthData authData = assertDoesNotThrow(() -> serverFacade.register(user));

        var gameID = assertDoesNotThrow(() -> serverFacade.createGame(authData.authToken(), "Test Game"));
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK,gameID, authData.authToken());

        assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequest));

        var user2 = new UserData("Test Username2", "Test Password2", "Test Email2");
        AuthData authData2 = assertDoesNotThrow(() -> serverFacade.register(user2));
        var joinGameRequest2 = new JoinGameRequest(ChessGame.TeamColor.BLACK,gameID, authData2.authToken());

        assertThrows(ResponseException.class, ()-> serverFacade.joinGame(joinGameRequest2));
    }

}
