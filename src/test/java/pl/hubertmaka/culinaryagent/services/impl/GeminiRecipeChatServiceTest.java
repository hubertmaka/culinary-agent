package pl.hubertmaka.culinaryagent.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import pl.hubertmaka.culinaryagent.domain.dtos.ChatAgentResponseDto;
import pl.hubertmaka.culinaryagent.domain.dtos.MessageDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeChatRequestDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.Role;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;
import pl.hubertmaka.culinaryagent.exceptions.RecipeChatException;
import pl.hubertmaka.culinaryagent.mappers.Mapper;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiRecipeChatServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Mock
    private Mapper<Message, MessageDto> mapper;

    private GeminiRecipeChatService geminiRecipeChatService;

    @BeforeEach
    void setUp() {
        geminiRecipeChatService = new GeminiRecipeChatService(chatClient, "Test instruction: {language} {schema}", mapper);
    }

    @Test
    @DisplayName("Test if GeminiRecipeChatService is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Then
        assertNotNull(geminiRecipeChatService);
    }

    @Test
    @DisplayName("Test if chat returns a valid ChatAgentResponseDto with content and metadata")
    void whenChat_thenReturnChatAgentResponseDto() {
        // Given
        var messageDto = new MessageDto(Role.USER, "What can I cook with tomatoes?");
        var schema = new RecipeSchemaDto("Tomato soup", List.of(), 30, List.of());
        var request = new RecipeChatRequestDto(schema, List.of(messageDto), Voice.VOICE_WOMAN, Language.ENGLISH);

        var usage = mock(Usage.class);
        when(usage.getPromptTokens()).thenReturn(10);
        when(usage.getCompletionTokens()).thenReturn(20);
        when(usage.getTotalTokens()).thenReturn(30);

        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);
        when(metadata.getModel()).thenReturn("gemini-pro");

        var generation = mock(Generation.class);
        var assistantMessage = mock(AssistantMessage.class);
        when(assistantMessage.getText()).thenReturn("Here is a tomato soup recipe!");
        when(generation.getOutput()).thenReturn(assistantMessage);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);

        when(mapper.mapFrom(messageDto)).thenReturn(new UserMessage("What can I cook with tomatoes?"));

        when(chatClient.prompt()
                .user(any(Consumer.class))
                .messages(anyList())
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        ChatAgentResponseDto result = geminiRecipeChatService.chat(request);

        // Then
        assertNotNull(result);
        assertEquals("Here is a tomato soup recipe!", result.content());
        assertNotNull(result.metadata());
        assertEquals(10, result.metadata().inputTokens());
        assertEquals(20, result.metadata().outputTokens());
        assertEquals(30, result.metadata().totalTokens());
        assertEquals("gemini-pro", result.metadata().model());
    }

    @Test
    @DisplayName("Test if chat processes conversation history and maps messages")
    void whenChat_thenMapConversationHistory() {
        // Given
        var userMessageDto = new MessageDto(Role.USER, "How long should I cook the pasta?");
        var assistantMessageDto = new MessageDto(Role.ASSISTANT, "Cook for 10 minutes.");
        var schema = new RecipeSchemaDto("Pasta", List.of(), 20, List.of());
        var request = new RecipeChatRequestDto(schema, List.of(userMessageDto, assistantMessageDto), Voice.VOICE_WOMAN, Language.ENGLISH);

        var usage = mock(Usage.class);
        when(usage.getPromptTokens()).thenReturn(5);
        when(usage.getCompletionTokens()).thenReturn(10);
        when(usage.getTotalTokens()).thenReturn(15);

        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);
        when(metadata.getModel()).thenReturn("gemini-pro");

        var generation = mock(Generation.class);
        var assistantMessage = mock(AssistantMessage.class);
        when(assistantMessage.getText()).thenReturn("Cook pasta for 10 minutes.");
        when(generation.getOutput()).thenReturn(assistantMessage);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);

        when(mapper.mapFrom(any(MessageDto.class))).thenReturn(new UserMessage("mapped message"));

        when(chatClient.prompt()
                .user(any(Consumer.class))
                .messages(anyList())
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        geminiRecipeChatService.chat(request);

        // Then
        verify(mapper, times(2)).mapFrom(any(MessageDto.class));
    }

    @Test
    @DisplayName("Test if chat processes empty conversation history without error")
    void whenChatWithEmptyHistory_thenReturnResponse() {
        // Given
        var schema = new RecipeSchemaDto("Salad", List.of(), 10, List.of());
        var request = new RecipeChatRequestDto(schema, List.of(), Voice.VOICE_WOMAN, Language.POLISH);

        var usage = mock(Usage.class);
        when(usage.getPromptTokens()).thenReturn(3);
        when(usage.getCompletionTokens()).thenReturn(7);
        when(usage.getTotalTokens()).thenReturn(10);

        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);
        when(metadata.getModel()).thenReturn("gemini-pro");

        var generation = mock(Generation.class);
        var assistantMessage = mock(AssistantMessage.class);
        when(assistantMessage.getText()).thenReturn("A simple salad recipe.");
        when(generation.getOutput()).thenReturn(assistantMessage);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);

        when(chatClient.prompt()
                .user(any(Consumer.class))
                .messages(anyList())
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        ChatAgentResponseDto result = geminiRecipeChatService.chat(request);

        // Then
        assertNotNull(result);
        assertEquals("A simple salad recipe.", result.content());
        verify(mapper, never()).mapFrom(any(MessageDto.class));
    }

    @Test
    @DisplayName("Test if chat throws RecipeChatException when response is null")
    void whenChatResponseIsNull_thenThrowRecipeChatException() {
        // Given
        var schema = new RecipeSchemaDto("Soup", List.of(), 15, List.of());
        var request = new RecipeChatRequestDto(schema, List.of(), Voice.VOICE_WOMAN, Language.ENGLISH);

        when(chatClient.prompt()
                .user(any(Consumer.class))
                .messages(anyList())
                .call()
                .chatResponse()
        ).thenReturn(null);

        // When & Then
        assertThrows(RecipeChatException.class, () -> geminiRecipeChatService.chat(request));
    }
}


