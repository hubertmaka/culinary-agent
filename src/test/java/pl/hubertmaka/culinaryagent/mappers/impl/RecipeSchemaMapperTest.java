package pl.hubertmaka.culinaryagent.mappers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSchemaMapperTest {

    private RecipeSchemaMapper recipeSchemaMapper;

    @BeforeEach
    void setUp() {
        recipeSchemaMapper = new RecipeSchemaMapper();
    }

    @Test
    @DisplayName("Test if RecipeSchemaMapper is initialized correctly")
    void whenInitialized_thenNotNull() {
        // Then
        assertNotNull(recipeSchemaMapper);
    }

    @Test
    @DisplayName("Test if getConverter returns non-null BeanOutputConverter")
    void whenGetConverter_thenReturnNotNull() {
        // When
        var converter = recipeSchemaMapper.getConverter();

        // Then
        assertNotNull(converter);
    }

    @Test
    @DisplayName("Test if getFormat returns non-null and non-empty format string")
    void whenGetFormat_thenReturnNonEmptyFormat() {
        // When
        var format = recipeSchemaMapper.getFormat();

        // Then
        assertNotNull(format);
        assertFalse(format.isBlank());
    }

    @Test
    @DisplayName("Test if mapFrom correctly deserializes valid JSON to RecipeSchemaDto")
    void whenMapFrom_withValidJson_thenReturnRecipeSchemaDto() {
        // Given
        String json = """
                {
                  "content": "Spaghetti Bolognese",
                  "ingredients": [],
                  "preparationTimeInMinutes": 30,
                  "aiEstimations": []
                }
                """;

        // When
        RecipeSchemaDto result = recipeSchemaMapper.mapFrom(json);

        // Then
        assertNotNull(result);
        assertEquals("Spaghetti Bolognese", result.content());
        assertEquals(30, result.preparationTimeInMinutes());
        assertNotNull(result.ingredients());
        assertTrue(result.ingredients().isEmpty());
        assertNotNull(result.aiEstimations());
        assertTrue(result.aiEstimations().isEmpty());
    }

    @Test
    @DisplayName("Test if mapFrom correctly deserializes JSON with ingredients")
    void whenMapFrom_withIngredients_thenReturnRecipeSchemaDtoWithIngredients() {
        // Given
        String json = """
                {
                  "content": "Tomato Soup",
                  "ingredients": [
                    {
                      "ingredient": "Tomato"
                    }
                  ],
                  "preparationTimeInMinutes": 20,
                  "aiEstimations": []
                }
                """;

        // When
        RecipeSchemaDto result = recipeSchemaMapper.mapFrom(json);

        // Then
        assertNotNull(result);
        assertEquals("Tomato Soup", result.content());
        assertEquals(1, result.ingredients().size());
        assertEquals("Tomato", result.ingredients().get(0).ingredient());
    }

    @Test
    @DisplayName("Test if mapTo throws UnsupportedOperationException")
    void whenMapTo_thenThrowUnsupportedOperationException() {
        // Given
        var dto = new RecipeSchemaDto("Test", List.of(), 10, List.of());

        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> recipeSchemaMapper.mapTo(dto));
    }

    @Test
    @DisplayName("Test if mapTo throws UnsupportedOperationException with correct message")
    void whenMapTo_thenThrowUnsupportedOperationExceptionWithMessage() {
        // Given
        var dto = new RecipeSchemaDto("Test", List.of(), 10, List.of());

        // When
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> recipeSchemaMapper.mapTo(dto)
        );

        // Then
        assertEquals("Mapping from RecipeSchemaDto to String is not supported", exception.getMessage());
    }
}
