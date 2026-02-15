package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Role;

public record MessageDto(
    Role role,
    String content
) { }
