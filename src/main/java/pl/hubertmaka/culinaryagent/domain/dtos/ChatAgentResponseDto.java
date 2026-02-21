package pl.hubertmaka.culinaryagent.domain.dtos;

/**
 * Data Transfer Object representing the response from a chat agent.
 *
 * @param content  The content of the response message.
 * @param metadata Additional metadata associated with the response.
 */
public record ChatAgentResponseDto(
        String content,
        MetadataDto metadata
)
{ }
