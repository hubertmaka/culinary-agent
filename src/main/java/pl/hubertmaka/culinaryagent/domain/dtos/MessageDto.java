package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.hubertmaka.culinaryagent.domain.enums.Role;

/**
 * DTO representing a message in the conversation with the AI agent.
 *
 * @param role    The role of the message sender (e.g., USER, ASSISTANT).
 * @param content The content of the message.
 */
public record MessageDto(
    @NotNull
    Role role,
    @NotNull
    @Size(max = 10000, message = "Content must not exceed 10,000 characters")
    String content
) { }
