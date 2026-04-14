# Task API 🚀

A production-style **Task Management REST API** built with **Spring Boot**, featuring PostgreSQL persistence, pagination, sorting, filtering, and full test coverage.

---

## 🧠 Features

- ✅ CRUD operations for tasks

- ✅ Filtering by status and priority

- ✅ Pagination (`page`, `size`)

- ✅ Sorting (`sort=field,asc|desc`)

- ✅ PostgreSQL integration

- ✅ Swagger/OpenAPI documentation

- ✅ 83%+ test coverage (JUnit + Mockito)

---

## 🛠️ Tech Stack

- **Java 21**

- **Spring Boot**

- **Spring Data JPA**

- **PostgreSQL**

- **Gradle**

- **JUnit 5 + Mockito**

- **Swagger (OpenAPI)**

---

## 📦 API Endpoints

| Method | Endpoint | Description |

|------|--------|-------------|

| GET | `/api/tasks` | Get all tasks (supports filtering, pagination, sorting) |

| GET | `/api/tasks/{id}` | Get task by ID |

| POST | `/api/tasks` | Create a new task |

| PATCH | `/api/tasks/{id}/status` | Update task status |

| PUT | `/api/tasks/{id}` | Update full task |

| DELETE | `/api/tasks/{id}` | Delete task |

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

### 📘 Swagger UI

Once the app is running:

```text
http://localhost:8080/swagger-ui/index.html
```

### 🧪 Example Request

Create Task

POST /api/tasks

```text
{
  "title": "Finish backend project",
  "description": "Complete Task API with tests and deployment",
  "priority": "HIGH",
  "dueDate": "2026-04-20T18:00:00"
}
```

### ⚙️ Local Setup

1. Clone repo

```text
git clone https://github.com/aaali-software/task-api.git
cd task-api
```

2. Configure PostgreSQL

Create a database:

```text
CREATE DATABASE taskdb;
```

Update application.properties :

```text
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. Run the app

```text
./gradlew bootRun
```

### 🧪 Run Tests

```text
./gradlew test
```

### 📊 Code Coverage

83%+ coverage
Service layer fully tested (including filtering & sorting branches)
Controller layer tested with MockMvc

### 🚀 Future Improvements

- Authentication (JWT)
- Role-based access control
- Dockerization
- Cloud deployment (Render / Railway)
- API rate limiting

### 👨‍💻 Author

Aziz Ali

GitHub: https://github.com/aaali-software

LinkedIn: https://www.linkedin.com/in/aziz-ali-5518128a/

### ⭐ Notes

This project was built to simulate a real-world backend service, focusing on:

- clean architecture
- test-driven development practices
- production-ready API patterns

## 📄 License

MIT License  
See `LICENSE` file for details.
