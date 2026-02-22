package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the response containing the extracted recipe schema and associated metadata.
 *
 * @param recipeSchema The extracted recipe schema information.
 * @param metadata     Metadata about the extraction process, such as source and confidence level.
 */
public record RecipeSchemaResponseDto(
    @NotNull
    RecipeSchemaDto recipeSchema,
    @NotNull
    MetadataDto metadata
) { }
