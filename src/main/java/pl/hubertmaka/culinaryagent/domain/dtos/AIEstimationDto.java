package pl.hubertmaka.culinaryagent.domain.dtos;

import java.util.List;

public record AIEstimationDto(
    List<IngredientDto> additionalIngredients,
    Integer estimatedPreparationTimeMinutes,
    String originalText
) { }
