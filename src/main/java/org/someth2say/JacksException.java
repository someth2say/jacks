package org.someth2say;

public class JacksException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1643405832912214952L;

    public JacksException(final Throwable cause) {
        super(cause);
    }

    public JacksException(final String message) {
        super(message);
    }

    public JacksException(final String message, final Throwable cause) {
        super(message, cause);
    }
}