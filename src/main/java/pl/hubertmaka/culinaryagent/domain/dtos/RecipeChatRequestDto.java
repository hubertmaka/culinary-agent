package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Language;
import pl.hubertmaka.culinaryagent.domain.enums.Voice;

import java.util.List;

/**
 * DTO representing a request for a recipe chat interaction.
 *
 * @param schema   The recipe schema containing the recipe details and metadata.
 * @param messages The list of messages exchanged in the chat interaction.
 * @param voice    The voice to be used for text-to-speech output.
 * @param language The language to be used for the chat interaction.
 */
public record RecipeChatRequestDto(
    RecipeSchemaDto schema,
    List<MessageDto> messages,
    Voice voice,
    Language language
) { }
