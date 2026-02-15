package pl.hubertmaka.culinaryagent.domain.enums;

public enum Language {
    POLISH("pl"),
    ENGLISH("en"),
    GERMAN("de"),
    FRENCH("fr"),
    SPANISH("es");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
