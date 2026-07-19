package com.ecommerce.backend.exception;

/**
 * Thrown when attempting to create or update a resource
 * that violates a uniqueness constraint.
 *
 * Examples:
 * - Email address already exists.
 * - Category name already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}