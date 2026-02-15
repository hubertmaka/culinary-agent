package pl.hubertmaka.culinaryagent.domain.enums;

public enum RecipeSource {
    URL("url"),
    IMAGE("image"),
    TEXT("text");

    private final String source;

    RecipeSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
