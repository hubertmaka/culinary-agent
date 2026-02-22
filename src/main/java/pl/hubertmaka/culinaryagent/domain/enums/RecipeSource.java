package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing the source of a recipe.
 */
public enum RecipeSource {
    URL("url"),
    IMAGE("image"),
    TEXT("text");
    /** The string representation of the recipe source. */
    private final String source;

    /**
     * Constructs a RecipeSource enum with the specified string representation.
     *
     * @param source the string representation of the recipe source
     */
    RecipeSource(String source) { this.source = source; }

    /** Returns the string representation of the recipe source. */
    public String getSource() { return source; }
}
