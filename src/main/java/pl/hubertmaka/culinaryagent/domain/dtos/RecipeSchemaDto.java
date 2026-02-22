package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO representing the schema of a recipe, including its content, ingredients, preparation time, and AI estimations.
 *
 * @param content The textual content of the recipe, including instructions and descriptions.
 * @param ingredients The list of ingredients required for the recipe, each represented as an IngredientDto.
 * @param preparationTimeInMinutes The estimated preparation time for the recipe in minutes.
 * @param aiEstimations The list of AI estimations, which may include additional ingredients and estimated preparation time.
 */
public record RecipeSchemaDto(
    @NotBlank
    @Size(max = 20000, message = "Recipe content must not exceed 20,000 characters")
    String content,
    @NotBlank
    List<IngredientDto> ingredients,
    @Min(value = 0, message = "Preparation time must be a non-negative integer.")
    @Max(value = 60 * 24 * 7, message = "Preparation time must be less than one week in minutes.")
    Integer preparationTimeInMinutes,
    @NotBlank
    List<AIEstimationDto> aiEstimations
) { }
