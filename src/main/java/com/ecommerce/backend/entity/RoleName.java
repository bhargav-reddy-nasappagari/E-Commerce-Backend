package com.ecommerce.backend.entity;

/**
 * Defines the roles supported by the application.
 *
 * These values are persisted in the database through the Role entity
 * and are later mapped to Spring Security authorities.
 */
public enum RoleName {

    ADMIN,

    CUSTOMER

}