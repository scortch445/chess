package ui;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
        super("Invalid command!");
    }
}
