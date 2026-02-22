package pl.hubertmaka.culinaryagent.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

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
        int status,
        String error,
        String message,
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

