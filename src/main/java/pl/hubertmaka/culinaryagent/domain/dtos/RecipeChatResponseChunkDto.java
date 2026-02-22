package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.ai.audio.tts.Speech;

/**
 * DTO representing a chunk of the recipe chat response, including both audio and text data, along with metadata.
 *
 * @param audioChunk    speech data representing the audio chunk of the response, which can be streamed to the client
 * @param agentResponse the text response from the chat agent, along with any associated metadata
 */
public record RecipeChatResponseChunkDto(
    @NotNull
    Speech audioChunk,
    @NotNull
    ChatAgentResponseDto agentResponse
) { }
