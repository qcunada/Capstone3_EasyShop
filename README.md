# 🛒 EasyShop eCommerce API

A Java Spring Boot RESTful API for an online store. Features user authentication, product browsing and search, admin-controlled category/product management, and a persistent shopping cart system.

## 📦 Features

- ✅ **User Registration & Login**
- ✅ **JWT Authentication & Authorization**
- ✅ **Browse & Search Products**
- ✅ **Filter by Category, Price Range, Color**
- ✅ **Admin-only: Add/Edit/Delete Products & Categories**
- ✅ **Persistent Shopping Cart**
- ✅ **Role-based Access Control**

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JDBC**
- **MySQL**
- **Postman** (for testing)
- **JWT (JSON Web Tokens)**

## 🗂️ Endpoints Overview

### 📦 Products
- `GET /products` – List/search products (supports filters)
- `GET /products/{id}` – Get product by ID
- `POST /products` – Add product (admin only)
- `PUT /products/{id}` – Update product (admin only)
- `DELETE /products/{id}` – Delete product (admin only)

### 🗃️ Categories
- `GET /categories` – List all categories
- `GET /categories/{id}` – Get category by ID
- `POST /categories` – Add category (admin only)
- `PUT /categories/{id}` – Update category (admin only)
- `DELETE /categories/{id}` – Delete category (admin only)

### 🛒 Shopping Cart (Authenticated Users)
- `GET /cart` – View user's shopping cart
- `POST /cart/products/{productId}` – Add product to cart
- `PUT /cart/products/{productId}` – Update quantity
- `DELETE /cart` – Clear cart

### 👤 Profile Management
- `GET /profile` – Get current user's profile
- `POST /profile` – Create profile
- `PUT /profile` – Update profile

<img width="1083" alt="Image" src="https://github.com/user-attachments/assets/3de80747-25e5-4a01-aff6-f6b5ab33b75b" />
