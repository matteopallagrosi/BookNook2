package it.ispw.booknook.logic.exception;

import java.io.Serial;

public class InvalidDateException extends Exception {

    @Serial
    private static final long serialVersionUID = 2341581167964798000L;

    public InvalidDateException (String message){
        super(message);
    }

    public InvalidDateException (Throwable cause) {
        super(cause);
    }

    public InvalidDateException (String message, Throwable cause) {
        super(message, cause);
    }
}
