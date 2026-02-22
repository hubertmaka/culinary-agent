package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object representing the response from a chat agent.
 *
 * @param content  The content of the response message.
 * @param metadata Additional metadata associated with the response.
 */
public record ChatAgentResponseDto(
    @NotBlank(message = "Content must not be blank")
    @Size(max = 10000, message = "Content must not exceed 10,000 characters")
    String content,
    @NotNull
    MetadataDto metadata
)
{ }
