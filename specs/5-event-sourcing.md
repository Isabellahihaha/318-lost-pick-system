# Event Sourcing Specification: Lost & Found Recovery Platform

## 1. Scope
- This specification defines the event-sourcing mechanism for core aggregates in the platform, specifically focusing on **`LostItemAggregate`** and **`FoundItemAggregate`** where state transitions are tracked as immutable audit streams.
- Secondary subsystems (such as basic user profiles and direct CRUD utility tables) remain standard database-backed entities.

---

## 2. Goals
- Preserve complete lifecycle histories and state transitions of lost and found items as immutable events.
- Power real-time tracking, analytics, and asynchronous notifications via event streaming.
- Provide a reliable replayable log for auditing ownership claims and match generation history.

---

## 3. Event Model

### 3.1 Event Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `eventId` | UUID | Unique event identity. Used for idempotency deduplication. |
| `aggregateId` | String | Identity of the aggregate instance (e.g., `"LOST-101"`) |
| `aggregateType` | String | Aggregate type (e.g., `"LostItemAggregate"`, `"FoundItemAggregate"`) |
| `eventType` | String | One of the event types listed in §3.3 |
| `aggregateVersion` | long | Monotonically increasing version of the aggregate stream after this event |
| `occurredAt` | timestamp | Wall-clock time the event was produced |
| `payload` | JSON | Event-specific data (state change fields) |

### 3.2 Event Stream Identity & Broker
- Stream key = `aggregateType` + `aggregateId`
- Distributed Event Broker: **Apache Kafka** topics handle publishing and consumer streaming across microservices[cite: 1].

### 3.3 Event Types for Item Aggregates

| Event Type | Triggering Behavior | Key Payload Fields |
| :--- | :--- | :--- |
| `LostItemReported` | User submits a new lost item | `lostItemId`, `userId`, `itemName`, `category`, `location`, `lostDate`[cite: 1] |
| `FoundItemReported` | Finder logs a found item | `foundItemId`, `finderId`, `itemName`, `category`, `location`, `foundDate` |
| `PotentialMatchFound` | AI matching engine links items | `matchId`, `lostItemId`, `foundItemId`, `confidenceScore`[cite: 7] |
| `ClaimSubmitted` | Claimant submits ownership proof | `claimId`, `lostItemId`, `foundItemId`, `claimantId`, `proofDescription`[cite: 7] |
| `ClaimVerified` | Finder/Admin approves verification | `claimId`, `lostItemId`, `foundItemId`, `status` (`VERIFIED`) |
| `ItemRecovered` | Item is returned to owner | `lostItemId`, `foundItemId`, `claimId`, `status` (`RECOVERED`) |
