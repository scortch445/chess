package ui;

import http.ResponseException;
import http.ServerFacade;
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

    public Client(int port){
        state = State.PRELOGIN;
        scanner = new Scanner(System.in);

        this.server = new ServerFacade(port);

        running = true;
    }


    public void run(){
        while(running){
            System.out.print(SET_TEXT_COLOR_YELLOW + "["+ state.toString() + "] "
                    + SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_BLINKING + ">>> ");
            String input = scanner.nextLine();
            try {
                var tokens = input.toLowerCase().split(" ");
                var cmd = (tokens.length > 0) ? tokens[0] : "help";
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                switch (cmd) {
                    case "help" -> help();
                    case "register" -> register(params);
                    case "quit" -> running=false;
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

    private void register(String... params) throws Exception {
        assumePreLogin();
        if(params.length !=3 ){
            throw new InvalidParameterException();
        }

        UserData user = new UserData(params[0],params[1],params[2]);
        server.register(user);


    }

}
