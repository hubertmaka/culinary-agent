package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.FileExtension;
import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.RecipeSource;

public record RecipeDataDto(
    String content,
    RecipeSource contentType,
    FileExtension fileExtension,
    Language language
) { }
