package pl.hubertmaka.culinaryagent.services;

import pl.hubertmaka.culinaryagent.domain.dtos.ChatAgentResponseDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatRequestDto;

/**
 * Service interface for handling recipe chat interactions.
 */
public interface RecipeChatService {
    /**
     * Processes a recipe chat request and generates a response from the chat agent.
     *
     * @param recipeChatRequestDto the request data for the recipe chat interaction
     * @return a ChatAgentResponseDto containing the response from the chat agent
     */
    public ChatAgentResponseDto chat(RecipeChatRequestDto recipeChatRequestDto);
}
