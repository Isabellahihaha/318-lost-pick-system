# Technical Architecture

## Microservice Architecture
This application contains two **Microservice Modules**:
- **Browsing Service** (`lost-service`)
- **Posting Service** (`found-service`)

## Layered Architecture
Each microservice module is structured into four distinct layers:
1. **Presentation Layer** (Controllers - `@RestController`)
2. **Service Layer** (Business Logic - `@Service`)
3. **Domain Layer** (Core Domain Models & Rules - `@Entity`)
4. **Data Access Layer** (Repositories - `@Repository`)

## Repository Structure 
The project is structured as a multi-module Maven repository:
- `lost-service`: Module for browsing found item registries, posting lost item requests.
- `found-service`: Module for managing found item registries, status checks, status updates, information updates, item deletion.

## Technology Stack
- **JDK & Build**: Java 21, Apache Maven (Multi-module POM)
- **Framework**: Spring Boot 3.4.x
    - **Controller Layer**: `@RestController` 
    - **Service Layer**: `@Service`
    - **Domain Layer**: `@Entity`
    - **Data Access Layer**: `@Repository` / Spring Data JPA
- **Database**: H2 in-memory database for development, SQLite
- **AI / Agentic Integration**: LangChain4j (`langchain4j-open-ai`, `langchain4j-google-ai-gemini`)
- **Unit & Integration Testing**: `MockMvc`, `@SpringBootTest`, `JUnit 5`

## Design Principles
- **Domain-Driven Modularization**: Each service encapsulates its own domain entities, services, and repositories.
- **Contract-First API Design**: REST APIs follow standardized HTTP methods, response codes (200, 201, 400, 404, 500), and JSON payloads.
- **Stateless Services**: Microservices remain stateless to allow independent deployment and scaling.

## Future Considerations
- Event-driven integration between `lost-service` and `found-service` via Kafka / RabbitMQ or Spring Cloud OpenFeign.
- Persistent database integration (PostgreSQL / MySQL) for production deployment.
