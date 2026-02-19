package pl.hubertmaka.culinaryagent.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import pl.hubertmaka.culinaryagent.strategies.impl.ImageRecipeInputStrategy;
import pl.hubertmaka.culinaryagent.strategies.impl.TextRecipeInputStrategy;
import pl.hubertmaka.culinaryagent.strategies.impl.UrlRecipeInputStrategy;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class GeminiRecipeExtractorServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    @Mock
    private BeanOutputConverter<RecipeSchemaDto> converter;

    @Mock
    private ImageRecipeInputStrategy imageRecipeInputStrategy;

    @Mock
    private TextRecipeInputStrategy textRecipeInputStrategy;

    @Mock
    private UrlRecipeInputStrategy urlRecipeInputStrategy;

    @InjectMocks
    private GeminiRecipeExtractorService geminiRecipeExtractorService;

    @Test
    @DisplayName("Test if GeminiRecipeExtractorService is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Given

        // Then
        assertNotNull(geminiRecipeExtractorService);
    }

    @Test
    @DisplayName("Test if supportedStrategies returns all created sources")
    void whenGetSupportedSources_thenReturnAllSources() {
        // Given

        // When
        var supportedStrategies = geminiRecipeExtractorService.getSupportedSources();

        // Then
        assertNotNull(supportedStrategies);
        assertTrue(supportedStrategies.contains(RecipeSource.IMAGE));
        assertTrue(supportedStrategies.contains(RecipeSource.TEXT));
        assertTrue(supportedStrategies.contains(RecipeSource.URL));
        assertEquals(3, supportedStrategies.size());
    }
}