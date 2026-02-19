package pl.hubertmaka.culinaryagent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataRequestDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaResponseDto;
import pl.hubertmaka.culinaryagent.services.RecipeExtractorService;
import reactor.core.publisher.Flux;

/**
 * REST controller for processing recipes. It provides endpoints for extracting recipe information from various
 * data formats and streaming chat responses related to recipes.
 */
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeProcessorController {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(RecipeProcessorController.class);
    /** The service responsible for extracting recipe information from the provided data. */
    private final RecipeExtractorService recipeExtractorService;

    /**
     * Constructor for RecipeProcessorController that initializes the RecipeExtractorService.
     *
     * @param recipeExtractorService the service to be used for extracting recipe information, injected by Spring
     */
    public RecipeProcessorController(RecipeExtractorService recipeExtractorService) {
        this.recipeExtractorService = recipeExtractorService;
    }

    /**
     * Endpoint for extracting recipe information from the provided recipe data. It accepts a RecipeDataDto
     * and returns a structured RecipeSchemaDto containing the extracted recipe information.
     *
     * @param recipeDataRequestDto the data transfer object containing the recipe data to be extracted
     * @return a ResponseEntity containing the extracted RecipeSchemaDto and an HTTP status of OK
     */
    @PostMapping(value = "/extract", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeSchemaResponseDto> extractRecipe(RecipeDataRequestDto recipeDataRequestDto) {
        log.info("Received recipe data for extraction: {}", recipeDataRequestDto);
        RecipeSchemaResponseDto recipeSchema = recipeExtractorService.extract(recipeDataRequestDto);
        return new ResponseEntity<>(recipeSchema, HttpStatus.OK);
    }

    /**
     * Endpoint for streaming chat responses related to recipe preparation. It accepts a RecipeSchemaDto and returns a
     * Flux of ServerSentEvent containing RecipeSchemaDto objects, allowing for real-time updates in the client.
     *
     * @param recipeSchemaResponseDto the data transfer object containing the recipe schema for which to stream chat responses
     * @return a Flux of ServerSentEvent containing RecipeSchemaDto objects
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<RecipeSchemaResponseDto>> streamChatResponse(RecipeSchemaResponseDto recipeSchemaResponseDto) {
        return Flux.empty();
    }
}
