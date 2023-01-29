package it.ispw.booknook.logic.exception;

import java.io.Serial;

public class FormatException extends Exception {

    @Serial
    private static final long serialVersionUID = 2341581167964798000L;

    public FormatException (String message){
        super(message);
    }

    public FormatException (Throwable cause) {
        super(cause);
    }

    public FormatException (String message, Throwable cause) {
        super(message, cause);
    }


}
