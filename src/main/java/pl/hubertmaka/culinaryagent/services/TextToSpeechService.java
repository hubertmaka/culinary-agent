package pl.hubertmaka.culinaryagent.services;

import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatResponseChunkDto;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;
import reactor.core.publisher.Flux;

/**
 * Service interface for handling text-to-speech interactions.
 */
public interface TextToSpeechService {
    /**
     * Converts the given text to speech and returns a stream of audio chunks.
     *
     * @param text  The text to be converted to speech.
     * @param voice The voice to be used for the text-to-speech conversion.
     * @return A Flux stream of RecipeChatResponseChunkDto containing audio chunks.
     */
    public Flux<RecipeChatResponseChunkDto> stream(String text, Voice voice);
    /**
     * Retrieves the model identifier for the text-to-speech service.
     *
     * @return A String representing the model identifier.
     */
    public String getModel();
}
