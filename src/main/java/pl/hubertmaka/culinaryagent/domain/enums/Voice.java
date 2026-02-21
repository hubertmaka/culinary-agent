package pl.hubertmaka.culinaryagent.domain.enums;

public enum Voice {
    VOICE_WOMAN("hpp4J3VqNfWAUOO0d1Us"),
    VOICE_MAN("CwhRBWXzGAHq8TQ4Fs17");

    private final String voiceId;

    Voice(String voiceId) {
        this.voiceId = voiceId;
    }

    public String getVoiceId() {
        return voiceId;
    }
}
