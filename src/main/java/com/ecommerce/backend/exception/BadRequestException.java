package com.ecommerce.backend.exception;

/**
 * Thrown when a request violates a business rule or represents
 * an invalid operation.
 *
 * Examples:
 * - Cart is empty.
 * - Category cannot be deleted because it contains products.
 * - Invalid order status transition.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}