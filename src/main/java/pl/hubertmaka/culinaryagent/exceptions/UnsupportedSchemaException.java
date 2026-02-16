package pl.hubertmaka.culinaryagent.exceptions;

/**
 * Exception thrown when an unsupported schema is encountered.
 */
public class UnsupportedSchemaException extends RuntimeException {
    /**
     * Constructs a new UnsupportedSchemaException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnsupportedSchemaException(String message) {
        super(message);
    }
}
