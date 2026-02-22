package pl.hubertmaka.culinaryagent.domain.dtos;


import jakarta.validation.constraints.*;

/**
 * DTO representing metadata information related to token usage and the model used.
 *
 * @param inputTokens  The number of tokens in the input.
 * @param outputTokens The number of tokens in the output.
 * @param totalTokens  The total number of tokens used (input + output).
 * @param model        The model used for processing.
 */
public record MetadataDto(
    @Min(value = 0, message = "Input tokens must be non-negative")
    @Max(value = Integer.MAX_VALUE, message = "Input tokens must not exceed " + Integer.MAX_VALUE)
    Integer inputTokens,
    @Min(value = 0, message = "Output tokens must be non-negative")
    @Max(value = Integer.MAX_VALUE, message = "Output tokens must not exceed " + Integer.MAX_VALUE)
    Integer outputTokens,
    @Min(value = 0, message = "Total tokens must be non-negative")
    @Max(value = Integer.MAX_VALUE, message = "Total tokens must not exceed " + Integer.MAX_VALUE)
    Integer totalTokens,
    @NotNull
    @Size(max = 255, message = "Model name must not exceed 255 characters")
    String model
) { }
