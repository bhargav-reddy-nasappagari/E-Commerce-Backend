# Authentication Module Test Report

**Project:** E-Commerce Backend  
**Module:** Phase 4 – Authentication Module  
**Version:** v0.4.0  
**Framework:** Spring Boot 3.3.2  
**Java Version:** Java 21 LTS  
**Database:** PostgreSQL  
**Authentication:** Spring Security + JWT (JJWT 0.12.x)  
**API Testing Tool:** Postman  
**Test Date:** July 21, 2026

---

# 1. Objective

The objective of this testing phase was to verify the correctness, robustness, and security of the Authentication Module after implementation.

Testing focused on validating:

- User Registration
- User Login
- Password Encryption
- JWT Generation
- Default Role Assignment
- Exception Handling
- Authentication Failure Responses
- API Contract Consistency
- Spring Security Configuration

The goal was not only to ensure that each endpoint functions correctly but also to validate the interaction between the Controller, Service, Security, Persistence, and Database layers.

---

# 2. Test Environment

| Component | Version |
|-----------|---------|
| Java | 21 LTS |
| Spring Boot | 3.3.2 |
| PostgreSQL | 16+ |
| Spring Security | 6.x |
| JWT Library | JJWT 0.12.x |
| API Client | Postman |
| Build Tool | Maven |

---

# 3. Authentication Endpoints

## Register User

```
POST /api/v1/auth/register
```

### Expected Result

- HTTP 201 Created
- User stored in database
- Password encrypted
- Default CUSTOMER role assigned
- JWT returned

---

## Login

```
POST /api/v1/auth/login
```

### Expected Result

- HTTP 200 OK
- JWT returned
- User authenticated successfully

---

# 4. Test Cases

---

## Test Case 1

### Register New User

### Request

```json
{
    "fullName": "Bunny",
    "email": "bunny@gmail.com",
    "password": "Bunny@7221"
}
```

### Expected

- 201 Created
- JWT generated
- CUSTOMER role assigned

### Actual

✔ PASS

Response

```json
{
    "success": true,
    "message": "User registered successfully.",
    "data": {
        "token": "<JWT>",
        "email": "bunny@gmail.com",
        "fullName": "Bunny",
        "roles": [
            "ROLE_CUSTOMER"
        ]
    }
}
```

---

## Test Case 2

### Duplicate Registration

### Request

Same registration request executed again.

### Expected

409 Conflict

### Actual

✔ PASS

DuplicateResourceException correctly thrown and handled by GlobalExceptionHandler.

---

## Test Case 3

### Successful Login

### Request

```json
{
    "email": "bunny@gmail.com",
    "password": "Bunny@7221"
}
```

### Expected

200 OK

JWT returned.

### Actual

✔ PASS

AuthenticationManager authenticated successfully.

JWT generated successfully.

---

## Test Case 4

### Wrong Password

### Request

```json
{
    "email": "bunny@gmail.com",
    "password": "WrongPassword"
}
```

### Expected

401 Unauthorized

### Initial Result

❌ FAIL

Returned

```
500 Internal Server Error
```

### Root Cause

AuthenticationManager throws

```
BadCredentialsException
```

which was not explicitly handled.

The exception propagated to the generic Exception handler resulting in HTTP 500.

### Resolution

Added handler for

```
AuthenticationException
```

inside GlobalExceptionHandler.

### Final Result

✔ PASS

Returned

```
401 Unauthorized
```

Response message

```
Invalid email or password.
```

---

## Test Case 5

### Unknown Email

### Request

```json
{
    "email": "unknown@gmail.com",
    "password": "Password123"
}
```

### Expected

401 Unauthorized

### Actual

✔ PASS

Returns identical response as wrong password to prevent username enumeration attacks.

---

## Test Case 6

### DTO Validation

### Invalid Payload

```json
{
    "fullName":"",
    "email":"abc",
    "password":"123"
}
```

### Expected

400 Bad Request

### Actual

✔ PASS

Bean Validation executed successfully.

