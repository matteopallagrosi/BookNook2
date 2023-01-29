package it.ispw.booknook.logic.exception;

import java.io.Serial;

public class BookNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1341581153964798000L;

    public BookNotFoundException (String message){
        super(message);
    }

    public BookNotFoundException (Throwable cause) {
        super(cause);
    }

    public BookNotFoundException (String message, Throwable cause) {
        super(message, cause);
    }
}
