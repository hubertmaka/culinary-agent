package pl.hubertmaka.culinaryagent.domain.dtos;

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
    String content,
    List<IngredientDto> ingredients,
    Integer preparationTimeInMinutes,
    List<AIEstimationDto> aiEstimations
) { }
