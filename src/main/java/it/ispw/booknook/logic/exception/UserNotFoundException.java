package it.ispw.booknook.logic.exception;

import java.io.Serial;

public class UserNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1341581160964798000L;

    public UserNotFoundException (String message){
        super(message);
    }

    public UserNotFoundException (Throwable cause) {
        super(cause);
    }

    public UserNotFoundException (String message, Throwable cause) {
        super(message, cause);
    }






}
