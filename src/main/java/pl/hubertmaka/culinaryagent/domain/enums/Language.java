package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing supported languages for recipe generation.
 */
public enum Language {
    PL("polish"),
    EN_US("english (US)"),
    EN_GB("english (GB)"),
    DE("german"),
    FR("french"),
    SP("spanish");

    /** The string representation of the language code. */
    private final String code;
    /**
     * Constructs a Language enum with the specified language code.
     *
     * @param code the string representation of the language code
     */
    Language(String code) {
        this.code = code;
    }
    /**
     * Returns the string representation of the language code.
     *
     * @return the language code as a string
     */
    public String getCode() {
        return code;
    }
}
