package pl.hubertmaka.culinaryagent.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data Transfer Object representing an error response.
 *
 * @param timestamp the date and time when the error occurred
 * @param status    the HTTP status code of the error
 * @param error     a brief description of the error
 * @param message   a detailed message about the error
 */
public record ErrorResponseDto(
    @JsonFormat(pattern = "yyyy-MM-dd:HH:mm:ss")
    LocalDate timestamp,
    @Min(value = 100, message = "Status code must be a non-negative integer")
    @Max(value = 599, message = "Status code must be less than 600")
    int status,
    @NotBlank
    @Size(max = 256, message = "Error description must not exceed 256 characters")
    String error,
    @NotBlank
    @Size(max = 1024, message = "Error message must not exceed 1024 characters")
    String message,
    @NotBlank
    @Size(max = 256, message = "Path must not exceed 256 characters")
    String path
) {
    /**
     * Factory method to create an instance of ErrorResponseDto.
     *
     * @param status  the HTTP status code of the error
     * @param error   a brief description of the error
     * @param message a detailed message about the error
     * @param path    the path of the request that caused the error
     * @return an instance of ErrorResponseDto with the current timestamp
     */
    public static ErrorResponseDto of(
        int status,
        String error,
        String message,
        String path
    ) {
        return new ErrorResponseDto(LocalDate.now(), status, error, message, path);
    }
}

