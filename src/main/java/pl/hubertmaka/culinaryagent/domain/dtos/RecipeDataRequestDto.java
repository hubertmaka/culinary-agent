package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

/**
 * DTO representing the raw data of a recipe, including its content, source type, file extension, and language.
 *
 * @param content The raw content of the recipe, which can be a URL, image data, or text.
 * @param contentType The type of the content source (e.g., URL, IMAGE, TEXT).
 * @param fileExtension The file extension associated with the content (e.g., .jpg for images).
 * @param language The language of the recipe content.
 */
public record RecipeDataRequestDto(
    String content,
    RecipeSource contentType,
    FileExtension fileExtension,
    Language language
) { }
