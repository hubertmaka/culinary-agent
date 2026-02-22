package pl.hubertmaka.culinaryagent.domain.enums;

/**
 * Enum representing available voices for text-to-speech conversion.
 */
public enum Voice {
    VOICE_WOMAN("hpp4J3VqNfWAUOO0d1Us"),
    VOICE_MAN("CwhRBWXzGAHq8TQ4Fs17");

    /** The string representation of the voice ID. */
    private final String voiceId;

    /**
     * Constructs a Voice enum with the specified string representation.
     *
     * @param voiceId the string representation of the voice ID
     */
    Voice(String voiceId) {
        this.voiceId = voiceId;
    }

    /**
     * Returns the string representation of the voice ID.
     *
     * @return the voice ID as a string
     */
    public String getVoiceId() { return voiceId; }
}
