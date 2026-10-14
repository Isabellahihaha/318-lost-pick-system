# Technical Architecture

## Microservice Architecture
This application is structured as a multi-module Maven microservice architecture comprising four core service domains:
1. **User Service** (`user-service`) – Manages user identity, registration, roles, and profiles.
2. **Lost Service** (`lost-service`) – Manages user-submitted lost item reports.
3. **Found Service** (`found-service`) – Manages found item registries and item statuses.
4. **Matching Service** (`matching-service`) – Integrates AI/LangChain4j to handle intelligent text analysis, scoring, and automated item cross-referencing.
5. **Claim & Recovery Service** (`claim-service`) – Manages ownership verification workflows and recovery state tracking.

## Layered Architecture
Each microservice module is structured into four distinct layers:
1. **Presentation Layer** (Controllers - `@RestController`)
2. **Service Layer** (Business Logic - `@Service`)
3. **Domain Layer** (Core Domain Models & Rules - `@Entity` / Aggregates)
4. **Data Access Layer** (Repositories - `@Repository` / Spring Data JPA)

## Repository Structure 
The project is structured as a multi-module Maven repository (`318-lost-pick-system`):
- `user-service`: Handles user authentication records and profile details.
- `lost-service`: Module for browsing registries and posting lost item requests.
- `found-service`: Module for managing found item logs and item status updates.
- `matching-service`: AI-powered engine leveraging LangChain4j for natural language processing and match scoring.
- `claim-service`: Workflow manager for handling proof submissions and finder verifications.

## Technology Stack
- **JDK & Build**: Java 21, Apache Maven (Multi-module POM)
- **Framework**: Spring Boot 3.4.x
    - **Controller Layer**: `@RestController` 
    - **Service Layer**: `@Service`
    - **Domain Layer**: `@Entity` / DDD Aggregates
    - **Data Access Layer**: `@Repository` / Spring Data JPA
- **Database**: H2 in-memory database for local development and testing, SQLite/JPA persistence stores.
- **Event Broker**: Apache Kafka for asynchronous event-driven messaging (e.g., `LostItemReported`, `PotentialMatchFound`).
- **AI / Agentic Integration**: LangChain4j (`langchain4j-open-ai`, `langchain4j-google-ai-gemini`) for smart text extraction, follow-up generation, and semantic match ranking.
- **Unit & Integration Testing**: `MockMvc`, `@SpringBootTest`, `JUnit 5`

## Design Principles
- **Domain-Driven Modularization**: Each microservice encapsulates its own bounded context, domain entities, repositories, and services.
- **Contract-First API Design**: REST APIs follow standardized HTTP methods, status codes (`200`, `201`, `400`, `404`, `500`), and JSON payloads defined via OpenAPI 3.0.
- **Asynchronous Event-Driven Integration**: Decoupled communication between services via Apache Kafka for real-time match processing and claim status updates.
