package com.ecommerce.backend.exception;

/**
 * Thrown when the requested quantity of a product exceeds
 * the available inventory.
 *
 * Example message:
 * Insufficient stock for product 'Laptop'. Requested: 5, Available: 2.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName, int requestedQuantity, int availableQuantity) {
        super(String.format(
                "Insufficient stock for product '%s'. Requested: %d, Available: %d.",
                productName,
                requestedQuantity,
                availableQuantity
        ));
    }
}