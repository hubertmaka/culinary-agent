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
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import pl.hubertmaka.culinaryagent.mappers.impl.RecipeSchemaMapper;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataRequestDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaResponseDto;
import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.exceptions.RecipeExtractionException;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedSchemaException;
import pl.hubertmaka.culinaryagent.strategies.impl.ImageRecipeInputStrategy;
import pl.hubertmaka.culinaryagent.strategies.impl.TextRecipeInputStrategy;
import pl.hubertmaka.culinaryagent.strategies.impl.UrlRecipeInputStrategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GeminiRecipeExtractorServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Mock
    private RecipeSchemaMapper mapper;

    @Mock
    private ImageRecipeInputStrategy imageRecipeInputStrategy;

    @Mock
    private TextRecipeInputStrategy textRecipeInputStrategy;

    @Mock
    private UrlRecipeInputStrategy urlRecipeInputStrategy;

    private GeminiRecipeExtractorService geminiRecipeExtractorService;

    @BeforeEach
    void setUp() {
        geminiRecipeExtractorService = new GeminiRecipeExtractorService(
                chatClient,
                mapper,
                List.of(imageRecipeInputStrategy, textRecipeInputStrategy, urlRecipeInputStrategy)
        );
    }

    @Test
    @DisplayName("Test if GeminiRecipeExtractorService is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Then
        assertNotNull(geminiRecipeExtractorService);
    }

    @Test
    @DisplayName("Test if getSupportedSources returns all created sources")
    void whenGetSupportedSources_thenReturnAllSources() {
        // When
        var supportedStrategies = geminiRecipeExtractorService.getSupportedSources();

        // Then
        assertNotNull(supportedStrategies);
        assertTrue(supportedStrategies.contains(RecipeSource.IMAGE));
        assertTrue(supportedStrategies.contains(RecipeSource.TEXT));
        assertTrue(supportedStrategies.contains(RecipeSource.URL));
        assertEquals(3, supportedStrategies.size());
    }

    @Test
    @DisplayName("Test if extract returns a valid RecipeSchemaResponseDto with content and metadata")
    void whenExtract_thenReturnRecipeSchemaResponseDto() {
        // Given
        var request = new RecipeDataRequestDto("Boil pasta for 10 minutes.", RecipeSource.TEXT, FileExtension.JPEG, Language.EN_US);
        var expectedSchema = new RecipeSchemaDto("Pasta", List.of(), 20, List.of());
        var userMessage = new UserMessage("Boil pasta for 10 minutes.");

        when(textRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(true);
        when(textRecipeInputStrategy.createMessage(request)).thenReturn(userMessage);

        var usage = mock(Usage.class);
        when(usage.getPromptTokens()).thenReturn(10);
        when(usage.getCompletionTokens()).thenReturn(20);
        when(usage.getTotalTokens()).thenReturn(30);

        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);
        when(metadata.getModel()).thenReturn("gemini-pro");

        var generation = mock(Generation.class);
        var message = mock(AssistantMessage.class);
        when(message.getText()).thenReturn("{\"content\":\"Pasta\"}");
        when(generation.getOutput()).thenReturn(message);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);

        when(mapper.mapFrom("{\"content\":\"Pasta\"}")).thenReturn(expectedSchema);

        when(chatClient.prompt()
                .messages(any(UserMessage.class))
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        RecipeSchemaResponseDto result = geminiRecipeExtractorService.extract(request);

        // Then
        assertNotNull(result);
        assertNotNull(result.recipeSchema());
        assertEquals("Pasta", result.recipeSchema().content());
        assertNotNull(result.metadata());
        assertEquals(10, result.metadata().inputTokens());
        assertEquals(20, result.metadata().outputTokens());
        assertEquals(30, result.metadata().totalTokens());
        assertEquals("gemini-pro", result.metadata().model());
    }

    @Test
    @DisplayName("Test if extract uses the correct strategy for TEXT source")
    void whenExtractWithTextSource_thenUseTextStrategy() {
        // Given
        var request = new RecipeDataRequestDto("Some recipe text", RecipeSource.TEXT, FileExtension.JPEG, Language.EN_US);
        var userMessage = new UserMessage("Some recipe text");
        var expectedSchema = new RecipeSchemaDto("Recipe", List.of(), 15, List.of());

        when(textRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(true);
        when(textRecipeInputStrategy.createMessage(request)).thenReturn(userMessage);

        var usage = mock(Usage.class);
        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);

        var generation = mock(Generation.class);
        var message = mock(AssistantMessage.class);
        when(message.getText()).thenReturn("{}");
        when(generation.getOutput()).thenReturn(message);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);
        when(mapper.mapFrom("{}")).thenReturn(expectedSchema);

        when(chatClient.prompt()
                .messages(any(UserMessage.class))
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        geminiRecipeExtractorService.extract(request);

        // Then
        verify(textRecipeInputStrategy).createMessage(request);
        verify(imageRecipeInputStrategy, never()).createMessage(any());
        verify(urlRecipeInputStrategy, never()).createMessage(any());
    }

    @Test
    @DisplayName("Test if extract uses the correct strategy for IMAGE source")
    void whenExtractWithImageSource_thenUseImageStrategy() {
        // Given
        var request = new RecipeDataRequestDto("base64imagedata", RecipeSource.IMAGE, FileExtension.JPEG, Language.EN_US);
        var userMessage = new UserMessage("base64imagedata");
        var expectedSchema = new RecipeSchemaDto("Image Recipe", List.of(), 30, List.of());

        when(imageRecipeInputStrategy.supports(RecipeSource.IMAGE)).thenReturn(true);
        when(imageRecipeInputStrategy.createMessage(request)).thenReturn(userMessage);

        var usage = mock(Usage.class);
        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);

        var generation = mock(Generation.class);
        var message = mock(AssistantMessage.class);
        when(message.getText()).thenReturn("{}");
        when(generation.getOutput()).thenReturn(message);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);
        when(mapper.mapFrom("{}")).thenReturn(expectedSchema);

        when(chatClient.prompt()
                .messages(any(UserMessage.class))
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When
        geminiRecipeExtractorService.extract(request);

        // Then
        verify(imageRecipeInputStrategy).createMessage(request);
        verify(textRecipeInputStrategy, never()).createMessage(any());
        verify(urlRecipeInputStrategy, never()).createMessage(any());
    }

    @Test
    @DisplayName("Test if extract throws UnsupportedSchemaException for unsupported source")
    void whenExtractWithUnsupportedSource_thenThrowUnsupportedSchemaException() {
        // Given
        var request = new RecipeDataRequestDto("some data", RecipeSource.TEXT, FileExtension.JPEG, Language.EN_US);

        when(textRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(false);
        when(imageRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(false);
        when(urlRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(false);

        // When & Then
        assertThrows(UnsupportedSchemaException.class, () -> geminiRecipeExtractorService.extract(request));
    }

    @Test
    @DisplayName("Test if extract throws RecipeExtractionException when chat response is null")
    void whenChatResponseIsNull_thenThrowRecipeExtractionException() {
        // Given
        var request = new RecipeDataRequestDto("Some recipe text", RecipeSource.TEXT, FileExtension.JPEG, Language.EN_US);
        var userMessage = new UserMessage("Some recipe text");

        when(textRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(true);
        when(textRecipeInputStrategy.createMessage(request)).thenReturn(userMessage);

        when(chatClient.prompt()
                .messages(any(UserMessage.class))
                .call()
                .chatResponse()
        ).thenReturn(null);

        // When & Then
        assertThrows(RecipeExtractionException.class, () -> geminiRecipeExtractorService.extract(request));
    }

    @Test
    @DisplayName("Test if extract throws RecipeExtractionException when converter returns null")
    void whenConverterReturnsNull_thenThrowRecipeExtractionException() {
        // Given
        var request = new RecipeDataRequestDto("Some recipe text", RecipeSource.TEXT, FileExtension.JPEG, Language.EN_US);
        var userMessage = new UserMessage("Some recipe text");

        when(textRecipeInputStrategy.supports(RecipeSource.TEXT)).thenReturn(true);
        when(textRecipeInputStrategy.createMessage(request)).thenReturn(userMessage);

        var usage = mock(Usage.class);
        var metadata = mock(ChatResponseMetadata.class);
        when(metadata.getUsage()).thenReturn(usage);

        var generation = mock(Generation.class);
        var message = mock(AssistantMessage.class);
        when(message.getText()).thenReturn("{}");
        when(generation.getOutput()).thenReturn(message);

        var chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(generation);
        when(chatResponse.getMetadata()).thenReturn(metadata);
        when(mapper.mapFrom("{}")).thenReturn(null);

        when(chatClient.prompt()
                .messages(any(UserMessage.class))
                .call()
                .chatResponse()
        ).thenReturn(chatResponse);

        // When & Then
        assertThrows(RecipeExtractionException.class, () -> geminiRecipeExtractorService.extract(request));
    }
}