package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing the role of a message in a conversation.
 */
public enum Role {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    /** The string representation of the role. */
    private final String role;

    /**
     * Constructs a Role enum with the specified string representation.
     *
     * @param role the string representation of the role
     */
    Role(String role) {
        this.role = role;
    }

    /**
     * Returns the string representation of the role.
     *
     * @return the role as a string
     */
    public String getRole() {
        return role;
    }
}
