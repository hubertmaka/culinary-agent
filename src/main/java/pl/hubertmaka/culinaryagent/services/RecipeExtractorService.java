package pl.hubertmaka.culinaryagent.services;

import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;

/**
 * Service interface for extracting recipe information from a given recipe data.
 */
public interface RecipeExtractorService {
    /**
     * Extracts recipe information from the provided recipe data and returns a structured recipe schema.
     *
     * @param recipeSchema The data transfer object containing the recipe data to be extracted.
     * @return A RecipeSchemaDto containing the structured recipe information extracted from the input data.
     */
    public RecipeSchemaDto extract(RecipeDataDto recipeSchema);
}
