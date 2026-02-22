package pl.hubertmaka.culinaryagent.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechOptions;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.elevenlabs.ElevenLabsTextToSpeechOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatResponseChunkDto;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;
import pl.hubertmaka.culinaryagent.services.TextToSpeechService;
import reactor.core.publisher.Flux;

/**
 * Implementation of the TextToSpeechService using the ElevenLabs TTS model.
 * This service converts text into speech and streams the audio chunks back to the client.
 */
@Service
public class ElevenLabsTTSService implements TextToSpeechService {
    /** Logger for logging information and debugging purposes. */
    private final static Logger log = LoggerFactory.getLogger(ElevenLabsTTSService.class);
    /** The TextToSpeechModel used to perform text-to-speech conversion. */
    private final TextToSpeechModel model;
    @Value("${spring.ai.elevenlabs.tts.options.model-id}")
    private String modelId;

    /**
     * Constructor for ElevenLabsTTSService that initializes the TextToSpeechModel.
     *
     * @param model the TextToSpeechModel to be used for text-to-speech conversion, injected by Spring
     */
    public ElevenLabsTTSService(TextToSpeechModel model) {
        log.info("Creating ElevenLabs TTS service...");
        this.model = model;
    }

    /**
     * Streams the text-to-speech conversion result as a Flux of RecipeChatResponseChunkDto.
     *
     * @param text the input text to be converted to speech
     * @param voice the Voice enum representing the desired voice for text-to-speech conversion
     * @return a Flux of RecipeChatResponseChunkDto containing the audio chunks of the converted speech
     */
    @Override
    public Flux<RecipeChatResponseChunkDto> stream(String text, Voice voice) {
        log.info("Creating Flux of Recipe Chat Response...");
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, createConfiguration(voice));
        return model.stream(prompt)
            .map(m -> new RecipeChatResponseChunkDto(m.getResult().getOutput(), null));
    }

    /**
     * Retrieves the model identifier for the ElevenLabs TTS model.
     *
     * @return a String representing the model identifier
     */
    @Override
    public String getModel() {
        return modelId;
    }

    /**
     * Creates a TextToSpeechOptions configuration based on the provided voice.
     *
     * @param voice the Voice enum representing the desired voice for text-to-speech conversion
     * @return a TextToSpeechOptions object configured with the specified voice
     */
    private TextToSpeechOptions createConfiguration(Voice voice) {
        return ElevenLabsTextToSpeechOptions.builder()
            .voice(voice.getVoiceId())
            .build();
    }
}
