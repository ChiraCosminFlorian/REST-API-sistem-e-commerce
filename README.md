# 🛒 E-Commerce REST API

## Overview

A full-featured e-commerce REST API built with **Spring Boot 3.x**. It provides secure endpoints for user authentication, product and category management, and order processing with stock validation. The API uses **JWT-based authentication**, **role-based access control** (USER/ADMIN), and is fully containerized with **Docker**.

---

## Tech Stack

| Technology        | Description                          |
|-------------------|--------------------------------------|
| Java 21           | Programming language                 |
| Spring Boot 3.2   | Application framework                |
| Spring Security   | Authentication & authorization (JWT) |
| Spring Data JPA   | Data persistence & ORM               |
| PostgreSQL 16     | Relational database                  |
| Docker            | Containerization                     |
| Maven             | Build & dependency management        |

---

## Architecture

The application follows a **layered architecture** pattern:

```
Controller (REST endpoints)
    ↓
Service (business logic)
    ↓
Repository (data access via JPA)
    ↓
PostgreSQL Database
```

Cross-cutting concerns such as security (JWT filter chain), validation, and exception handling are managed through dedicated components.

---

## Prerequisites

- **Java 21** (JDK)
- **Maven 3.9+**
- **Docker & Docker Compose** (for containerized setup)
- **PostgreSQL 16** (only if running locally without Docker)

---

## Getting Started

### Run with Docker (recommended)

```bash
git clone https://github.com/ChiraCosminFlorian/REST-API-sistem-e-commerce.git
cd REST-API-sistem-e-commerce
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### Run locally

1. Make sure PostgreSQL is running on `localhost:5432` with a database named `ecommerce_db`.
2. Update credentials in `src/main/resources/application.yml` if needed.
3. Build and run:

```bash
mvn spring-boot:run
```

---

## API Documentation

### 🔐 Auth

| Method | Endpoint             | Description         | Access |
|--------|----------------------|---------------------|--------|
| POST   | `/api/auth/register` | Register a new user | Public |
| POST   | `/api/auth/login`    | Login & get JWT     | Public |

**Register request body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Login response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "john@example.com",
  "role": "USER"
}
```

---

### 📦 Products

| Method | Endpoint                            | Description          | Access        |
|--------|-------------------------------------|----------------------|---------------|
| GET    | `/api/products?page=0&size=10`      | List all (paginated) | Authenticated |
| GET    | `/api/products/{id}`                | Get by ID            | Authenticated |
| GET    | `/api/products/search?name=phone`   | Search by name       | Authenticated |
| GET    | `/api/products/category/{catId}`    | Filter by category   | Authenticated |
| POST   | `/api/products`                     | Create product       | ADMIN         |
| PUT    | `/api/products/{id}`                | Update product       | ADMIN         |
| DELETE | `/api/products/{id}`                | Delete product       | ADMIN         |

**Create/Update request body:**
```json
{
  "name": "Smartphone X",
  "description": "Latest model smartphone",
  "price": 999.99,
  "stockQuantity": 50,
  "imageUrl": "https://example.com/phone.jpg",
  "categoryId": 1
}
```

---

### 📂 Categories

| Method | Endpoint               | Description    | Access        |
|--------|------------------------|----------------|---------------|
| GET    | `/api/categories`      | List all       | Authenticated |
| GET    | `/api/categories/{id}` | Get by ID      | Authenticated |
| POST   | `/api/categories`      | Create         | ADMIN         |
| PUT    | `/api/categories/{id}` | Update         | ADMIN         |
| DELETE | `/api/categories/{id}` | Delete         | ADMIN         |

---

### 🛍️ Orders

> **Note:** All order endpoints require the `Authorization: Bearer <token>` header.

| Method | Endpoint                            | Description          | Access        |
|--------|-------------------------------------|----------------------|---------------|
| POST   | `/api/orders`                       | Place an order       | Authenticated |
| GET    | `/api/orders/my`                    | Get my orders        | Authenticated |
| GET    | `/api/orders/{id}`                  | Get order details    | Owner / ADMIN |
| PUT    | `/api/orders/{id}/status?status=`   | Update order status  | ADMIN         |
| GET    | `/api/orders?page=0&size=10`        | List all (paginated) | ADMIN         |

**Place order request body:**
```json
{
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

---

## Authentication

The API uses **JWT (JSON Web Tokens)** for stateless authentication.

### Flow

1. **Register** or **login** to receive a JWT token.
2. Include the token in subsequent requests via the `Authorization` header.

### Example with cURL

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "john@example.com", "password": "password123"}'
```

**Authenticated request:**
```bash
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## Database Schema

The application uses **5 tables** with the following relationships:

| Table         | Description                                  |
|---------------|----------------------------------------------|
| `users`       | User accounts with roles (USER/ADMIN)        |
| `categories`  | Product categories                           |
| `products`    | Products linked to a category (ManyToOne)    |
| `orders`      | Orders placed by users (ManyToOne → User)    |
| `order_items` | Line items (ManyToOne → Order and Product)   |

```
users ──< orders ──< order_items >── products >── categories
```

---

## Project Structure

```
src/main/java/com/ecommerce/
├── config/          # Security configuration, application beans
├── controller/      # REST controllers (Auth, Product, Category, Order)
├── dto/             # Data Transfer Objects (requests & responses)
├── entity/          # JPA entities and enums
├── exception/       # Custom exceptions and global handler
├── repository/      # Spring Data JPA repositories
├── security/        # JWT service and authentication filter
├── service/         # Business logic layer
└── EcommerceApplication.java
```

---

## License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
