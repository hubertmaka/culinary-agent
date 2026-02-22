package pl.hubertmaka.culinaryagent.services;

import pl.hubertmaka.culinaryagent.domain.dtos.RecipeDataRequestDto;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaResponseDto;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

import java.util.List;

/**
 * Service interface for extracting recipe information from a given recipe data.
 */
public interface RecipeExtractorService {
    /**
     * Extracts recipe information from the provided recipe data and returns a structured recipe schema.
     *
     * @param recipeDataRequest The data transfer object containing the recipe data to be extracted.
     * @return A RecipeSchemaDto containing the structured recipe information extracted from the input data.
     */
    RecipeSchemaResponseDto extract(RecipeDataRequestDto recipeDataRequest);
    /**
     * Retrieves a list of supported recipe sources that this extractor can handle.
     *
     * @return A list of RecipeSource enums representing the supported recipe sources.
     */
    List<RecipeSource> getSupportedSources();
}
