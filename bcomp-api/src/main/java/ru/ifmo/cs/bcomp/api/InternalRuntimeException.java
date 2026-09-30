package ru.ifmo.cs.bcomp.api;

/**
 *
 * @author serge
 */
public class InternalRuntimeException extends RuntimeException {
    
    private static final String MESSAGE_HEAD = "INTERNAL ERROR: ";

    public InternalRuntimeException() {
    }

    public InternalRuntimeException(String message) {
        super(MESSAGE_HEAD+message);
    }

    public InternalRuntimeException(String message, Throwable cause) {
        super(MESSAGE_HEAD+message, cause);
    }

    public InternalRuntimeException(Throwable cause) {
        super(cause);
    }

    public InternalRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE_HEAD+message, cause, enableSuppression, writableStackTrace);
    }
    
}
