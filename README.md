# Lost & Found Platform

CSCI318 group project — The project title said itself, a lost-found platform with AI-assisted match scoring between items and requests

## Tech stack / install before you start, and need to set these on `PATH` before running anything:

| Tool | Version | Why |
|---|---|---|
| JDK | 21 | All four backend services target Java 21 (`<java.version>21</java.version>` in the root `pom.xml`) |
| Maven | 3.9+ | Builds and runs every service (`mvn spring-boot:run`) |
| Python | 3.x | Serves the static frontend (`py -m http.server` / `python3 -m http.server`) |
| Docker | any recent version, with Compose | Runs the local Kafka broker (`docker compose up -d`) — see [Event-driven matching](#event-driven-matching-kafka) |

Database use SQLite, pulled in auto through Maven, so no need to set up database. H2 is used by user-service. Both are embedded

!!REGARDING AIAIAIAI!!: a Google AI Studio Gemini API key, for matching-service's real AI-judged match scoring and reasoning (otherwise it's just keyword match)(See
##Configuration) the project runs fine without one, just with substantial lower quality, and no reasoning given for match.

### Key libraries per service (handled by Maven, nothing to install manually)

- **lost-service / found-service** — Spring Boot Web, Spring Data JPA, SQLite JDBC + Hibernate Community Dialects, RestTemplate-based clients to each other and to matching-service, Spring Cloud Stream (Kafka binder) for publishing `LostItemReported` and `FoundItemReported` events
- **user-service** — Spring Boot Web, Spring Data JPA, H2 (in-memory)
- **Matching-Service** — Spring Boot Web, Spring Boot Validation, Lombok, RestTemplate, calls to the Gemini `generateContent` API, Spring Cloud Stream (Kafka binder) for consuming those events and publishing `PotentialMatchFound`
- **frontend** — plain HTML/CSS/JS, no build step, no Node/npm required

## Services & ports

| Service | Port | Datastore |
|---|---|---|
| lost-service | 8081 | SQLite (`lostservice.db`) |
| found-service | 8082 | SQLite (`foundservice.db`) |
| user-service | 8083 | H2 (in-memory) |
| Matching-Service | 8085 | — (stateless, calls Gemini) |
| frontend (static) | 5500 | — |

Each BE module communicate with each other via plain HTTP, work independently. If a module isn't active, calls from other return no matches instead of outright fail.

## Configuration

`Matching-Service` reads its Gemini API key from the `GEMINI_API_KEY` environment variable (`application.yml`: `key: ${GEMINI_API_KEY:mock-key}`)
To set it for a session, in the terminal that boutta run Matching-Service

```powershell
# Windows PowerShell
$env:GEMINI_API_KEY = "your-real-key-here"
```

```bash
# macOS / Linux
export GEMINI_API_KEY="your-real-key-here"
```

`$env:`/`export` only apply to that terminal (and anything launched from it afterward), so if need to open another terminal, repeat setting the key there too 

## Event-driven matching (Kafka)

In addition to the original synchronous flow (found-service/lost-service call Matching-Service's `/api/ai-matching/calculate-match` directly and show the result in the same HTTP response), every created lost/found item also publishes a Kafka event, and Matching-Service ALSO consumes those events asynchronously and runs the exact same scoring logic in the background:

- `found-service` → publishes `FoundItemReported` to topic `found-item-reported`
- `lost-service` → publishes `LostItemReported` to topic `lost-item-reported`
- `Matching-Service` → consumes both, re-scores against the opposite side's unresolved
  items via `AiMatchingService.calculateMatchScore(...)`, and for any `HIGH_CONFIDENCE` or
  `SUGGESTED` result, publishes `PotentialMatchFound` to topic `potential-match-found`

**Start the broker before running the services:**

```bash
docker compose up -d
```

This brings up a single-node Kafka broker (KRaft mode, no Zookeeper needed) on `localhost:9092`, defined in `docker-compose.yml` at the repo root. Leave it running in the background for the whole session; `docker compose down` stops it when you're done.

### Verification checklist

1. `docker compose up -d`, then `docker compose ps` — `lost-pick-kafka` should show as `healthy` within ~30s.
2. `mvn clean install` **from the project root** (not inside a single module) — this is the first time all four services need to resolve `spring-cloud-starter-stream-kafka` from Maven Central; watch for dependency-resolution errors, especially around the `spring-cloud-dependencies` BOM import in the root `pom.xml`.
3. Start all five processes as in (##How to run) below
4. Watch the `found-service` and `lost-service` console logs on startup for Kafka binder messages. No stack trace should appear just from starting up, even before you create anything.
5. Watch the `Matching-Service` console for consumer-group messages (`Discovered coordinator`, `(Re-)joining group matching-service`) — this means it successfully connected as a Kafka consumer.
6. POST a found item (`POST http://localhost:8082/api/found-items`) or use the frontend (JUST USE THE FRONTEND DAWG. The REST response should arrive exactly as before (unchanged). Then check `Matching-Service`'s console for the `"Consumed FoundItemReported..."` log line from `MatchEventOrchestrator` — that confirms the event actually made the round trip through Kafka.
7. If you have candidate items on the other side (e.g. an unresolved lost item with an overlapping description/category/location), look for a `"Published PotentialMatchFound"` log line in `Matching-Service`.
8. //OPTIONAL// You may just close Kafka terminal and repeat step6; the found/lost item POST should **still succeed** (just without the async event)
If step 2 fails on the Spring Cloud BOM import, double-check `<spring-cloud.version>` in the root `pom.xml` (currently `2024.0.3`, the latest patch release compatible with Spring Boot 3.4.2) against whatever Spring Boot version the root `pom.xml`'s parent is pinned to by the time you read this.

## How to run

Open 5 terminals, one per module, all from the project root (plus Kafka above)

```powershell
cd lost-service
mvn spring-boot:run          # http://localhost:8081

cd found-service
mvn spring-boot:run          # http://localhost:8082

cd user-service
mvn spring-boot:run          # http://localhost:8083

cd Matching-Service
mvn spring-boot:run          # http://localhost:8085

cd frontend
py -m http.server 5500
```

On macOS/Linux, use `python3 -m http.server 5500` instead

Once all five are up, open 'http://localhost:5500' in your browser.

### Troubleshooting

- **"Port XXXX was already in use"** — a previous run of that same service is probably still alive in another terminal, Kill it
- **Frontend requests get blocked by CORS** — each backend only allows `http://localhost:5500`, `http://127.0.0.1:5500`, and `http://localhost:5173` by default. Serve the frontend on one of those
- **Matching-Service returns `matchReasoning: "... AI comparison was unavailable"`** — check `GEMINI_API_KEY` is actually set in the terminal running `Matching-Service`, or if it set in Matching-Service application.yml
- **found-service/lost-service logs show repeated Kafka connection warnings** — Kafka isn't running (`docker compose up -d` from the repo root) or isn't reachable on `localhost:9092`. This is harmless to the REST API itself, but means async matching via Kafka isn't happening.
- **Matching-Service never logs `"Consumed FoundItemReported..."` / `"...LostItemReported..."`**
  — check: (a) Kafka is up and healthy, (b) `found-service`/`lost-service` logs don't show
  publish warnings for that item, (c) all three services point at the same broker
  (`spring.cloud.stream.kafka.binder.brokers=localhost:9092` in each `application.properties`
  / `application.yml`).
