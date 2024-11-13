package ui;

import http.ResponseException;
import http.ServerFacade;
import model.AuthData;
import model.UserData;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private enum State {
        PRELOGIN,
        POSTLOGIN,
        INGAME
    }

    private final ServerFacade server;

    private State state;
    private Scanner scanner;
    private boolean running;

    private AuthData authData;

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
                    case "quit" -> quit();
                    default -> throw new InvalidCommandException();
                }
            } catch(InvalidCommandException ex){
                System.out.println(SET_TEXT_COLOR_RED + "Invalid Command! "
                        +SET_TEXT_COLOR_LIGHT_GREY +
                        "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                        + SET_TEXT_COLOR_LIGHT_GREY+ " for a list of commands");
            } catch(InvalidParameterException ex){
                System.out.println(SET_TEXT_COLOR_RED + "Invalid Parameters! "
                        +SET_TEXT_COLOR_LIGHT_GREY +
                        "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                        + SET_TEXT_COLOR_LIGHT_GREY+ " for which parameters are needed");
            } catch (ResponseException ex) {
                System.out.println("["+ex.statusCode+"] "+ex.getMessage());
            } catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void help(){
        Map commands = Map.of();
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

    private void assumeParams(int expected, String... params){
        if(params.length !=expected ){
            throw new InvalidParameterException();
        }
    }

    private void quit() throws Exception {
        if(state!=State.PRELOGIN) {
            logout();
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
        assumePostLogin();
        server.logout(authData.authToken());

        System.out.println(SET_TEXT_COLOR_BLUE + "See you later "+
                SET_TEXT_COLOR_WHITE + authData.username());

        authData = null;
        state=State.PRELOGIN;
    }

    private void listGames() throws Exception {
        assumePostLogin();

        var games = server.getGames(authData.authToken());

        System.out.println(SET_TEXT_COLOR_WHITE+SET_TEXT_UNDERLINE+"\nGames:"+RESET_TEXT_UNDERLINE);
        if (games.size()==0) {
            System.out.println(SET_TEXT_COLOR_BLUE+"None\nYou should try creating a game:");
            System.out.println(SET_TEXT_COLOR_GREEN+"create <NAME>"
                    +SET_TEXT_COLOR_LIGHT_GREY+" - "
                    +SET_TEXT_COLOR_MAGENTA+"a game");
        } else {
            System.out.println(SET_TEXT_COLOR_MAGENTA+SET_TEXT_ITALIC+
                    "\tNAME\t\tID\t\tWHITE USER\t\tBLACK USER"+
                    RESET_TEXT_ITALIC);
            int i = 0;
            for (var game : games){
                i++;
                String whiteUser = (game.whiteUsername()==null) ? "\t" : game.whiteUsername();
                String blackUser = (game.blackUsername()==null) ? "\t" : game.blackUsername();
                System.out.println(SET_TEXT_COLOR_WHITE+i+". "+SET_TEXT_COLOR_MAGENTA+
                        game.gameName()+"\t\t"+
                        game.gameID()+"\t\t"+
                        whiteUser+"\t\t\t"+
                        blackUser);
            }
        }
    }

    private void createGame(String... params) throws Exception {
        assumePostLogin();
        assumeParams(1,params);

        int id = server.createGame(authData.authToken(),params[0]);

        System.out.println(SET_TEXT_COLOR_WHITE+"Game "+
                SET_TEXT_COLOR_BLUE + "\"" + params[0] + "\"" + SET_TEXT_COLOR_WHITE+" created with id: "+
                SET_TEXT_COLOR_BLUE+id);
    }

}
