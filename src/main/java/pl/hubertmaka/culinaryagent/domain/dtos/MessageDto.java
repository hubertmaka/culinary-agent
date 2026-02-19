package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Role;

/**
 * DTO representing a message in the conversation with the AI agent.
 *
 * @param role    The role of the message sender (e.g., USER, ASSISTANT).
 * @param content The content of the message.
 */
public record MessageDto(
    Role role,
    String content
) { }
