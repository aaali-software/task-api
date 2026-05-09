# Task API 🚀

![CI](https://github.com/aaali-software/task-api/actions/workflows/ci.yml/badge.svg?branch=main&event=push)

A production-style **Task Management REST API** built with **Spring Boot**, secured with JWT authentication, containerized with Docker, and backed by PostgreSQL.

---

## 🌐 Live Demo

- **API Base URL:** https://task-api-tug6.onrender.com
- **Swagger UI:** https://task-api-tug6.onrender.com/swagger-ui/index.html

---

## 🧠 Features

- ✅ Full CRUD operations for tasks
- ✅ Filtering by **status** and **priority**
- ✅ Pagination (`page`, `size`)
- ✅ Sorting (`sort=field,asc|desc`)
- ✅ PostgreSQL persistence
- ✅ Swagger/OpenAPI documentation
- ✅ Caching with eviction
- ✅ API rate limiting
- ✅ Logging + Observability
- ✅ CI/CD pipeline (GitHub Actions)
- ✅ 87%+ automated test coverage (JUnit + Mockito)

## 🔐 Security Features

- ✅ JWT-based authentication
- ✅ Access tokens (short-lived)
- ✅ Refresh tokens (DB-backed, rotated on use)
- ✅ Role-based authorization (`USER` / `ADMIN`)
- ✅ Protected API endpoints
- ✅ Rate limiting with Bucket4j

## ⚡ Performance Features

- Caffeine in-memory caching
- Cache eviction on task updates/deletes
- Pagination and sorting support

## 🏗️ Architecture

- Controller layer
- Service layer
- Repository layer
- DTO-based request/response mapping
- PostgreSQL persistence
- Stateless JWT authentication

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Gradle**
- **Caffeine Cache**
- **Bucket4j**
- **JUnit 5 + Mockito**
- **Swagger (OpenAPI)**
- **Docker**
- **Render (Cloud Deployment)**

---

## 📦 API Endpoints

| Method | Endpoint                 | Description                                             |
| ------ | ------------------------ | ------------------------------------------------------- |
| GET    | `/api/tasks`             | Get all tasks (supports filtering, pagination, sorting) |
| GET    | `/api/tasks/{id}`        | Get task by ID                                          |
| POST   | `/api/tasks`             | Create a new task                                       |
| PATCH  | `/api/tasks/{id}/status` | Update task status                                      |
| PUT    | `/api/tasks/{id}`        | Update full task                                        |
| DELETE | `/api/tasks/{id}`        | Delete task                                             |
| POST   | `/api/auth/register`     | Register a new user                                     |
| POST   | `/api/auth/login`        | Authenticate user and access/refresh tokens             |
| POST   | `/api/auth/refresh`      | Refresh access token                                    |

---

## 🔍 Query Parameters

### Pagination

```text
?page=0\&size=10
```

### Sorting

```text
?sort=createdAt,desc
?sort=dueDate,asc
```

### Filtering

```text
?status=PENDING
?priority=HIGH
?status=IN_PROGRESS&priority=MEDIUM
```

## 🐳 Docker Compose

Run the full stack locally:

```bash
docker compose up --build
```

### 📘 Swagger UI

Once the app is running:

```text
http://localhost:8080/swagger-ui/index.html
```

Stop containers:

```bash
docker compose down
```

### 🧪 Example Request

Create Task

POST /api/tasks

```json
{
  "title": "Finish backend project",
  "description": "Complete Task API with tests and deployment",
  "priority": "HIGH",
  "dueDate": "2026-04-20T18:00:00"
}
```

### ⚙️ Local Setup

1. Clone repo

```bash
git clone https://github.com/aaali-software/task-api.git
cd task-api
```

2. Configure PostgreSQL

Create a database:

```sql
CREATE DATABASE taskdb;
```

Update application.properties :

```text
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. Run the app

```bash
./gradlew bootRun
```

### 🧪 Run Tests

```bash
./gradlew test
```

### 📊 Code Coverage

- 87% coverage across meaningful code
- Service layer fully tested (including filtering & sorting branches)
- Controller layer tested with MockMvc

### 🚀 Future Improvements

- Angular frontend UI
- User dashboards and analytics
- Distributed caching / Redis
- Multi-device refresh token support
- API monitoring and metrics

### 👨‍💻 Author

Aziz Ali

GitHub: https://github.com/aaali-software

LinkedIn: https://www.linkedin.com/in/aziz-ali-5518128a/

### ⭐ Notes

This project was built to simulate a real-world backend service, focusing on:

- clean architecture
- test-driven development practices
- production-ready API patterns
- cloud deployment with Docker

## 📄 License

MIT License  
See `LICENSE` file for details.
