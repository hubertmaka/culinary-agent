package pl.hubertmaka.culinaryagent.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(max = 10000, message = "Content must not exceed 10,000 characters")
    String content,
    @NotNull
    RecipeSource contentType,
    @NotNull
    FileExtension fileExtension,
    @NotNull
    Language language
) { }
