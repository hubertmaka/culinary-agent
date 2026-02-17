package pl.hubertmaka.culinaryagent.strategies.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

import static org.junit.jupiter.api.Assertions.*;

class TextRecipeInputStrategyTest {
    private TextRecipeInputStrategy textRecipeInputStrategy;

    @BeforeEach
    public void setUp() {
        textRecipeInputStrategy = new TextRecipeInputStrategy();
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy is initialized correctly")
    public void whenInitialized_thenNotNull() {
        assertNotNull(textRecipeInputStrategy);
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy supports TEXT source")
    public void whenSupports_thenReturnTrueForText() {
        // Given
        var recipeSource = RecipeSource.TEXT;

        // When
        var result = textRecipeInputStrategy.supports(recipeSource);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy does not support non-TEXT sources - URL")
    public void whenSupports_thenReturnFalseForURL() {
        // Given
        var urlSource = RecipeSource.URL;

        // When
        var result = textRecipeInputStrategy.supports(urlSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy does not support non-TEXT sources - IMAGE")
    public void whenSupports_thenReturnFalseForImage() {
        // Given
        var imageSource = RecipeSource.IMAGE;

        // When
        var result = textRecipeInputStrategy.supports(imageSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy creates UserMessage with correct content")
    public void whenCreateMessage_thenReturnUserMessageWithContent() {
        // Given
        var recipeData = new RecipeDataDto(
                "Test recipe content",
                RecipeSource.TEXT,
                null,
                Language.ENGLISH
        );

        // When
        var userMessage = textRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals("Test recipe content", userMessage.getText());
        assertTrue(userMessage.getMedia().isEmpty());
    }

    @Test
    @DisplayName("Test if TextRecipeInputStrategy creates UserMessage ignores not null fileExtension")
    public void whenCreateMessage_thenReturnUserMessageWithEmptyContent() {
        // Given
        var recipeData = new RecipeDataDto(
                "Test recipe content",
                RecipeSource.TEXT,
                FileExtension.PNG,
                Language.ENGLISH
        );

        // When
        var userMessage = textRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals("Test recipe content", userMessage.getText());
        assertTrue(userMessage.getMedia().isEmpty());
    }
}