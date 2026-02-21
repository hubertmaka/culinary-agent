package pl.hubertmaka.culinaryagent.exceptions;

/**
 * Exception thrown when an error occurs during recipe chat operations.
 */
public class RecipeChatException extends RuntimeException {
    /**
     * Constructs a new RecipeChatException with the specified detail message.
     *
     * @param message the detail message
     */
    public RecipeChatException(String message) {
        super(message);
    }
}
