package pl.hubertmaka.culinaryagent.strategies.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class ImageRecipeInputStrategyTest {
    private ImageRecipeInputStrategy imageRecipeInputStrategy;

    @BeforeEach
    void setUp() {
        imageRecipeInputStrategy = new ImageRecipeInputStrategy();
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy is initialized correctly")
    void whenInitialized_thenNotNull() {
        assertNotNull(imageRecipeInputStrategy);
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy supports IMAGE source")
    void whenSupports_thenReturnTrueForImage() {
        // Given
        var recipeSource = RecipeSource.IMAGE;

        // When
        var result = imageRecipeInputStrategy.supports(recipeSource);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy does not support non-IMAGE sources - URL")
    void whenSupports_thenReturnFalseForURL() {
        // Given
        var urlSource = RecipeSource.URL;

        // When
        var result = imageRecipeInputStrategy.supports(urlSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy does not support non-IMAGE sources - TEXT")
    void whenSupports_thenReturnFalseForText() {
        // Given
        var textSource = RecipeSource.TEXT;

        // When
        var result = imageRecipeInputStrategy.supports(textSource);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy creates a UserMessage with media content")
    void whenCreateMessage_thenReturnUserMessageWithMedia() {
        // Given
        byte[] imageBytes = "test image data".getBytes();
        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        var recipeData = new RecipeDataDto(
                base64Image,
                RecipeSource.IMAGE,
                FileExtension.JPEG,
                Language.ENGLISH
        );

        // When
        var userMessage = imageRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals("Extract text from given image", userMessage.getText());
        assertNotNull(userMessage.getMedia());
        assertFalse(userMessage.getMedia().isEmpty());
        assertEquals(1, userMessage.getMedia().size());
    }

    @Test
    @DisplayName("Test if ImageRecipeInputStrategy creates a UserMessage with correct media type without prefix")
    void whenCreateMessageWithoutPrefix_thenReturnUserMessageWithCorrectMediaType() {
        // Given
        byte[] imageBytes = "test image data".getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        var recipeData = new RecipeDataDto(
                base64Image,
                RecipeSource.IMAGE,
                FileExtension.PNG,
                Language.ENGLISH
        );

        // When
        var userMessage = imageRecipeInputStrategy.createMessage(recipeData);

        // Then
        assertNotNull(userMessage);
        assertEquals("Extract text from given image", userMessage.getText());
        assertNotNull(userMessage.getMedia());
        assertFalse(userMessage.getMedia().isEmpty());
    }
}