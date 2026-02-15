package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Model;

public record MetadataDto(
    Integer inputTokens,
    Integer outputTokens,
    Integer cachedTokens,
    Model model
) { }
