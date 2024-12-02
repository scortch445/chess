package ui;

import chess.ChessGame;
import http.ServerFacade;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;
import websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;

import java.util.*;

import static UI.EscapeSequences.*;

public class Client {
    private enum State {
        PRELOGIN,
        POSTLOGIN,
        INGAME
    }

    private final ServerFacade server;
    private WebSocketFacade ws;

    private State state;
    private final Scanner scanner;
    private boolean running;

    private AuthData authData;

    private int[] gameIDs;
    private int currentGameID;

    private ArrayList<GameData> games;

    public Client(int port){
        state = State.PRELOGIN;
        scanner = new Scanner(System.in);

        this.server = new ServerFacade(port);

        running = true;
    }


    public void run(){
        while(running){
            System.out.print(SET_TEXT_COLOR_YELLOW + "\n["+ state.toString() + "] "
                    + SET_TEXT_COLOR_LIGHT_GREY + ">>> ");
            String input = scanner.nextLine();
            try {
                var tokens = input.toLowerCase().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                switch (cmd) {
                    case "help" -> help();
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "logout" -> logout();
                    case "list" -> listGames();
                    case "create" -> createGame(params);
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "quit" -> quit();
                    case "leave" -> leave();
                    default -> throw new InvalidCommandException();
                }
            } catch (Exception ex){
                System.out.println(SET_TEXT_COLOR_RED+ex.getMessage());
            }
        }
    }

    private void help(){
        Map<Object, Object> commands = Map.of();
        switch(state){
            case State.PRELOGIN -> commands = Map.of(
                    "help","list possible commands",
                    "register <USERNAME> <PASSWORD> <EMAIL>","create a new account",
                    "login <USERNAME> <PASSWORD>","to play chess",
                    "quit","exits the program");
            case State.POSTLOGIN -> commands = Map.of(
                    "create <NAME>","a game",
                    "list","games",
                    "join <ID> [WHITE|BLACK]","a game",
                    "observe <ID>","a game",
                    "logout","when you are done",
                    "quit","playing chess",
                    "help","list possible commands"
            );
            case State.INGAME -> commands = Map.of(
                    "help","list possible commands",
                    "leave","the game (and vacate your spot)"
            );
        }
        for(var key : commands.keySet()){
            System.out.println(SET_TEXT_COLOR_GREEN+key
                    +SET_TEXT_COLOR_LIGHT_GREY+" - "
                    +SET_TEXT_COLOR_MAGENTA+commands.get(key));
        }
    }

    private void assumePreLogin(){
        if(state!=State.PRELOGIN){
            throw new InvalidCommandException();
        }
    }

    private void assumePostLogin(){
        if(state!=State.POSTLOGIN){
            throw new InvalidCommandException();
        }
    }

    private void assumeNotInGame(){
        if(state==State.INGAME){
            throw new InvalidCommandException();
        }
    }

    private void assumeInGame(){
        if(state!=State.INGAME){
            throw new InvalidCommandException();
        }
    }

    private void assumeGamesNotNull(){
        if(gameIDs==null){
            throw new InvalidCommandException(SET_TEXT_COLOR_RED + "Woah woah woah! Hold onto your horses!\n"
                    +SET_TEXT_COLOR_LIGHT_GREY +
                    "Try listing the games first using the command "+ SET_TEXT_COLOR_GREEN+"list");
        }
    }

    private int assumeValidGameChoice(String param){
        int gameChosen;
        try{
            gameChosen = Integer.parseInt(param);
            if(gameChosen>games.size() || gameChosen <= 0) {
                throw new InvalidCommandException(
                        SET_TEXT_COLOR_RED + "Silly goose! There is no game with the id: "
                                + SET_TEXT_COLOR_MAGENTA + gameChosen +
                                SET_TEXT_COLOR_LIGHT_GREY + "\nTry using the command " +
                                SET_TEXT_COLOR_GREEN + "list");
            }

            return gameChosen;
        } catch (Exception ex){
            throw new InvalidCommandException(
                    SET_TEXT_COLOR_RED+"Silly goose! There is no game with the id: "
                            +SET_TEXT_COLOR_MAGENTA + param+
                            SET_TEXT_COLOR_LIGHT_GREY+"\nTry using the command "+
                            SET_TEXT_COLOR_GREEN+"list");
        }
    }

    private void assumeParams(int expected, String... params){
        if(params.length !=expected ){
            throw new InvalidParameterException();
        }
    }

    private void quit() throws Exception {
        assumeNotInGame();
        if(state==State.PRELOGIN) {
            logout();
        }
        if (state==State.INGAME){
            ws.close();
        }

        running = false;
    }

