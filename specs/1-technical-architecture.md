# Lost & Found Platform

CSCI318 group project — A lost-found platform with AI-assisted match scoring between items and requests.

## Tech stack / install before you start
You need to set these on your PATH before running anything:

| Tool | Version | Why |
| :--- | :--- | :--- |
| **JDK** | 21 | All backend services target Java 21 (`<java.version>21</java.version>` in the root `pom.xml`) |
| **Maven** | 3.9+ | Builds and runs every service (`mvn spring-boot:run`) |
| **Python** | 3.x | Serves the static frontend (`py -m http.server` / `python3 -m http.server`) |
| **Docker** | Any recent version | Runs the local Kafka broker (`docker compose up -d`) — see Event-driven matching |

**Database:** Uses SQLite, pulled in automatically through Maven, so no need to set up a database. H2 is used by `user-service`. Both are embedded.

**!!REGARDING AI!!:** A Google AI Studio Gemini API key is required for `matching-service`'s real AI-judged match scoring and reasoning (otherwise it's just keyword match). See **Configuration**. The project runs fine without one, just with substantially lower quality, and no reasoning given for matches.

## Key libraries per service
Handled by Maven, nothing to install manually:

* **lost-service / found-service** — Spring Boot Web, Spring Data JPA, SQLite JDBC + Hibernate Community Dialects, RestTemplate-based clients to each other and to matching-service, Spring Cloud Stream (Kafka binder) for publishing `LostItemReported` and `FoundItemReported` events.
* **user-service** — Spring Boot Web, Spring Data JPA, H2 (in-memory).
* **claim-recovery-service** — Spring Boot Web, Spring Data JPA, SQLite JDBC. Manages endpoints for initiating item claims, submitting ownership verification proof, updating recovery statuses, and real-time notifications[cite: 1].
* **Matching-Service** — Spring Boot Web, Spring Boot Validation, Lombok, RestTemplate, calls to the Gemini generateContent API, Spring Cloud Stream (Kafka binder) for consuming events and publishing `PotentialMatchFound`.
* **frontend** — plain HTML/CSS/JS, no build step, no Node/npm required.

## Services & ports

| Service | Port | Datastore |
| :--- | :--- | :--- |
| **lost-service** | 8081 | SQLite (`lostservice.db`) |
| **found-service** | 8082 | SQLite (`foundservice.db`) |
| **user-service** | 8083 | H2 (in-memory) |
| **claim-recovery-service** | 8084 | SQLite (`claimservice.db`)[cite: 5] |
| **Matching-Service** | 8085 | — (stateless, calls Gemini) |
| **frontend (static)** | 5500 | — |

Each BE module communicates with each other via plain HTTP and works independently. If a module isn't active, calls from others return no matches instead of outright failing.

## Configuration

`Matching-Service` reads its Gemini API key from the `GEMINI_API_KEY` environment variable (`application.yml`: `key: ${GEMINI_API_KEY:mock-key}`). To set it for a session, in the terminal that will run `Matching-Service`:

**Windows PowerShell:**
```powershell
$env:GEMINI_API_KEY = "your-real-key-here"
