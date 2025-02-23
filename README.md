# Expense Tracker API

A robust REST API for tracking personal expenses built with Spring Boot. This application allows users to manage their expenses, categorize them, and track spending patterns.

## Features

- User management with secure registration and authentication
- Expense tracking with categories
- Detailed expense reporting
- RESTful API design
- Comprehensive test coverage (unit and integration tests)
- Docker support for easy deployment

## Technology Stack

- Java 21
- Spring Boot 3.4.2
- PostgreSQL 16
- Maven
- Docker
- JUnit 5
- Testcontainers
- Spring Security (coming soon)

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker Desktop
- PostgreSQL 16 (local development)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/ishwor/expenses/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── repository/
│   │       ├── service/
│   │       └── exception/
│   └── resources/
│       └── application.yml
├── test/
│   └── java/
│       └── com/ishwor/expenses/
│           ├── controller/
│           ├── service/
│           └── integration/
```

## Getting Started

### Local Development

1. Clone the repository:
```bash
git clone git@github.com:ishworii/Expense-Tracker-API.git
cd Expense-Tracker-API
```

2. Configure database connection in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expenses
    username: your_username
    password: your_password
```

3. Run the application:
```bash
mvn spring-boot:run
```

### Docker Development

1. Build and run using Docker Compose:
```bash
docker-compose up --build
```

## API Endpoints

### Users
- POST `/api/users/register` - Register a new user
- GET `/api/users/{id}` - Get user by ID
- GET `/api/users/email/{email}` - Get user by email

### Categories
- POST `/api/categories` - Create a new category
- GET `/api/categories` - Get all categories
- GET `/api/categories/{id}` - Get category by ID
- DELETE `/api/categories/{id}` - Delete category

### Expenses
- POST `/api/expenses` - Create a new expense
- GET `/api/expenses` - Get all expenses
- GET `/api/expenses/{id}` - Get expense by ID
- GET `/api/expenses/user/{userId}` - Get expenses by user
- PUT `/api/expenses/{id}` - Update expense
- DELETE `/api/expenses/{id}` - Delete expense

## Request/Response Examples

### Create Expense
```json
POST /api/expenses
{
    "amount": 100.50,
    "description": "Grocery shopping",
    "categoryId": 1,
    "userId": 1,
    "expenseDate": "2025-02-23"
}
```

### Response
```json
{
    "id": 1,
    "amount": 100.50,
    "description": "Grocery shopping",
    "categoryId": 1,
    "categoryName": "Groceries",
    "userId": 1,
    "userName": "John Doe",
    "createdAt": "2025-02-23T12:00:00Z"
}
```

## Testing

The project includes both unit and integration tests. To run the tests:

```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest=*Test

# Run only integration tests
mvn test -Dtest=*IntegrationTest
```

## Docker Support

The application can be containerized using Docker:

```bash
# Build the image
docker build -t expenses-app .

# Run with Docker Compose
docker-compose up
```

## CI/CD

GitHub Actions workflow is configured for:
- Running tests on each push
- Building Docker image
- Code quality checks

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Future Enhancements

- [ ] Add authentication with JWT
- [ ] Implement role-based authorization
- [ ] Add expense statistics and reporting
- [ ] Implement file upload for receipts
- [ ] Add API documentation with Swagger
- [ ] Implement email notifications

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Author

Ishwor Khanal

