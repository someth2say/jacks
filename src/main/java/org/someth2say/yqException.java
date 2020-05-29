package org.someth2say;

public class yqException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1643405832912214952L;

    public yqException(final Throwable cause) {
        super(cause);
    }

    public yqException(final String message) {
        super(message);
    }

    public yqException(final String message, final Throwable cause) {
        super(message, cause);
    }
}