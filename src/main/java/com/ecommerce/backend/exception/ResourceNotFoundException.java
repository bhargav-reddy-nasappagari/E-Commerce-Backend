package com.ecommerce.backend.exception;

/**
 * Thrown when a requested resource cannot be found.
 *
 * Example:
 * throw new ResourceNotFoundException("Product", "id", 5);
 *
 * Message:
 * Product not found with id : '5'
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName + " not found with " + fieldName + " : '" + fieldValue + "'");
    }
}