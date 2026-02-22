package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO representing an ingredient.
 *
 * @param ingredient the name of the ingredient
 */
public record IngredientDto(
    @NotBlank
    @Size(max = 255, message = "Ingredient name must not exceed 255 characters")
    String ingredient
) { }
