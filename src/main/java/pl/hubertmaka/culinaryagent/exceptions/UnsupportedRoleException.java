package pl.hubertmaka.culinaryagent.exceptions;

/**
 * Exception thrown when an unsupported role is encountered.
 */
public class UnsupportedRoleException extends RuntimeException {
    /**
     * Constructs a new UnsupportedRoleException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnsupportedRoleException(String message) {
        super(message);
    }
}
