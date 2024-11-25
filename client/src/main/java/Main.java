import ui.Client;

import static UI.EscapeSequences.*;

public class Main {



    public static void main(String[] args) {
        var port = 8080;

        System.out.println("♕ Welcome to the Chess ♕");
        System.out.println("For a list of commands, type "+SET_TEXT_COLOR_GREEN + "help");

        new Client(port).run();
    }


}