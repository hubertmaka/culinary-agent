package pl.hubertmaka.culinaryagent.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.hubertmaka.culinaryagent.domain.dtos.ErrorResponseDto;
import pl.hubertmaka.culinaryagent.exceptions.RecipeChatException;
import pl.hubertmaka.culinaryagent.exceptions.RecipeExtractionException;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedRoleException;
import pl.hubertmaka.culinaryagent.exceptions.UnsupportedSchemaException;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);
    /**
     * Handles errors that occur during recipe extraction (e.g. failed AI call,
     * unreadable image/URL).
     */
    @ExceptionHandler(RecipeExtractionException.class)
    public ResponseEntity<ErrorResponseDto> handleRecipeExtractionException(
            RecipeExtractionException ex, HttpServletRequest request) {
        log.error("Recipe extraction failed [path={}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    /**
     * Handles errors that occur during recipe chat interactions (e.g. model
     * returned an unexpected response).
     */
    @ExceptionHandler(RecipeChatException.class)
    public ResponseEntity<ErrorResponseDto> handleRecipeChatException(
            RecipeChatException ex, HttpServletRequest request) {
        log.error("Recipe chat error [path={}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    /**
     * Handles cases where a role value that is not supported by the application
     * logic is encountered (e.g. an unexpected AI message role).
     */
    @ExceptionHandler(UnsupportedRoleException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedRoleException(
            UnsupportedRoleException ex, HttpServletRequest request) {
        log.warn("Unsupported role [path={}]: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles cases where a schema type that is not supported by the application
     * logic is encountered (e.g. an unknown recipe output schema).
     */
    @ExceptionHandler(UnsupportedSchemaException.class)
    public ResponseEntity<ErrorResponseDto> handleUnsupportedSchemaException(
            UnsupportedSchemaException ex, HttpServletRequest request) {
        log.warn("Unsupported schema [path={}]: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Catches any unhandled exception so that the API never leaks an internal
     * stack trace to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error [path={}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.", request);
    }

    /**
     * Utility method to build a standardized error response entity with the given status, message, and request information.
     *
     * @param status  the HTTP status to be returned in the response
     * @param message the error message to be included in the response body
     * @param request the HttpServletRequest containing information about the incoming request
     * @return a ResponseEntity containing an ErrorResponseDto with the specified status and message
     */
    private ResponseEntity<ErrorResponseDto> buildResponse(
            HttpStatus status, String message, HttpServletRequest request) {
        log.debug("Building error response [status={}, message={}, path={}]", status.value(), message, request.getRequestURI());
        ErrorResponseDto body = ErrorResponseDto.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}


