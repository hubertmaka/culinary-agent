package pl.hubertmaka.culinaryagent.strategies.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.reactive.function.client.WebClient;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlRecipeInputStrategyTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;
    @InjectMocks
    private UrlRecipeInputStrategy urlRecipeInputStrategy;

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy is initialized correctly")
    void whenInitialized_thenNotNull() {
        assertNotNull(urlRecipeInputStrategy);
    }

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy supports URL source")
    void whenSupports_thenReturnTrueForURL() {
        // Given
        var recipeSource = RecipeSource.URL;

        // When
        var result = urlRecipeInputStrategy.supports(recipeSource);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy does not support non-URL sources - IMAGE")
    void whenSupports_thenReturnFalseForImage() {
        // Given
        var imageSource = RecipeSource.IMAGE;

        // When
        var result = urlRecipeInputStrategy.supports(imageSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy does not support non-URL sources - TEXT")
    void whenSupports_thenReturnFalseForText() {
        // Given
        var textSource = RecipeSource.TEXT;

        // When
        var result = urlRecipeInputStrategy.supports(textSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy creates a UserMessage with extracted content")
    void whenCreateMessage_thenReturnUserMessageWithExtractedContent() {
        // Given
        var url = "https://example.com/recipe";
        var recipeData = new RecipeDataDto(url, RecipeSource.URL, null, Language.ENGLISH);
        var expectedContent = "Delicious Pasta Ingredients: pasta, tomato sauce, cheese";
        var htmlContent = "<html><head><title>Test Recipe</title></head><body><h1>Delicious Pasta</h1><p>Ingredients: pasta, tomato sauce, cheese</p></body></html>";
        when(webClient.get().uri(anyString()).retrieve().bodyToMono(String.class)).thenReturn(Mono.just(htmlContent));

        // When
        var userMessage = urlRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals(expectedContent, userMessage.getText());
    }

    @Test
    @DisplayName("Test if UrlRecipeInputStrategy handles empty HTML content gracefully")
    void whenCreateMessage_thenReturnUserMessageWithEmptyContent() {
        // Given
        var url = "https://example.com/empty-recipe";
        var recipeData = new RecipeDataDto(url, RecipeSource.URL, null, Language.ENGLISH);
        var expectedContent = "";
        var htmlContent = "<html><head><title>Empty Recipe</title></head><body></body></html>";
        when(webClient.get().uri(url).retrieve().bodyToMono(String.class)).thenReturn(Mono.just(htmlContent));

        // When
        var userMessage = urlRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals(expectedContent, userMessage.getText());
    }
}