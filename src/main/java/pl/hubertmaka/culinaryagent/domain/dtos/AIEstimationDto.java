package pl.hubertmaka.culinaryagent.domain.dtos;

import java.util.List;

/**
 * DTO representing the estimation of additional ingredients and preparation time for a recipe.
 *
 * @param additionalIngredients List of additional ingredients suggested by the AI.
 * @param estimatedPreparationTimeMinutes Estimated preparation time in minutes based on the AI's analysis.
 */
public record AIEstimationDto(
    List<IngredientDto> additionalIngredients,
    Integer estimatedPreparationTimeMinutes
) { }
