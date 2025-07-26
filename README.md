# Spring Boot Playground

A demo project to learn Spring Boot through. This application implements basic CRUD operations with MongoDB and SSL configuration.

## Features

- **Spring Boot 3.5.3** with Java 24
- **MongoDB Integration** with Spring Data MongoDB
- **SSL/HTTPS Support** with self-signed certificates
- **RESTful API** endpoints for CRUD operations
- **Development Tools** with Spring Boot DevTools for hot reload

## Prerequisites

- Java 24+
- MongoDB running on localhost:27017
- Maven

## MongoDB Setup

This application expects MongoDB to be running with the following configuration:
- **Host:** localhost
- **Port:** 27017
- **Database:** letsplay
- **Username:** rootuser
- **Password:** rootpass
- **Auth Database:** admin

### Start MongoDB with Docker

```bash
docker compose up -d
```

## Running the Application

1. **Clone and navigate to the project:**
   ```bash
   git clone https://github.com/heshamalmosawi/springboot-playground
   cd springboot-playground
   ```

2. **Ensure MongoDB is running** (see MongoDB Setup above)

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on **https://localhost:8443**

⚠️ **Note:** Your browser will show a security warning for the self-signed certificate. Click "Advanced" and "Proceed to localhost" to continue.

## API Testing with Postman

### Import Postman Collection

The project includes a Postman collection with pre-configured requests for all endpoints:

###### **Import the collection:**
   - Open Postman
   - Click "Import" 
   - Select `postman/SpringBoot-Playground.postman_collection.json`

### API Endpoints

The Postman collection includes the following endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST  | `/auth/register`  | Register a new uesr |
| POST  | `/auth/login`     | Login with existing user |
| GET   | `/products`       | Get all products |
| GET   | `/products/{id}`  | Get single product by ID |
| POST  | `/products`       | Create new product |
| PUT   | `/products/{id}`  | Replace a product with a new one |
| PATCH | `/products/{id}`  | Update certain fields of a product |
| DELETE | `/products/{id}`  | Delete a product by ID |
| GET    | `/users` | Get all users - only accessibly by admin|
| GET    | `/users/{id}` | Get item by ID - only accessibly by admin|
| PATCH   | `/users/{id}` | Update certain fields of a user - only accessibly by admin |
| DELETE | `/users/{id}` | Delete user - only accessibly by admin |

## Configuration

Key configuration properties in `application.properties`:

```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=letsplay
spring.data.mongodb.username=rootuser
spring.data.mongodb.password=rootpass

# SSL Configuration
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=12345678
```

## Development

- **Hot Reload:** Spring Boot DevTools automatically restarts the application when code changes
- **Debug Mode:** Add `--debug` flag to see detailed startup information
- **Logging:** Set logging levels in `application.properties`

## Building

Create a production JAR:
```bash
./mvnw clean package
```

Run the JAR:
```bash
java -jar target/crudmongo-0.0.1-SNAPSHOT.jar
```

## Testing
Use the provided Postman collection for comprehensive API testing.
