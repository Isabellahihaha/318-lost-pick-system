# Technical Architecture

## Microservice Architecture
This application contains two **Microservice Modules**:
- **Lost Service**
- **Found Service**

## Layered Architecture
Each microservice module is structured into four distinct layers:
1. **Presentation Layer** (Controllers)
2. **Service Layer** (Business Logic)
3. **Domain Layer** (Core Domain Models & Rules)
4. **Data Access Layer** (Repositories)

## Repository Structure 
The project is structured as a multi-module repository:
- `Lost-service`: Module for the Lost Service
- `Found-service`: Module for the Found Service

## Technology Stack
- **JDK & Build**: Java 21, Apache Maven (Multi-module POM)
- **Framework**: Spring Boot
    - **Controller Layer**: `@RestController` 
    - **Service Layer**: `@Service`
    - **Domain Layer**: `@Entity`
    - **Data Access Layer**: `@Repository`
- **Database**: H2 in-memory database for development
- **Unit & Integration Testing**: `MockMvc`, `@SpringBootTest`, `JUnit 5`

## Design Principles
Todo

## Future Considerations
Todo
