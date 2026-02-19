package pl.hubertmaka.culinaryagent.exceptions;

/**
 * Exception thrown when an error occurs during recipe extraction.
 */
public class RecipeExtractionException extends RuntimeException {
    /**
     * Constructs a new RecipeExtractionException with the specified detail message.
     *
     * @param message the detail message
     */
    public RecipeExtractionException(String message) {
        super(message);
    }
}
