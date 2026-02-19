package pl.hubertmaka.culinaryagent.domain.dtos;

/**
 * DTO representing a chunk of the recipe chat response, including both audio and text data, along with metadata.
 *
 * @param audioChunk the audio data chunk as a byte array
 * @param textChunk the corresponding text data chunk
 * @param metadata additional metadata related to the response chunk
 */
public record RecipeChatResponseChunkDto(
    byte[] audioChunk,
    String textChunk,
    MetadataDto metadata
) { }
