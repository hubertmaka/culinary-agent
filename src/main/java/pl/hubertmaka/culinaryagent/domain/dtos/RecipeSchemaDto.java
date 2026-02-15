package pl.hubertmaka.culinaryagent.domain.dtos;

import java.util.List;

public record RecipeSchemaDto(
    String content,
    List<IngredientDto> ingredients,
    Integer preparationTimeInMinutes,
    List<AIEstimationDto> aiEstimations,
    MetadataDto metadata
) { }
