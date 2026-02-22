package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * DTO representing the estimation of additional ingredients and preparation time for a recipe.
 *
 * @param additionalIngredients List of additional ingredients suggested by the AI.
 * @param estimatedPreparationTimeMinutes Estimated preparation time in minutes based on the AI's analysis.
 */
public record AIEstimationDto(
    List<IngredientDto> additionalIngredients,
    @Min(value = 0, message = "Estimated preparation time must be a non-negative integer.")
    @Max(value = 60 * 24 * 7, message = "Estimated preparation time must be less than one week in minutes.")
    Integer estimatedPreparationTimeMinutes
) { }
