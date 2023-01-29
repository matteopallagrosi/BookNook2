package it.ispw.booknook.logic.exception;

import java.io.Serial;

public class LostConnectionException extends Exception {

    @Serial
    private static final long serialVersionUID = 1368481153964798000L;

    public LostConnectionException (String message){
        super(message);
    }

    public LostConnectionException (Throwable cause) {
        super(cause);
    }

    public LostConnectionException (String message, Throwable cause) {
        super(message, cause);
    }
}
