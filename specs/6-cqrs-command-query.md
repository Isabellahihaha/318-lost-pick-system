# CQRS Command and Query Specification

## 1. Purpose
This document defines how the Lost & Found Platform applies CQRS principles, including:
1. Separation between command and query application services.
2. Write and read model responsibilities.
3. User story mapping to command or query workflows.
4. Command and query catalogs.
5. Handler wiring and asynchronous projection updates.
6. Consistency and error handling models.

---

## 2. CQRS Principles
1. **Commands mutate state** and execute domain invariants across item and claim aggregates.
2. **Queries read state** from read models/projections and do not modify domain state.
3. Write models focus on transactional integrity while read models are optimized for UI rendering and AI searches.
4. Read models are eventually consistent with the write model via Kafka event streaming.

---

## 3. Application Service Split

### 3.1 Command Application Service
- Accepts command requests from API endpoints (`/lost-items`, `/found-items`, `/claims`).
- Routes payloads to command handlers.
- Loads aggregates, validates business rules, and saves state changes.
- Publishes domain events to Kafka for downstream consumers (e.g., matching engine).

### 3.2 Query Application Service
- Accepts read requests from API endpoints (`/matches`, list queries).
- Routes to query handlers.
- Reads strictly from query-optimized projections and materialized views.
- Shapes response DTOs directly matching API contracts.

### Constraint
- Query services must never invoke domain mutation rules or execute repository save operations.

---

## 4. Write and Read Models

### 4.1 Write Model (Aggregates)
- Event-sourced and transactional entities: `LostItem`, `FoundItem`, `OwnershipClaim`, `PotentialMatch`.

### 4.2 Read Model (Projections)
- `item_search_projection`: Optimized index supporting category, location, and keyword filtering for AI matching and browsing.
- `claim_status_projection`: Aggregated status records powering real-time status feeds.

---

## 5. User Story Classification

| User Story ID | User Story Description | CQRS Type | Through Aggregate |
|:--------------| :--- | :--- | :--- |
| LI1 / B1      | Report a new lost item / Manage request | Command | Yes (`LostItem`) |
| LI2 / B2      | Retrieve/list registered lost items | Query | No |
| FI1 / L1      | Report a new found item / Manage registry | Command | Yes (`FoundItem`) |
| FI2 / L2      | Retrieve/list registered found items | Query | No |
| MS1           | Query AI-assisted potential matches | Query | No |
| CS1           | Submit ownership claim for an item | Command | Yes (`OwnershipClaim`) |

---

## 6. Command Catalog

| Command | Target Aggregate | Story | Output Events | Persistence |
| :--- | :--- | :--- | :--- | :--- |
| `CreateLostItemCommand` | `LostItem` | B1 | `LostItemReported` | JPA / Event Store |
| `CreateFoundItemCommand` | `FoundItem` | L1 | `FoundItemReported` | JPA / Event Store |
| `ProcessAIMatchingCommand` | `PotentialMatch` | MS1 | `PotentialMatchFound` | Event Store |
| `SubmitClaimCommand` | `OwnershipClaim` | CS1 | `ClaimSubmitted` | JPA / Event Store |
| `VerifyClaimCommand` | `OwnershipClaim` | CS1 | `ClaimVerified`, `ItemRecovered` | JPA / Event Store |

Command responses return outcome execution metadata (success status, generated IDs, and versioning info) rather than read projections.

---

## 7. Query Catalog

| Query | Story | Read Source | Consistency | Notes |
| :--- | :--- | :--- | :--- | :--- |
| `GetLostItemByIdQuery(id)` | B1 | Lost Item Read Store | Strong (local DB) | Direct lookup by identifier |
| `GetAllLostItemsQuery()` | B2 | `item_search_projection` | Eventual | Powers user dashboard lists |
| `GetFoundItemByIdQuery(id)` | L1 | Found Item Read Store | Strong (local DB) | Direct lookup by identifier |
| `GetAllFoundItemsQuery()` | L2 | `item_search_projection` | Eventual | Powers registry browsing |
| `QueryPotentialMatchesQuery()` | MS1 | Match Projection Store | Eventual | Retrieved by Matching Service endpoints |
