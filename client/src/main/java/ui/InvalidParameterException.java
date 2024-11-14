package ui;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
        super(SET_TEXT_COLOR_RED + "Invalid Parameters! "
                +SET_TEXT_COLOR_LIGHT_GREY +
                "Try typing "+ SET_TEXT_COLOR_GREEN+"help"
                + SET_TEXT_COLOR_LIGHT_GREY+ " for which parameters are needed");
    }
}
