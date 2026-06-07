# Marketplace — E-commerce Platform

Multi-service marketplace built with Java/Spring Boot. Buyers can search for products and place orders, sellers can manage their catalog and track sales.

## Tech Stack

- **Java 17, Spring Boot** — core framework
- **Spring Security + JWT** — authentication and role-based access
- **PostgreSQL** — primary database
- **Redis** — caching
- **RabbitMQ** — async messaging between services
- **Elasticsearch** — full-text product search
- **MinIO** — image storage
- **Docker / Docker Compose** — infrastructure
- **Liquibase** — database migrations
- **YooKassa** — payment processing

## Services

| Service | Description |
|---|---|
| user-service | Registration, authentication, seller profiles |
| product-service | Product catalog, categories, search |
| order-service | Cart, orders |
| notification-service | Email notifications |

## Getting Started

```bash
git clone https://github.com/your-username/marketplace.git
cd marketplace
docker-compose up -d
```

## Features

- Registration and login (JWT + Refresh token)
- Product catalog with filters and search
- Product image uploads
- Cart and order management
- Payment processing via YooKassa
- Email notifications on order status updates
- Seller dashboard with sales analytics
- Admin panel
