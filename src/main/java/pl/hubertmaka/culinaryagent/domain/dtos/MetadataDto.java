package pl.hubertmaka.culinaryagent.domain.dtos;

import pl.hubertmaka.culinaryagent.domain.enums.Model;

/**
 * DTO representing metadata information related to token usage and the model used.
 *
 * @param inputTokens  The number of tokens in the input.
 * @param outputTokens The number of tokens in the output.
 * @param totalTokens  The total number of tokens used (input + output).
 * @param model        The model used for processing.
 */
public record MetadataDto(
    Integer inputTokens,
    Integer outputTokens,
    Integer totalTokens,
    String model
) { }