    private void register(String... params) throws Exception {
        assumePreLogin();
        assumeParams(3,params);

        UserData user = new UserData(params[0],params[1],params[2]);
        authData = server.register(user);

        System.out.println(SET_TEXT_COLOR_GREEN+"You are successfully registered as "
                +SET_TEXT_COLOR_WHITE + authData.username());

        state = State.POSTLOGIN;
    }

    private void login(String... params) throws Exception {
        assumePreLogin();
        assumeParams(2,params);

        UserData user = new UserData(params[0],params[1],null);
        authData = server.login(user);

        System.out.println(SET_TEXT_COLOR_BLUE+"Welcome "+SET_TEXT_COLOR_WHITE + authData.username());

        state = State.POSTLOGIN;
    }

    private void logout() throws Exception {
        if(state!=State.INGAME) {
            assumePostLogin();
        }
        server.logout(authData.authToken());

        System.out.println(SET_TEXT_COLOR_BLUE + "See you later "+
                SET_TEXT_COLOR_WHITE + authData.username());

        authData = null;
        state=State.PRELOGIN;
    }

    private void listGames() throws Exception {
        assumePostLogin();

        games = server.getGames(authData.authToken());

        System.out.println(SET_TEXT_COLOR_WHITE+SET_TEXT_UNDERLINE+"\nGames:"+RESET_TEXT_UNDERLINE);
        if (games.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_BLUE+"None\nYou should try creating a game:");
            System.out.println(SET_TEXT_COLOR_GREEN+"create <NAME>"
                    +SET_TEXT_COLOR_LIGHT_GREY+" - "
                    +SET_TEXT_COLOR_MAGENTA+"a game");
        } else {
            System.out.println(SET_TEXT_COLOR_MAGENTA+SET_TEXT_ITALIC+
                    "\tNAME\t\tWHITE USER\t\tBLACK USER"+
                    RESET_TEXT_ITALIC);
            int i = 0;
            gameIDs = new int[games.size()];
            for (var game : games){
                i++;
                gameIDs[i-1] = game.gameID();
                String whiteUser = (game.whiteUsername()==null) ? "\t\t" : game.whiteUsername();
                String blackUser = (game.blackUsername()==null) ? "\t\t" : game.blackUsername();
                System.out.println(SET_TEXT_COLOR_WHITE+i+". "+SET_TEXT_COLOR_MAGENTA+
                        game.gameName()+"\t\t\t"+
                        whiteUser+"\t\t\t"+
                        blackUser);
            }
        }
    }

    private void createGame(String... params) throws Exception {
        assumePostLogin();
        assumeParams(1,params);

        server.createGame(authData.authToken(),params[0]);

        System.out.println(SET_TEXT_COLOR_WHITE+"Game "+
                SET_TEXT_COLOR_BLUE + "\"" + params[0] + "\"" + SET_TEXT_COLOR_WHITE+" created");
    }

    private void joinGame(String... params) throws Exception {
        assumePostLogin();
        assumeParams(2,params);
        assumeGamesNotNull();

        int gameChosen = assumeValidGameChoice(params[0]);
        currentGameID = gameIDs[gameChosen-1];

        ChessGame.TeamColor color;

        if(Objects.equals(params[1], "white")){
            color = ChessGame.TeamColor.WHITE;
        } else if(Objects.equals(params[1], "black")){
            color = ChessGame.TeamColor.BLACK;
        } else{
            throw new InvalidParameterException();
        }

        System.out.println(SET_TEXT_COLOR_WHITE+"Joining game...");

        var request = new JoinGameRequest(color,currentGameID,authData.authToken());

        server.joinGame(request);
        ws = new WebSocketFacade(server.baseUrl, color);
        var command = new UserGameCommand(
                UserGameCommand.CommandType.CONNECT,
                authData.authToken(),
                currentGameID);
        ws.sendCommand(command);
        state = State.INGAME;
    }

    private void observeGame(String... params) throws Exception {
        assumePostLogin();
        assumeParams(1,params);
        assumeGamesNotNull();

        int gameChosen = assumeValidGameChoice(params[0]);


        System.out.println(SET_TEXT_COLOR_WHITE+"Observing game...");
        ws = new WebSocketFacade(server.baseUrl, null);
        var command = new UserGameCommand(
                UserGameCommand.CommandType.CONNECT,
                authData.authToken(),
                gameIDs[gameChosen-1]);
        ws.sendCommand(command);

        state=State.INGAME;
    }

    private void leave() throws Exception {
        assumeInGame();

        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                authData.authToken(), currentGameID);
        ws.sendCommand(command);
    }

}
