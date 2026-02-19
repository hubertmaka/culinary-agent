package pl.hubertmaka.culinaryagent.strategies;

import org.springframework.ai.chat.messages.UserMessage;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataRequestDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

/**
 * Strategy interface for handling different types of recipe inputs.
 * Implementations of this interface will determine if they support a given recipe source
 * and will create a UserMessage based on the provided RecipeDataDto.
 */
public interface RecipeInputStrategy {
    /**
     * Determines if the strategy supports the given recipe source.
     *
     * @param source the recipe source to check
     * @return true if the strategy supports the source, false otherwise
     */
    boolean supports(RecipeSource source);

    /**
     * Creates a UserMessage based on the provided RecipeDataDto.
     *
     * @param recipeData the data of the recipe to create a message from
     * @return a UserMessage representing the recipe data
     */
    UserMessage createMessage(RecipeDataRequestDto recipeData);
}
