package pl.hubertmaka.culinaryagent.domain.enums;

public enum StreamEvent {
    AUDIO("audio"),
    AGENT_COMPLETION("agent_completion"),
    END("end");

    private final String event;

    StreamEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
