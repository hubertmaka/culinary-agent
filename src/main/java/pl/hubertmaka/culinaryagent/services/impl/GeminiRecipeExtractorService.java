package pl.hubertmaka.culinaryagent.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.hubertmaka.culinaryagent.domain.dtos.MetadataDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataRequestDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaResponseDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.exceptions.RecipeExtractionException;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedSchemaException;
import pl.hubertmaka.culinaryagent.services.RecipeExtractorService;
import pl.hubertmaka.culinaryagent.strategies.RecipeInputStrategy;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for extracting recipe information using a Gemini-based chat client.
 * This service interacts with the ChatClient to prompt for recipe extraction based on the provided input data.
 */
@Service
public class GeminiRecipeExtractorService implements RecipeExtractorService {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(GeminiRecipeExtractorService.class);
    /** The ChatClient used to interact with the Gemini model for recipe extraction. */
    private final ChatClient chatClient;
    /** The BeanOutputConverter used to convert the chat response into a RecipeSchemaDto format. */
    private final BeanOutputConverter<RecipeSchemaDto> converter;
    /** A list of strategies for handling different types of recipe inputs. */
    private final List<RecipeInputStrategy> strategies;

    /**
     * Constructor for GeminiRecipeExtractorService that initializes the ChatClient.
     *
     * @param chatClient the ChatClient to be used for recipe extraction, injected by Spring with the qualifier "recipeExtractorAgent"
     */
    public GeminiRecipeExtractorService(
            @Qualifier("recipeExtractorAgent") ChatClient chatClient,
            BeanOutputConverter<RecipeSchemaDto> converter,
            List<RecipeInputStrategy> strategies
    ) {
        log.info("Creating Gemini recipe extractor service...");
        this.chatClient = chatClient;
        this.converter = converter;
        this.strategies = strategies;
    }

    /**
     * Extracts recipe information from the provided recipe data and returns a structured recipe schema.
     *
     * @param recipeSchema The data transfer object containing the recipe data to be extracted.
     * @return A RecipeSchemaDto containing the structured recipe information extracted from the input data.
     */
    @Override
    public RecipeSchemaResponseDto extract(RecipeDataRequestDto recipeSchema) {
        log.info("Extracting recipe using GeminiRecipeExtractorService...");
        RecipeInputStrategy strategy = getAvailableStrategy(recipeSchema);
        ChatResponse response = callAgent(strategy, recipeSchema);
        MetadataDto metadata = extractMetadata(response);
        return extractSchema(response, metadata);
    }

    /**
     * Retrieves a list of supported recipe sources that this extractor can handle.
     *
     * @return A list of RecipeSource enums representing the supported recipe sources.
     */
    @Override
    public List<RecipeSource> getSupportedSources() {
        return List.of(RecipeSource.IMAGE, RecipeSource.TEXT, RecipeSource.URL);
    }

    /**
     * Retrieves the appropriate RecipeInputStrategy based on the content type of the provided recipe data.
     *
     * @param recipe the recipe data for which to find a supporting strategy
     * @return the RecipeInputStrategy that supports the content type of the provided recipe data
     * @throws UnsupportedSchemaException if no strategy supports the content type of the provided recipe data
     */
    private RecipeInputStrategy getAvailableStrategy(RecipeDataRequestDto recipe) {
        log.info("Getting available strategy...");
        return strategies.stream()
                .filter(s -> s.supports(recipe.contentType()))
                .filter(s -> getSupportedSources().contains(recipe.contentType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedSchemaException("Unsupported source: " + recipe.contentType()));
    }

    /**
     * Calls the recipe extractor agent with the appropriate strategy and recipe data to obtain a chat response.
     *
     * @param strategy the RecipeInputStrategy to use for creating the message for the agent
     * @param recipeSchema the recipe data to be extracted, which will be used to create the message for the agent
     * @return the ChatResponse obtained from calling the recipe extractor agent
     */
    private ChatResponse callAgent(RecipeInputStrategy strategy, RecipeDataRequestDto recipeSchema) {
        log.info("Calling recipe extractor agent...");
        return chatClient.prompt()
                .messages(strategy.createMessage(recipeSchema))
                .call()
                .chatResponse();
    }

    /**
     * Extracts metadata from the chat response, including token usage and model information.
     *
     * @param response the ChatResponse from which to extract metadata
     * @return a MetadataDto containing the extracted metadata information
     * @throws RecipeExtractionException if the metadata is missing from the response
     */
    private MetadataDto extractMetadata(ChatResponse response) {
        log.info("Extracting recipe metadata...");
        return Optional.ofNullable(response)
                .map(ChatResponse::getMetadata)
                .map(meta -> {
                            Usage usage = meta.getUsage();
                            return new MetadataDto(
                                    usage.getPromptTokens(),
                                    usage.getCompletionTokens(),
                                    usage.getTotalTokens(),
                                    meta.getModel()
                            );
                        }
                )
                .orElseThrow(() -> new RecipeExtractionException("Metadata is missing from the response"));
    }

    /**
     * Extracts the recipe schema from the chat response and combines it with the provided metadata to create a RecipeSchemaResponseDto.
     *
     * @param response the ChatResponse from which to extract the recipe schema
     * @param metadata the MetadataDto containing metadata information to be included in the response
     * @return a RecipeSchemaResponseDto containing the extracted recipe schema and associated metadata
     * @throws RecipeExtractionException if the recipe schema cannot be extracted from the response
     */
    private RecipeSchemaResponseDto extractSchema(ChatResponse response, MetadataDto metadata) {
        log.info("Extracting recipe schema from recipe extractor agent response...");
        return Optional.of(response)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AbstractMessage::getText)
                .map(converter::convert)
                .map(recipeSchemaDto -> new RecipeSchemaResponseDto(recipeSchemaDto, metadata))
                .orElseThrow(() -> new RecipeExtractionException("Failed to extract recipe schema from response"));
    }
}
