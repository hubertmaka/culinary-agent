package pl.hubertmaka.culinaryagent.domain.enums;

public enum Role {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
