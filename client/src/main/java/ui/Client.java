package ui;

import http.ResponseException;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private enum State {
        PRELOGIN,
        POSTLOGIN,
        INGAME
    }

    private State state;
    private Scanner scanner;
    private boolean running;

    public Client(String serverUrl){
        state = State.PRELOGIN;
        scanner = new Scanner(System.in);

        running = true;
    }


    public void run(){
        while(running){
            if(state==State.PRELOGIN){
                System.out.print(SET_TEXT_COLOR_YELLOW + "[LOGGED OUT] "
                        + SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_BLINKING + ">>> ");
                String input = scanner.nextLine();
                try {
                    var tokens = input.toLowerCase().split(" ");
                    var cmd = (tokens.length > 0) ? tokens[0] : "help";
                    var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                    switch (cmd) {
                        case "help" -> help();
                        case "quit" -> running=false;
                        default -> System.out.println(SET_TEXT_COLOR_RED + "Invalid Command! "
                                +SET_TEXT_COLOR_LIGHT_GREY +
                                "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                                + SET_TEXT_COLOR_LIGHT_GREY+ " for a list of commands");
                    }
                } catch(InvalidParameterException ex){
                    System.out.println(SET_TEXT_COLOR_RED + "Invalid Parameters! "
                            +SET_TEXT_COLOR_LIGHT_GREY +
                            "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                            + SET_TEXT_COLOR_LIGHT_GREY+ " for which parameters to use");
                } catch (ResponseException ex) {

                }

            } else if (state==State.POSTLOGIN) {

            } else if (state==State.INGAME){

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

}
