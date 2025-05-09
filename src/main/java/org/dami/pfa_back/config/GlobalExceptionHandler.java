package org.dami.pfa_back.config; // Or a dedicated 'exception' package

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request, String customMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", customMessage != null ? customMessage : ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", "")); // Get request path
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request, "Resource not found");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", ex.getStatusCode().value());
        body.put("error", ex.getStatusCode());
        body.put("message", ex.getReason() != null ? ex.getReason() : "An error occurred"); // Use getReason for ResponseStatusException
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
        // Could be more specific based on context (e.g., file storage issue vs network)
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, "File system or I/O error occurred");
    }

    // General fallback for other RuntimeExceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Unhandled RuntimeException: ", ex); // Log the full stack trace for unexpected errors
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, "An unexpected internal server error occurred.");
    }

    // You can add more specific exception handlers here
}
