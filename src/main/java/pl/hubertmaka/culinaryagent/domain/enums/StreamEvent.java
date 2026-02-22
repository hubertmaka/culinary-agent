package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing the types of events that can occur in a streaming response.
 */
public enum StreamEvent {
    AUDIO("audio"),
    AGENT_COMPLETION("agent_completion"),
    END("end");

    /** The string representation of the event type. */
    private final String event;
    /**
     * Constructs a StreamEvent enum with the specified string representation.
     *
     * @param event the string representation of the event type
     */
    StreamEvent(String event) {
        this.event = event;
    }

    /**
     * Returns the string representation of the event type.
     *
     * @return the event type as a string
     */
    public String getEvent() {
        return event;
    }
}
