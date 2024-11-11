package ui;

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
                System.out.print(SET_TEXT_COLOR_YELLOW + "[LOGGED OUT] " + SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_BLINKING + ">>> ");
                String input = scanner.nextLine();

                System.out.println("Inputted: " + input);

            } else if (state==State.POSTLOGIN) {

            } else if (state==State.INGAME){

            }
        }
    }

}
