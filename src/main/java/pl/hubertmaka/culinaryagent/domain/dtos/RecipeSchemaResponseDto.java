package pl.hubertmaka.culinaryagent.domain.dtos;

/**
 * DTO representing the response containing the extracted recipe schema and associated metadata.
 *
 * @param recipeSchema The extracted recipe schema information.
 * @param metadata     Metadata about the extraction process, such as source and confidence level.
 */
public record RecipeSchemaResponseDto(
    RecipeSchemaDto recipeSchema,
    MetadataDto metadata
) { }
