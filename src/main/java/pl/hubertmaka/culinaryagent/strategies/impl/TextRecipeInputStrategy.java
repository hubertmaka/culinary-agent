package pl.hubertmaka.culinaryagent.strategies.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.strategies.RecipeInputStrategy;

/**
 * Strategy implementation for handling text-based recipe inputs.
 * This strategy supports the RecipeSource.TEXT type and creates a UserMessage
 * containing the recipe content as is.
 */
@Component
public class TextRecipeInputStrategy implements RecipeInputStrategy {
    /** Logger for logging information and debugging purposes. */
    private final static Logger log = LoggerFactory.getLogger(TextRecipeInputStrategy.class);

    /**
     * Implements the supports method to check if the given recipe source is of type TEXT.
     *
     * @param source the recipe source to check
     * @return true if the source is RecipeSource.TEXT, false otherwise
     */
    @Override
    public boolean supports(RecipeSource source) {
        return source == RecipeSource.TEXT;
    }

    /**
     * Implements the createMessage method to create a UserMessage containing the recipe content.
     *
     * @param recipeData the data of the recipe to create a message from
     * @return a UserMessage representing the recipe data content
     */
    @Override
    public UserMessage createMessage(RecipeDataDto recipeData) {
        return UserMessage.builder()
                .text(recipeData.content())
                .build();
    }
}
