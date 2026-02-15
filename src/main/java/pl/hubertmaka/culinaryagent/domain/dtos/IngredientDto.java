package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Unit;

public record IngredientDto(
    String name,
    Integer amount,
    Unit unit,
    String originalText
) { }
