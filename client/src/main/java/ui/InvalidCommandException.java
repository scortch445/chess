package ui;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
        super(SET_TEXT_COLOR_RED + "Invalid Command! "
                +SET_TEXT_COLOR_LIGHT_GREY +
                "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                + SET_TEXT_COLOR_LIGHT_GREY+ " for a list of commands");
    }

    public InvalidCommandException(String msg){
        super(msg);
    }
}
