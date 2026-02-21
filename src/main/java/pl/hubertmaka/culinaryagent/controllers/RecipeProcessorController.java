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
import pl.hubertmaka.culinaryagent.domain.dtos.*;
import pl.hubertmaka.culinaryagent.domain.enums.StreamEvent;
import pl.hubertmaka.culinaryagent.services.RecipeChatService;
import pl.hubertmaka.culinaryagent.services.RecipeExtractorService;
import pl.hubertmaka.culinaryagent.services.TextToSpeechService;
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
    /** The service responsible for handling chat interactions related to recipes. */
    private final RecipeChatService recipeChatService;
    /** The service responsible for converting text to speech for recipe-related interactions. */
    private final TextToSpeechService textToSpeechService;

    /**
     * Constructor for RecipeProcessorController that initializes the RecipeExtractorService.
     *
     * @param recipeExtractorService the service to be used for extracting recipe information, injected by Spring
     * @param recipeChatService the service to be used for handling chat interactions related to recipes, injected by Spring
     * @param textToSpeechService the service to be used for converting text to speech for recipe-related interactions, injected by Spring
     */
    public RecipeProcessorController(
            RecipeExtractorService recipeExtractorService,
            RecipeChatService recipeChatService,
            TextToSpeechService textToSpeechService
    ) {
        this.recipeExtractorService = recipeExtractorService;
        this.recipeChatService = recipeChatService;
        this.textToSpeechService = textToSpeechService;
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
     * @param recipeChatRequestDto the data transfer object containing the chat request information, including language, schema, and conversation history
     * @return a Flux of ServerSentEvent containing RecipeSchemaDto objects
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<RecipeChatResponseChunkDto>> streamChatResponse(RecipeChatRequestDto recipeChatRequestDto) {
        log.info("Received recipe chat response for streaming: {}", recipeChatRequestDto);
        ChatAgentResponseDto agentResponse = recipeChatService.chat(recipeChatRequestDto);
        return textToSpeechService.stream(agentResponse.content(), recipeChatRequestDto.voice())
                .map(this::buildAudioChunk)
                .concatWith(Flux.just(buildAgentMetaChunk(agentResponse)))
                .concatWith(Flux.just(buildTTSMetaChunk(agentResponse)));
    }

    /**
     * Builds a ServerSentEvent containing an audio chunk from the provided RecipeChatResponseChunkDto.
     *
     * @param chunk the RecipeChatResponseChunkDto containing the audio data to be included in the ServerSentEvent
     * @return a ServerSentEvent containing the audio chunk data
     */
    private ServerSentEvent<RecipeChatResponseChunkDto> buildAudioChunk(RecipeChatResponseChunkDto chunk) {
        log.info("Received recipe chat response for audio: {}", chunk);
        return ServerSentEvent.<RecipeChatResponseChunkDto>builder()
            .event(StreamEvent.AUDIO.getEvent())
            .data(chunk)
            .build();
    }

    /**
     * Builds a ServerSentEvent containing metadata about the agent's response from the provided ChatAgentResponseDto.
     *
     * @param response the ChatAgentResponseDto containing the agent's response and associated metadata
     * @return a ServerSentEvent containing the agent's response metadata
     */
    private ServerSentEvent<RecipeChatResponseChunkDto> buildAgentMetaChunk(ChatAgentResponseDto response) {
        log.info("Received recipe chat response for agent: {}", response);
        return ServerSentEvent.<RecipeChatResponseChunkDto>builder()
            .event(StreamEvent.AGENT_COMPLETION.getEvent())
            .data(new RecipeChatResponseChunkDto(null, response))
            .build();
    }

    /**
     * Builds a ServerSentEvent containing metadata about the text-to-speech conversion from the provided ChatAgentResponseDto.
     *
     * @param response the ChatAgentResponseDto containing the agent's response and associated metadata
     * @return a ServerSentEvent containing the text-to-speech conversion metadata
     */
    private ServerSentEvent<RecipeChatResponseChunkDto> buildTTSMetaChunk(ChatAgentResponseDto response) {
        log.info("Received recipe chat response for TTS: {}", response);
        int inputTokens = response.content().length();
        int outputTokens = 0;
        int totalTokens = inputTokens + outputTokens;
        return ServerSentEvent.<RecipeChatResponseChunkDto>builder()
            .event(StreamEvent.AGENT_COMPLETION.getEvent())
            .data(new RecipeChatResponseChunkDto(
                null,
                new ChatAgentResponseDto(
                    response.content(),
                    new MetadataDto(inputTokens, outputTokens, totalTokens, textToSpeechService.getModel())
                )
            ))
            .build();
    }
}
