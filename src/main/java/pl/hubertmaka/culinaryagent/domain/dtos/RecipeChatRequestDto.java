package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;

import java.util.List;

public record RecipeChatRequestDto(
    RecipeSchemaDto schema,
    List<MessageDto> messages,
    Voice voice,
    Language language
) { }
