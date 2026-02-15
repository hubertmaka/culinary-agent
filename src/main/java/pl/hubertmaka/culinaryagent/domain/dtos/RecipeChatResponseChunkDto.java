package pl.hubertmaka.culinaryagent.domain.dtos;

public record RecipeChatResponseChunkDto(
    byte[] audioChunk,
    String textChunk,
    MetadataDto metadata
) { }
