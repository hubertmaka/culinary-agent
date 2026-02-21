package pl.hubertmaka.culinaryagent.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.hubertmaka.culinaryagent.domain.dtos.ChatAgentResponseDto;
import pl.hubertmaka.culinaryagent.domain.dtos.MessageDto;
import pl.hubertmaka.culinaryagent.domain.dtos.MetadataDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatRequestDto;
import pl.hubertmaka.culinaryagent.exceptions.RecipeChatException;
import pl.hubertmaka.culinaryagent.mappers.Mapper;
import pl.hubertmaka.culinaryagent.services.RecipeChatService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation for handling recipe chat interactions using a Gemini-based chat client.
 * This service processes incoming chat requests, interacts with the ChatClient to generate responses,
 * and extracts relevant metadata from the chat interactions.
 */
@Service
public class GeminiRecipeChatService implements RecipeChatService {
    /** Logger for logging information and debugging purposes. */
    private static final Logger log = LoggerFactory.getLogger(GeminiRecipeChatService.class);
    /** The ChatClient used to interact with the Gemini model for recipe chat interactions. */
    private final ChatClient chatClient;
    /** The Mapper used to convert between Message and MessageDto objects. */
    private final Mapper<Message, MessageDto> mapper;
    /** The instruction prompt for the chat agent, loaded from the classpath resource. */
    private final String instruction;

    /**
     * Constructor for GeminiRecipeChatService that initializes the ChatClient and instruction prompt.
     *
     * @param chatClient the ChatClient to be used for recipe chat interactions, injected by Spring with the qualifier "recipeChatAgent"
     * @param instruction the instruction prompt for the chat agent, injected by Spring with the qualifier "agentUserInstruction"
     * @param mapper the Mapper used to convert between Message and MessageDto objects
     */
    public GeminiRecipeChatService(
            @Qualifier("recipeChatAgent") ChatClient chatClient,
            @Qualifier("agentUserInstruction") String instruction,
            Mapper<Message, MessageDto> mapper
    ) {
        log.info("Creating Gemini recipe chat service...");
        this.chatClient = chatClient;
        this.instruction = instruction;
        this.mapper = mapper;
    }

    /**
     * Handles a chat request related to recipes and returns a response containing the generated text and metadata.
     *
     * @param recipeChatRequestDto the data transfer object containing the chat request information, including language, schema, and conversation history
     * @return a ChatAgentResponseDto containing the generated response text and associated metadata
     */
    @Override
    public ChatAgentResponseDto chat(RecipeChatRequestDto recipeChatRequestDto) {
        log.info("Gemini recipe chat request received");
        ChatResponse response = callAgent(recipeChatRequestDto);
        MetadataDto metadataDto = extractMetadata(response);
        return new ChatAgentResponseDto(response.getResult().getOutput().getText(), metadataDto);
    }

    /**
     * Calls the Gemini chat agent with the provided chat request information and returns the chat response.
     *
     * @param request the data transfer object containing the chat request information, including language, schema, and conversation history
     * @return a ChatResponse containing the generated response from the Gemini chat agent
     */
    private ChatResponse callAgent(RecipeChatRequestDto request) {
        log.info("Calling Gemini recipe chat service...");
        return chatClient.prompt()
            .user(u -> u
                .text(instruction)
                .params(Map.of("language", request.language(), "schema", request.schema()))
            )
            .messages(preprocessConversationHistory(request.messages()))
            .call()
            .chatResponse();
    }

    /**
     * Extracts metadata from the given chat response and returns it as a MetadataDto.
     *
     * @param response the ChatResponse from which to extract metadata
     * @return a MetadataDto containing the extracted metadata information, including token usage and model details
     * @throws RecipeChatException if the metadata cannot be retrieved from the chat response
     */
    private MetadataDto extractMetadata(ChatResponse response) {
        log.info("Extract metadata from chat response");
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
            })
            .orElseThrow(() -> new RecipeChatException("Failed to retrieve metadata from chat response"));
    }

    /**
     * Preprocesses the conversation history by mapping each MessageDto to a Message object using the provided mapper.
     *
     * @param conversationHistory the list of MessageDto objects representing the conversation history
     * @return a list of Message objects that have been mapped from the input MessageDto objects
     */
    private List<Message> preprocessConversationHistory(List<MessageDto> conversationHistory) {
        return conversationHistory.stream()
            .map(mapper::mapFrom)
            .toList();
    }
}
