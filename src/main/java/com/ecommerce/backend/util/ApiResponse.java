package com.ecommerce.backend.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * Standard API response wrapper used across all REST endpoints.
 *
 * @param <T> the type of the response payload
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiResponse<T> {

    private final boolean success;

    private final String message;

    private final T data;

    private final Instant timestamp;

    /**
     * Creates a successful API response.
     *
     * @param data    the response payload
     * @param message the success message
     * @param <T>     the response payload type
     * @return a successful ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                true,
                message,
                data,
                Instant.now()
        );
    }

    /**
     * Creates an error API response without additional data.
     *
     * @param message the error message
     * @param <T>     the response payload type
     * @return an error ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                Instant.now()
        );
    }

    /**
     * Creates an error API response with additional error details.
     * Intended for structured error payloads such as validation errors.
     *
     * @param message the error message
     * @param data    additional error details
     * @param <T>     the response payload type
     * @return an error ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(
                false,
                message,
                data,
                Instant.now()
        );
    }
}