---

# 5. Database Verification

After successful registration the following validations were performed.

## User Table

Verified

- User inserted successfully
- Email stored correctly
- Password encrypted using BCrypt

Password format observed

```
$2a$
```

or equivalent BCrypt prefix.

---

## User Roles

Verified

```
User
        |
        | Many-to-Many
        |
Role
```

Default role assigned

```
ROLE_CUSTOMER
```

Relationship stored correctly in

```
user_roles
```

join table.

---

# 6. JWT Verification

Verified

- JWT generated successfully
- Subject contains user email
- Issued time present
- Expiration time present
- HS256 signing algorithm used

Example Payload

```json
{
    "sub":"bunny@gmail.com",
    "iat": ...,
    "exp": ...
}
```

---

# 7. Security Verification

Verified

✔ BCrypt Password Encoding

✔ AuthenticationManager

✔ JwtUtil

✔ JwtAuthFilter Initialization

✔ UserDetailsService

✔ SecurityFilterChain

✔ Public Authentication Endpoints

```
/api/v1/auth/**
```

Protected endpoints remain secured.

---

# 8. Issues Encountered During Development

---

## Issue 1

### JWT Secret Not Loading

Application failed during startup.

Exception

```
Illegal base64 character '-'
```

### Root Cause

Spring Boot was reading the fallback value defined inside

```
application.yml
```

instead of loading the `.env` file.

### Resolution

Integrated

```
spring-dotenv
```

dependency.

Created

```
.env
```

file in project root.

JWT secret loaded successfully.

---

## Issue 2

### Incorrect Endpoint

Initially tested

```
POST /api/v1/register
```

instead of

```
POST /api/v1/auth/register
```

Result

401 Unauthorized

Resolution

Corrected endpoint mapping.

---

## Issue 3

### Incorrect JSON Property

Sent

```json
{
    "fullname":"Bunny"
}
```

instead of

```json
{
    "fullName":"Bunny"
}
```

Resolution

Corrected property name.

---

## Issue 4

### Authentication Failure Returned HTTP 500

Root Cause

Missing AuthenticationException handler.

Resolution

Added dedicated exception handler.

Returned

```
401 Unauthorized
```

---

# 9. Postman Collection

Authentication folder contains

- Register User
- Register Duplicate User
- Login
- Login – Wrong Password
- Login – Unknown User

Collection Variables

```
baseUrl

customerEmail

customerPassword

jwtToken
```

JWT automatically stored after successful authentication for future API testing.

---

# 10. Architecture Validation

The following architecture has been validated through integration testing.

```
Client (Postman)
        │
        ▼
AuthController
        │
        ▼
AuthService
        │
        ▼
AuthServiceImpl
        │
        ├── UserRepository
        ├── RoleRepository
        ├── PasswordEncoder
        ├── JwtUtil
        ├── AuthenticationManager
        │
        ▼
Spring Security
        │
        ▼
PostgreSQL
```

Every layer participated successfully in the authentication workflow.

---

# 11. Remaining Validation

One integration test remains pending.

## JWT Authorization Testing

To be executed after the first protected business endpoint is implemented.

Scenarios

- Valid JWT
- Missing JWT
- Invalid JWT
- Tampered JWT
- Expired JWT

These tests will validate JwtAuthFilter and SecurityContext population.

---

# 12. Conclusion

The Authentication Module has been successfully implemented and validated through end-to-end integration testing.

The module correctly performs user registration, authentication, password encryption, JWT generation, default role assignment, validation, and exception handling.

Several configuration and security-related issues were identified during testing—including JWT secret loading, endpoint configuration, request payload mismatches, and authentication exception handling—and were resolved as part of the verification process.

The authentication subsystem is now stable and ready to support subsequent protected business modules such as Categories, Products, Cart, Orders, and Admin operations.

---

# Phase Status

**Phase 4 – Authentication Module**

**Status:** ✅ Completed

**Version:** v0.4.0

**Overall Assessment:** Stable and Ready for Integration