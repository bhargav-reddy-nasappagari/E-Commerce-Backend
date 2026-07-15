package com.ecommerce.backend.entity;

/**
 * Represents the lifecycle states of an order.
 *
 * These values are persisted in the database through the Order entity
 * and are used throughout the application to track order progress.
 */
public enum OrderStatus {

    PENDING,

    CONFIRMED,

    SHIPPED,

    DELIVERED,

    CANCELLED

}