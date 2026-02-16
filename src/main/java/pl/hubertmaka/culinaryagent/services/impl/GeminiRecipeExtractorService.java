package pl.hubertmaka.culinaryagent.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedSchemaException;
import pl.hubertmaka.culinaryagent.services.RecipeExtractorService;
import pl.hubertmaka.culinaryagent.strategies.RecipeInputStrategy;

import java.util.List;

/**
 * Service implementation for extracting recipe information using a Gemini-based chat client.
 * This service interacts with the ChatClient to prompt for recipe extraction based on the provided input data.
 */
@Service
public class GeminiRecipeExtractorService implements RecipeExtractorService {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(GeminiRecipeExtractorService.class);
    @Value("classpath:prompts/recipe-extractor-agent-personality.md")
    private String extractorPersonalityPrompt;
    /** The ChatClient used to interact with the Gemini model for recipe extraction. */
    private final ChatClient chatClient;
    /** A list of strategies for handling different types of recipe inputs. */
    private final List<RecipeInputStrategy> strategies;

    /**
     * Constructor for GeminiRecipeExtractorService that initializes the ChatClient.
     *
     * @param chatClient the ChatClient to be used for recipe extraction, injected by Spring with the qualifier "recipeExtractorAgent"
     */
    public GeminiRecipeExtractorService(
            @Qualifier("recipeExtractorAgent") ChatClient chatClient,
            List<RecipeInputStrategy> strategies
    ) {
        log.info("Creating Gemini recipe extractor service...");
        this.chatClient = chatClient;
        this.strategies = strategies;
    }

    /**
     * Extracts recipe information from the provided recipe data and returns a structured recipe schema.
     *
     * @param recipeSchema The data transfer object containing the recipe data to be extracted.
     * @return A RecipeSchemaDto containing the structured recipe information extracted from the input data.
     * @throws UnsupportedSchemaException if the provided recipe source is not supported by any of the strategies
     */
    @Override
    public RecipeSchemaDto extract(RecipeDataDto recipeSchema) {
        log.info("Extracting recipe using GeminiRecipeExtractorService...");
        RecipeInputStrategy strategy = strategies.stream()
                .filter(s -> s.supports(recipeSchema.contentType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedSchemaException("Unsupported recipe source: " + recipeSchema.contentType()));

        return chatClient.prompt()
                .messages(strategy.createMessage(recipeSchema))
                .call()
                .entity(RecipeSchemaDto.class);
    }
}
